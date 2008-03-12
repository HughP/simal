package uk.ac.osswatch.simal.rdf.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import org.openrdf.model.BNode;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.RDFXMLWriter;

import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * Validates and annotates an RDF/XML file for use within SIMAL. This handler
 * does things like ensure a valid QName is present and key Simal information is
 * captured. <br/>
 * <h3>FIXME</h3>
 * At present this handler wraps an RDFXMLWriter and writes the file to the
 * local disk. This can then be read from the disk complete with annotations.
 * This is unwanted overhead, ideally this handler will be re-written to
 * annotate the file on the fly as it is being added to the repository.
 * 
 */
public class AnnotatingRDFXMLHandler implements RDFHandler {

  private RDFXMLWriter handler;
  private Resource currentSubject;
  private Set<QName> projectQNames = new HashSet<QName>();
  private SimalRepository repository;

  /**
   * Create a new AnnotatingRDFXMLHandler that will write to a file.
   * 
   * @param file
   *          the file to write the annotated RDF/XML output to.
   * @throws IOException
   *           if there is a problem creating the output file
   */
  public AnnotatingRDFXMLHandler(File file, SimalRepository repository)
      throws IOException {
    handler = new RDFXMLWriter(new FileWriter(file));
    this.repository = repository;
  }

  public void endRDF() throws RDFHandlerException {
    handler.endRDF();
  }

  public void handleComment(String comment) throws RDFHandlerException {
    handler.handleComment(comment);
  }

  public void handleNamespace(String prefix, String name)
      throws RDFHandlerException {
    handler.handleNamespace(prefix, name);
  }

  public void handleStatement(Statement inStatement) throws RDFHandlerException {
    Statement outStatement = inStatement;
    Resource outSubject = inStatement.getSubject();
    URI outPredicate = inStatement.getPredicate();
    Value outValue;
    if (currentSubject == null) {
      currentSubject = outSubject;
    }

    if (inStatement.getObject().stringValue().equals(
        SimalRepository.DOAP_PROJECT_URI)
        || currentSubject.stringValue().startsWith(
            SimalRepository.DEFAULT_PROJECT_NAMESPACE_URI)
        || outPredicate.stringValue().startsWith(
            SimalRepository.DOAP_NAMESPACE_URI)) {
      outSubject = verifyNode(inStatement.getSubject(),
          SimalRepository.DEFAULT_PROJECT_NAMESPACE_URI);
      currentSubject = outSubject;
      if (inStatement.getObject().stringValue().equals(
          SimalRepository.DOAP_PROJECT_URI)) {
        newProject(outSubject);
      }
    } else if (inStatement.getObject().stringValue().startsWith(
        SimalRepository.FOAF_NAMESPACE_URI)
        || currentSubject.stringValue().startsWith(
            SimalRepository.DEFAULT_PERSON_NAMESPACE_URI)) {
      outSubject = verifyNode(inStatement.getSubject(),
          SimalRepository.DEFAULT_PERSON_NAMESPACE_URI);
      currentSubject = outSubject;
    }

    outValue = fixEncoding(inStatement.getObject());

    if (inStatement.getPredicate().stringValue().startsWith(
        SimalRepository.FOAF_NAMESPACE_URI)) {
      // FIXME: for some reason elmo does not support foaf:name, for
      // now just convert to givenname
      String predicateValue = outPredicate.stringValue();
      if (predicateValue.endsWith("/name")) {
        outPredicate = new URIImpl(predicateValue.substring(0, predicateValue
            .length() - 5)
            + "/givenname");
      }
      if (predicateValue.endsWith("/knows")) {
        outValue = verifyNode(inStatement.getObject(),
            SimalRepository.DEFAULT_PERSON_NAMESPACE_URI);
      }
    } else {
      outPredicate = inStatement.getPredicate();
    }

    outStatement = new StatementImpl(outSubject, outPredicate, outValue);

    handler.handleStatement(outStatement);
  }

  /**
   * Checks to see if this is a new project in the repository. If it is then
   * ensure that it has a unique Simal identifier and is recorded in the
   * list of projects found in the last file processed. If the project
   * already exists then just add it to the list of files processed 
   * (there will already be a unique identifier in the repo).
   * 
   * @param project
   * @throws RDFHandlerException
   */
  private void newProject(Resource project) throws RDFHandlerException {
    QName qname = new QName(project.stringValue());
    try {
      if (repository.getProject(qname) != null) {
        projectQNames.add(qname);
        return;
      }
    } catch (SimalRepositoryException e) {
      throw new RDFHandlerException(
          "Unable to verify if a project already exists", e);
    }

    Value idValue;
    try {
      idValue = new LiteralImpl(SimalRepository.getNewProjectID());
      Statement idStatement = new StatementImpl(project, new URIImpl(
          SimalRepository.SIMAL_ID), idValue);
      handler.handleStatement(idStatement);

      projectQNames.add(qname);
    } catch (Exception e) {
      throw new RDFHandlerException(
          "Unable to save the Simal propertis file, aborting file annotation",
          e);
    }
  }

  private Value fixEncoding(final Value inValue) throws RDFHandlerException {
    Value outValue = inValue;

    if (inValue instanceof LiteralImpl) {
      String strValue = inValue.stringValue();
      try {
        byte[] utf8Bytes = strValue.getBytes("UTF8");
        outValue = new LiteralImpl(new String(utf8Bytes));
      } catch (UnsupportedEncodingException e) {
        throw new RDFHandlerException("Unable to encode value", e);
      }
    }
    return outValue;
  }

  /**
   * Checks to see if a resource is a blank node If it is a blank node we assign
   * a default QName to it because Elmo can't deal well with blank nodes.
   * 
   * @param inSubject
   * @return
   */
  private Resource verifyNode(final Resource inSubject, final String defaultURI) {
    Resource outSubject = inSubject;
    if (inSubject instanceof BNode) {
      outSubject = new URIImpl(defaultURI + inSubject.stringValue());
    }
    return outSubject;
  }

  /**
   * Checks to see if a value is a blank node If it is a blank node we assign a
   * default QName to it because Elmo can't deal well with blank nodes.
   * 
   * @param inSubject
   * @return
   */
  private Value verifyNode(final Value inObject, final String defaultURI) {
    Value outObject = inObject;
    if (inObject instanceof BNode) {
      outObject = new URIImpl(defaultURI + inObject.stringValue());
    }
    return outObject;
  }

  public void startRDF() throws RDFHandlerException {
    handler.handleNamespace("simal", SimalRepository.SIMAL_NAMESPACE_URI);
    handler.startRDF();
  }

  /**
   * Get all project QNames found or generated in the last file to be annotated
   * by this handler.
   * 
   * @return
   */
  public Set<QName> getProjectQNames() {
    return projectQNames;
  }

}
