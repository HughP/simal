package uk.ac.osswatch.simal.rdf.jena;

import java.io.Writer;
import java.net.URL;
import java.util.Set;

import javax.xml.namespace.QName;

import org.openrdf.concepts.doap.Project;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.repository.RepositoryException;

import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.DuplicateQNameException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class SimalRepository implements ISimalRepository {

  private static ISimalRepository instance;
  
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

  public void add(URIImpl predicate, URIImpl subject, URIImpl object)
      throws RepositoryException, SimalRepositoryException {
    // TODO Auto-generated method stub
    
  }

  public void add(String data) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    
  }

  public void addProject(URL url, String baseURI)
      throws SimalRepositoryException {
    // TODO Auto-generated method stub
    
  }

  public void addRDFXML(URL url, String baseURL)
      throws SimalRepositoryException {
    // TODO Auto-generated method stub
    
  }

  public IPerson createPerson(QName qname) throws SimalRepositoryException,
      DuplicateQNameException {
    // TODO Auto-generated method stub
    return null;
  }

  public IProject createProject(QName qname) throws SimalRepositoryException,
      DuplicateQNameException {
    // TODO Auto-generated method stub
    return null;
  }

  public void destroy() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    
  }

  public IDoapCategory findCategory(QName qname)
      throws SimalRepositoryException {
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

  public QName getDefaultQName(Project project) {
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

  public IPerson getPerson(QName qname) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public IProject getProject(QName qname) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    return null;
  }

  public void initialise() throws SimalRepositoryException {
    // TODO Auto-generated method stub
    
  }

  public void remove(QName qname) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    
  }

  public void setIsTest(boolean newValue) throws SimalRepositoryException {
    // TODO Auto-generated method stub
    
  }

  public void writeXML(Writer writer, QName qname)
      throws SimalRepositoryException {
    // TODO Auto-generated method stub
    
  }

  /**
   * Return true if this repository has been successfully initialised and is
   * ready to be used, otherwise return false.
   * 
   * @return
   */
  public boolean isInitialised() {
    return true;
  }

}
