package uk.ac.osswatch.simal.model.elmo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;

import uk.ac.osswatch.simal.model.IDoapResource;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * This is a wrapper around an Elmo DoapResource class. It should not be
 * instatiated directly instances will be created by elmo as required when a
 * concept extends DOAPResource. Wrappers for these more granular classes should
 * be created that extend this class, when such wrappers are instantiated they
 * should set the value of getDoapResource() to to wrapped elmo object.
 * 
 * @see org.openrdf.concepts.doap.DoapResource
 */
public class DoapResource extends Resource implements IDoapResource {

  private static final long serialVersionUID = -7610178891247360114L;

  protected DoapResource() {
  };

  public DoapResource(org.openrdf.concepts.doap.DoapResource resource,
      SimalRepository repository) {
    super(resource, repository);
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
    Set<String> names = convertToSetOfStrings(getDoapResource().getDoapNames());
    if (names == null || names.size() == 0) {
      names = new HashSet<String>();
      names.add(this.getLabel(true));
    }
    return names;
  }

  public String getShortDesc() {
    return getDoapResource().getDoapShortdesc();
  }

  public void setShortDesc(String shortDesc) {
    getDoapResource().setDoapShortdesc(shortDesc);
  }

  public QName getQName() {
    return getDoapResource().getQName();
  }

  public String toString() {
    return getLabel(true) + " (" + getNames() + ")";
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
    json.append("\"id\":\"" + getQName().getNamespaceURI() + "\",");
    json.append("\"label\":\"" + getLabel(true) + "\",");
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

  protected Set<String> convertToSetOfStrings(Set<Object> sourceSet) {
    Set<String> result = new HashSet<String>(sourceSet.size());
    Iterator<Object> itr = sourceSet.iterator();
    while (itr.hasNext()) {
      result.add(itr.next().toString());
    }
    return result;
  }

  public org.openrdf.concepts.doap.DoapResource getDoapResource() {
    return (org.openrdf.concepts.doap.DoapResource) elmoResource;
  }

  /**
   * Convert a set of objects returned by Elmo to a set of IDoapResource
   * objects.
   * 
   * @param set
   * @return
   */
  public Set<IDoapResource> createResourceSet(Set<Object> set) {
    Iterator<Object> elmoResources = set.iterator();
    HashSet<IDoapResource> results = new HashSet<IDoapResource>(set.size());
    Object resource;
    while (elmoResources.hasNext()) {
      resource = elmoResources.next();
      if (resource instanceof org.openrdf.concepts.doap.DoapResource) {
        results
            .add(new DoapResource(
                (org.openrdf.concepts.doap.DoapResource) resource,
                getRepository()));
      } else {
        throw new IllegalArgumentException(
            "Can only create ResourceSets from elmo DoapResources. you just tried to create one containing "
                + resource.toString());
      }
    }
    return results;
  }
}