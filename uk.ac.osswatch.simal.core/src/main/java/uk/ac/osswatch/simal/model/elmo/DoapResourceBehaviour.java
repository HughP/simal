package uk.ac.osswatch.simal.model.elmo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.openrdf.concepts.doap.DoapResource;
import org.openrdf.elmo.Entity;
import org.openrdf.elmo.annotations.rdf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.IDoapResourceBehaviour;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * Provides functionality for a DOAPResource object provided by
 * Elmo.
 */

@rdf("http://usefulinc.com/ns/doap#Project")
public class DoapResourceBehaviour extends ResourceBehavior implements IDoapResourceBehaviour {
  private static final Logger logger = LoggerFactory
  .getLogger(DoapResourceBehaviour.class);
    
  public DoapResourceBehaviour() {
  }
  
  /**
   * Create a resource behaviour for a given resource.
   * @param resource
   */
  public DoapResourceBehaviour(DoapResource resource) {
    super(resource);
    logger.debug("Create a DoapResourceBehaviour for an Elmo DoapResource object");
  }
  
  /**
   * Create a resource behaviour for a given Elmo Entity.
   */
  public DoapResourceBehaviour(Entity elmoEntity) {
    super(elmoEntity);
    logger.debug("Create a DoapResourceBehaviour for a Sesame Entity object");
  }

  /**
   * Returns the default name for this resource/
   * 
   * If no names are supplied then the return value of getLabel() is returned.
   */
  public String getName() {
    return (String) getNames().toArray()[0];
  }

  /**
   * Add a name.
   */
  public void addName(String name) {
    Set<Object> names = getDoapResource().getDoapNames();
    names.add(name);
    getDoapResource().setDoapNames(names);
  }

  /**
   * Get all the names for this resource.
   */
  public Set<String> getNames() {
    Set<String> names = Utilities.convertToSetOfStrings(getDoapResource().getDoapNames());
    if (names == null || names.size() == 0) {
      names = new HashSet<String>();
      names.add(this.getLabel());
    }
    return names;
  }

  public String getShortDesc() {
    return getDoapResource().getDoapShortdesc();
  }

  public void setShortDesc(String shortDesc) {
    getDoapResource().setDoapShortdesc(shortDesc);
  }

  public String toString() {
    return getLabel() + " (" + getNames() + ")";
  }

  /**
   * Get a JSON representation of this resource.
   * 
   * @return
   */
  public String toJSON() {
    StringBuffer json = new StringBuffer();
    json.append("{ \"items\": [");
    json.append(toJSONRecord());
    json.append("]}");
    return json.toString();
  }

  /**
   * Get a JSON representation of this resource as a record, i.e. one that is
   * suitable for inserting into a JSON file.
   * 
   * @param json
   */
  public String toJSONRecord() {
    StringBuffer json = new StringBuffer();
    json.append("{");
    json.append(toJSONRecordContent());
    json.append("}");
    return json.toString();
  }

  protected String toJSONRecordContent() {
    StringBuffer json = new StringBuffer();
    json.append("\"id\":\"" + elmoEntity.getQName().getNamespaceURI() + "\",");
    json.append("\"label\":\"" + getLabel() + "\",");
    json.append("\"name\":\"" + getName() + "\",");
    json.append("\"shortdesc\":\"" + getShortDesc() + "\"");
    return json.toString();
  }

  public String getCreated() {
    Set<Object> created = getDoapResource().getDoapCreated();
    if (created == null || created.size() == 0) {
      return "Unknown";
    }
    return (String) created.toArray()[0];
  }

  public String getDescription() {
    return getDoapResource().getDoapDescription();
  }

  @SuppressWarnings("unchecked")
  public Set<String> getLicences() {
    Set<org.openrdf.concepts.rdfs.Resource> licences = (Set) getDoapResource()
        .getDoapLicenses();
    return getResourceURIs(licences);
  }

  protected Set<String> getResourceURIs(
      Set<org.openrdf.concepts.rdfs.Resource> resources) {
    Iterator<org.openrdf.concepts.rdfs.Resource> itr = resources.iterator();
    org.openrdf.concepts.rdfs.Resource resource;
    String ns;
    String local;
    Set<String> result = new HashSet<String>(getDoapResource()
        .getDoapLicenses().size());
    while (itr.hasNext()) {
      resource = (org.openrdf.concepts.rdfs.Resource) itr.next();
      ns = resource.getQName().getNamespaceURI();
      local = resource.getQName().getLocalPart();
      result.add(ns + local);
    }
    return result;
  }

  public void setCreated(String created) throws SimalRepositoryException {
    if (getDoapResource().getDoapCreated() != null) {
      if (getDoapResource().getDoapCreated().size() != 1) {
        throw new SimalRepositoryException(
            "Cannot set*(newValue) on values with more than one existing value, use add*(newValue) instead");
      }
      Set<Object> dates = getDoapResource().getDoapCreated();
      dates.remove(dates.toArray()[0]);
      dates.add(created);
    }
  }

  public void setDescription(String newDesc) {
    getDoapResource().setDoapDescription(newDesc);
  }

  public org.openrdf.concepts.doap.DoapResource getDoapResource() {
    return (org.openrdf.concepts.doap.DoapResource) elmoEntity;
  }

  /**
   * Convert a set of objects returned by Elmo to a set of IDoapResource
   * objects.
   * 
   * @param set
   * @return
   */
  public Set<DoapResourceBehaviour> createDoapResourceBehaviourSet(Set<Object> set) {
    Iterator<Object> elmoResources = set.iterator();
    HashSet<DoapResourceBehaviour> results = new HashSet<DoapResourceBehaviour>(set.size());
    Object resource;
    while (elmoResources.hasNext()) {
      resource = elmoResources.next();
      if (resource instanceof org.openrdf.concepts.doap.DoapResource) {
        results
            .add(new DoapResourceBehaviour(
                (org.openrdf.concepts.doap.DoapResource) resource));
      } else {
        throw new IllegalArgumentException(
            "Can only create ResourceSets from elmo DoapResources. you just tried to create one containing "
                + resource.toString());
      }
    }
    return results;
  }
  


  /**
   * Get the URL for this resource. Attempts to construct a URL
   * for this resource from the QName. This will often work if a resource
   * is defined by rdf:resource. If it is not possible to create an
   * URL for the resource an exception is thrown.
   */
  public URL getURL() throws SimalRepositoryException {
    String ns = elmoEntity.getQName().getNamespaceURI();
    String local = elmoEntity.getQName().getLocalPart();
    try {
      return new URL(ns + local);
    } catch (MalformedURLException e) {
      throw new SimalRepositoryException("Unable to create homepage URL", e);
    }
  }
}