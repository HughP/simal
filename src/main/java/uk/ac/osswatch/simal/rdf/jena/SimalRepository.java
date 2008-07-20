package uk.ac.osswatch.simal.rdf.jena;

import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.util.Set;

import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.jena.Project;
import uk.ac.osswatch.simal.rdf.AbstractSimalRepository;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class SimalRepository extends AbstractSimalRepository {
  
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
    try {
      model.read(url.openStream(), baseURI);
    } catch (IOException e) {
      throw new SimalRepositoryException("Unable to open stream for " + url, e);
    }    
  }

  public void add(String data) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    
  }

  public void addRDFXML(URL url, String baseURL)
      throws SimalRepositoryException {
    // TODO Auto-generated method stub
    
  }

  public IPerson createPerson(URI uri) throws SimalRepositoryException,
      DuplicateURIException {
    // TODO Auto-generated method stub
    return null;
  }

  public IProject createProject(URI uri) throws SimalRepositoryException,
      DuplicateURIException {
    // TODO Auto-generated method stub
    return null;
  }

  public void destroy() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    
  }

  public IDoapCategory findCategory(URI uri) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
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

  public String getNewProjectID() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public IPerson getPerson(URI uri) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public IProject getProject(URI uri) throws SimalRepositoryException {
    return new Project(model.getResource(uri.toString()));
  }

  public void remove(URI uri) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    
  }

  public void writeXML(Writer writer, URI uri) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    
  }

}
