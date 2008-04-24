/*
 * Copyright 2007 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.osswatch.simal.rdf.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
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
  private static final String PROJECT_WITHOUT_ID = "Project without ID";
  private static final String PROJECT_WITH_ID = "Project";
  private static final String PERSON_WITHOUT_ID = "Person without ID";
  private static final String PERSON_WITH_ID = "Person";

  private static final Logger logger = LoggerFactory
      .getLogger(AnnotatingRDFXMLHandler.class);

  private RDFXMLWriter handler;
  private Stack<Resource> subjects = new Stack<Resource>();
  private Stack<String> subjectType = new Stack<String>();
  private Set<QName> projectQNames = new HashSet<QName>();
  private Set<QName> personQNames = new HashSet<QName>();
  private Set<String> personSha1Sums = new HashSet<String>();
  private Set<String> seeAlsos = new HashSet<String>();
  private SimalRepository repository;
  private HashMap<String, Value> bnodeMap = new HashMap<String, Value>();
  private boolean passThrough = false;

  private URIImpl sourceURL;

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
    file.createNewFile();
    handler = new RDFXMLWriter(new FileWriter(file));
    this.repository = repository;
  }

  public void endRDF() throws RDFHandlerException {
    // clear out any subjects not yet closed off
    while (!subjects.empty()) {
      fixAnnotations(null);
    }
    handler.endRDF();
    sourceURL = null;
  }

  public void handleComment(String comment) throws RDFHandlerException {
    handler.handleComment(comment);
  }

  public void handleNamespace(String prefix, String name)
      throws RDFHandlerException {
    handler.handleNamespace(prefix, name);
  }

  public void handleStatement(Statement inStatement) throws RDFHandlerException {
    logger.debug("Processing statement: " + inStatement);
    if (passThrough) {
      handler.handleStatement(inStatement);
      logger.debug("Passing statement through to standard RDF handler");
      return;
    }
    Statement outStatement = inStatement;
    Resource outSubject = inStatement.getSubject();
    URI outPredicate = inStatement.getPredicate();
    Value outValue = fixEncoding(inStatement.getObject());

    String uri;
    if (isNewProjectType(inStatement)) {
      uri = SimalRepository.DEFAULT_PROJECT_NAMESPACE_URI;
    } else if (isNewPersonType(inStatement) || isNewParticipant(inStatement)
        || isFoafPredicate(inStatement)) {
      uri = SimalRepository.DEFAULT_PERSON_NAMESPACE_URI;
    } else {
      if (subjects.empty()) {
        logger.debug("We don't recognise this type and we are not processing an existing recognised type, so passing statement through to standard RDF handler");
        passThrough = true;
        handler.handleStatement(inStatement);
        return;
      } else {
        uri = subjects.peek().stringValue();
      }
    }
    outSubject = verifyNode(inStatement.getSubject(), uri);
    outValue = verifyNode(inStatement.getObject(), uri);

    if (isNewProjectType(inStatement)) {
      newProject(outSubject);
    }

    if (isNewPersonType(inStatement)) {
      newPerson(outSubject);
    }

    fixAnnotations(outSubject);

    if (isFoafPredicate(inStatement)) {
      // FIXME: for some reason elmo does not support foaf:name, for
      // now just convert to givenname
      String predicateValue = outPredicate.stringValue();
      if (predicateValue.endsWith("/name")) {
        outPredicate = new URIImpl(predicateValue.substring(0, predicateValue
            .length() - 5)
            + "/givenname");
      } else if (predicateValue.endsWith("/mbox")) {
        try {
          String sha1sum = getSHA1(outValue.stringValue());
          StatementImpl statement = new StatementImpl(outSubject, new URIImpl(
              SimalRepository.FOAF_MBOX_SHA1SUM_URI), new LiteralImpl(sha1sum));
          handleStatement(statement);
          personSha1Sums.add(sha1sum);
        } catch (NoSuchAlgorithmException e) {
          throw new RDFHandlerException("Unable to get SHA1 algorithm", e);
        }
      }
    } else if (isRDFSPredicate(inStatement)) {
      String predicateValue = outPredicate.stringValue();
      if (predicateValue.endsWith("seeAlso")) {
        seeAlsos.add(outValue.stringValue());
      }
    } else {
      outPredicate = inStatement.getPredicate();
    }

    if (isSimalPredicate(inStatement)) {
      String predicateValue = outPredicate.stringValue();
      String id = inStatement.getObject().stringValue();

      // look to see if this id is available, if it is use it, otherwise drop it
      try {
        if (predicateValue.equals(SimalRepository.SIMAL_URI_PERSON_ID)) {
          IPerson person = repository.findPersonById(id);
          if (person != null) {
            logger
                .warn("Throwing away simal personID from XML file as it is allready in use on this server. ID = "
                    + id + " Owner = " + person.toString());
            return;
          } else {
            subjectType.pop();
            subjectType.push(PERSON_WITH_ID);
          }
        } else if (predicateValue.equals(SimalRepository.SIMAL_URI_PROJECT_ID)) {
          IProject project = repository.findProjectById(id);
          if (project != null) {
            logger
                .warn("Throwing away simal projectID from XML file as it is allready in use on this server. ID = "
                    + id + " Owner = " + project.toString());
            return;
          } else {
            subjectType.pop();
            subjectType.push(PROJECT_WITH_ID);
          }
        }
      } catch (SimalRepositoryException e) {
        e.printStackTrace();
        return;
      }
    }

    outStatement = new StatementImpl(outSubject, outPredicate, outValue);

    handler.handleStatement(outStatement);
    logger.debug("Handled as: " + outStatement);
  }

  /**
   * A simple method for getting an SHA1 hash from a string.
   */
  public String getSHA1(String data) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA");
    StringBuffer sb = new StringBuffer();

    md.update(data.getBytes());
    byte[] digest = md.digest();
    for (int i = 0; i < digest.length; i++) {
      String hex = Integer.toHexString(digest[i]);
      if (hex.length() == 1)
        hex = "0" + hex;
      hex = hex.substring(hex.length() - 2);
      sb.append(hex);
    }
    return sb.toString();
  }

  /**
   * Check appropriate annotations, such as ID and seeAlso are present.
   * 
   * @param outSubject
   * @throws RDFHandlerException
   */
  private void fixAnnotations(Resource outSubject) throws RDFHandlerException {
    if (!subjects.empty()) {
      if (!subjects.peek().equals(outSubject) || outSubject == null) {
        if (subjects.contains(outSubject) || outSubject == null) {
          // if the subject exists in the stack but is not on the top
          // then it must be the next one down (that's just the way it is)
          // We must have finished with the current top of stack.
          // So remove the top of the stack.
          String type = subjectType.pop();
          Resource resource = subjects.pop();
          
          // now do some final annotation
          if (type.startsWith(PROJECT_WITHOUT_ID)) {
            // check ID
            Value idValue;
            StatementImpl statement;
            try {
              idValue = new LiteralImpl(repository.getNewProjectID());
              statement = new StatementImpl(resource, new URIImpl(
                  SimalRepository.SIMAL_URI_PROJECT_ID), idValue);
              handler.handleStatement(statement);
            } catch (Exception e) {
              throw new RDFHandlerException(
                  "Unable to save the Simal properties file, aborting file annotation",
                  e);
            }
            // add see also
            statement = new StatementImpl(resource, new URIImpl(
                SimalRepository.RDFS_URI_SEE_ALSO), sourceURL);
            handler.handleStatement(statement);
          } else if (type.startsWith(PERSON_WITHOUT_ID)) {
            Value idValue;
            try {
              idValue = new LiteralImpl(repository.getNewPersonID());
              Statement idStatement = new StatementImpl(resource, new URIImpl(
                  SimalRepository.SIMAL_URI_PERSON_ID), idValue);
              handler.handleStatement(idStatement);
            } catch (Exception e) {
              throw new RDFHandlerException(
                  "Unable to save the Simal properties file, aborting file annotation",
                  e);
            }
          }
        }
      }
    }
  }

  /**
   * Return true if the predicate for a statement is in the FOAF namespace.
   * 
   * @param inStatement
   * @return
   */
  private boolean isFoafPredicate(Statement inStatement) {
    return inStatement.getPredicate().stringValue().startsWith(
        SimalRepository.FOAF_NAMESPACE_URI);
  }

  /**
   * Return true if the predicate for a statement is in the RDF namespace.
   * 
   * @param inStatement
   * @return
   */
  private boolean isRDFSPredicate(Statement inStatement) {
    return inStatement.getPredicate().stringValue().startsWith(
        SimalRepository.RDFS_NAMESPACE_URI);
  }

  /**
   * Return true if the predicate for a statement is in the Simal namespace.
   * 
   * @param inStatement
   * @return
   */
  private boolean isSimalPredicate(Statement inStatement) {
    return inStatement.getPredicate().stringValue().startsWith(
        SimalRepository.SIMAL_NAMESPACE_URI);
  }

  /**
   * Return true if the statement indicates that this is a new person type.
   * 
   * @param inStatement
   * @return
   */
  private boolean isNewPersonType(Statement inStatement) {
    boolean isPerson = inStatement.getObject().stringValue().startsWith(
        SimalRepository.FOAF_NAMESPACE_URI);
    return isPerson;
  }

  /**
   * Tests to see if the statement is about a new participant in the project.
   * 
   * @param inStatement
   * @return
   */
  private boolean isNewParticipant(Statement inStatement) {
    boolean isPerson = false;

    isPerson = isPerson
        || inStatement.getPredicate().stringValue().startsWith(
            SimalRepository.DOAP_MAINTAINER_URI);
    isPerson = isPerson
        || inStatement.getPredicate().stringValue().startsWith(
            SimalRepository.DOAP_DEVELOPER_URI);
    isPerson = isPerson
        || inStatement.getPredicate().stringValue().startsWith(
            SimalRepository.DOAP_DOCUMENTER_URI);
    isPerson = isPerson
        || inStatement.getPredicate().stringValue().startsWith(
            SimalRepository.DOAP_HELPER_URI);
    isPerson = isPerson
        || inStatement.getPredicate().stringValue().startsWith(
            SimalRepository.DOAP_TESTER_URI);
    isPerson = isPerson
        || inStatement.getPredicate().stringValue().startsWith(
            SimalRepository.DOAP_TRANSLATOR_URI);
    return isPerson;
  }

  /**
   * Return true if the statement indicates that this is a new project type.
   * 
   * @param inStatement
   * @return
   */
  private boolean isNewProjectType(Statement inStatement) {
    return inStatement.getObject().stringValue().equals(
        SimalRepository.DOAP_PROJECT_URI);
  }

  /**
   * Checks to see if this is a new project in the repository. If it is then
   * ensure that it has a unique Simal identifier and is recorded in the list of
   * projects found in the last file processed. If the project already exists
   * then just add it to the list of files processed (there will already be a
   * unique identifier in the repo).
   * 
   * @param project
   * @throws RDFHandlerException
   */
  private void newProject(Resource project) throws RDFHandlerException {
    projectQNames.add(new QName(project.stringValue()));
    subjects.push(project);
    subjectType.push(PROJECT_WITHOUT_ID);
  }

  /**
   * Checks to see if this is a new person in the repository. If it is then
   * ensure that it has a unique Simal identifier and is recorded in the list of
   * people found in the last file processed. If the person already exists then
   * just add it to the list of files processed (there will already be a unique
   * identifier in the repo).
   * 
   * @param person
   * @throws RDFHandlerException
   */
  private void newPerson(Resource person) throws RDFHandlerException {
    subjects.push(person);
    subjectType.push(PERSON_WITHOUT_ID);
    personQNames.add(new QName(person.stringValue()));
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
  private Resource verifyNode(final Resource inSubject, String defaultURI) {
    Resource outSubject = inSubject;
    if (inSubject instanceof BNode) {
      outSubject = (Resource) bnodeMap.get(inSubject.stringValue());
      if (outSubject == null) {
        outSubject = new URIImpl(defaultURI + inSubject.stringValue());
        bnodeMap.put(inSubject.stringValue(), outSubject);
      }
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
  private Value verifyNode(final Value inObject, String defaultURI) {
    Value outObject = inObject;
    if (inObject instanceof BNode) {
      outObject = bnodeMap.get(inObject.stringValue());
      if (outObject == null) {
        outObject = new URIImpl(defaultURI + inObject.stringValue());
        bnodeMap.put(outObject.stringValue(), outObject);
      }
    }
    return outObject;
  }

  public void startRDF() throws RDFHandlerException {
    if (sourceURL == null) {
      throw new RDFHandlerException(
          "Source URL has not been set, must call setSourceURL prior to using the AnnotatingRDFXMLHandler");
    }
    subjects = new Stack<Resource>();
    subjectType = new Stack<String>();
    projectQNames = new HashSet<QName>();
    personQNames = new HashSet<QName>();
    handler.handleNamespace("simal", SimalRepository.SIMAL_NAMESPACE_URI);
    passThrough = false;
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

  /**
   * Set the source URL for the next document to be annotated.
   * 
   * @param url
   */
  public void setSourceURL(URL url) {
    sourceURL = new URIImpl(url.toString());
  }

  /**
   * Get the MBOX SHA1 Sums for found in this file.
   */
  public Set<String> getPeopleSha1Sums() {
    return personSha1Sums;
  }

  public Set<String> getSeeAlsos() {
    return seeAlsos;
  }

}
