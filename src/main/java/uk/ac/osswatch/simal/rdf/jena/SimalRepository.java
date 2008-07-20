package uk.ac.osswatch.simal.rdf.jena;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.jena.Category;
import uk.ac.osswatch.simal.model.jena.Project;
import uk.ac.osswatch.simal.rdf.AbstractSimalRepository;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;

public class SimalRepository extends AbstractSimalRepository {
  private static final Logger logger = LoggerFactory.getLogger(SimalRepository.class);

 
  private Model model;

  /**
   * Use getInstance instead.
   */
  private SimalRepository() {
  }

  /**
   * Get the SimalRepository object. Note that only one of these can exist in a
   * single virtual machine.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public static ISimalRepository getInstance() throws SimalRepositoryException {
    if (instance == null) {
      instance = new SimalRepository();
    }
    return instance;
  }
  
  public void initialise() throws SimalRepositoryException {
    if (model != null) {
      throw new SimalRepositoryException(
          "Illegal attempt to create a second SimalRepository in the same JAVA VM.");
    }
    
    model = ModelFactory.createDefaultModel();
    initialised = true;

    if (isTest) {
      addTestData();
    }
  }
  
  public void addProject(URL url, String baseURI)
      throws SimalRepositoryException {
    logger.info("Adding a project from " + url.toString());

    verifyInitialised();

    Iterator<File> annotatedFiles = null;
    try {
      logger.info("Preprocessing file");
      annotatedFiles = RDFUtils.preProcess(url, baseURI, this).iterator();
      logger.info("Adding processed RDF/XML");
      while (annotatedFiles.hasNext()) {
        addRDFXML(annotatedFiles.next().toURL(), baseURI);
      }
    } catch (IOException e) {
      throw new SimalRepositoryException(
          "Unable to write the annotated RDF/XML file: " + e.getMessage(), e);
    }  
  }

  public void add(String data) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    
  }

  public void addRDFXML(URL url, String baseURI)
      throws SimalRepositoryException {
    try {
      model.read(url.openStream(), baseURI);
    } catch (IOException e) {
      throw new SimalRepositoryException("Unable to open stream for " + url, e);
    }  
  }

  public IPerson createPerson(String uri) throws SimalRepositoryException,
      DuplicateURIException {
    // TODO Auto-generated method stub
    return null;
  }

  public IProject createProject(String uri) throws SimalRepositoryException,
      DuplicateURIException {
    if (containsProject(uri)) {
      throw new DuplicateURIException(
          "Attempt to create a second project with the URI " + uri);
    }

    Property o = model.createProperty( "http://usefulinc.com/ns/doap#Project" );
    Resource r = model.createResource(uri.toString());
    Statement s = model.createStatement(r, RDF.type, o);
    model.add(s);
    
    IProject project = new Project(model.createResource(uri));
    project.setSimalID(getNewProjectID());
    return project;
  }

  public void destroy() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    
  }

  public IDoapCategory findCategory(String uri) throws SimalRepositoryException {
    return new Category(model.getResource(uri.toString()));
  }

  public IPerson findPersonById(String id) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public IPerson findPersonBySeeAlso(String seeAlso)
      throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public IPerson findPersonBySha1Sum(String sha1sum)
      throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public IProject findProjectByHomepage(String homepage)
      throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public IProject findProjectById(String id) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public IProject findProjectBySeeAlso(String seeAlso)
      throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<IDoapCategory> getAllCategories() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<IPerson> getAllPeople() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public Set<IProject> getAllProjects() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public String getAllProjectsAsJSON() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public String getNewPersonID() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public IPerson getPerson(String uri) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public IProject getProject(String uri) throws SimalRepositoryException {
    if (containsProject(uri)) {
      return new Project(model.getResource(uri.toString()));
    } else {
      return null;
    }
  }

  public void remove(String uri) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    
  }

  public void writeXML(Writer writer, String uri) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    
  }

  public boolean containsProject(String uri) {
    Property o = model.createProperty( "http://usefulinc.com/ns/doap#Project" );
    Resource r = model.createResource(uri.toString());
    Statement s = model.createStatement(r, RDF.type, o);
    return model.contains(s);
  }

}
