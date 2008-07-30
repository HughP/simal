package uk.ac.osswatch.simal.model.jena;
/*
 * 
Copyright 2007 University of Oxford * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */


import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;

import uk.ac.osswatch.simal.model.Doap;
import uk.ac.osswatch.simal.model.IDoapLicence;
import uk.ac.osswatch.simal.model.IDoapResource;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDFS;

public class DoapResource extends Resource implements IDoapResource {

  public DoapResource(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
  }

  public void addName(String name) {
    jenaResource.addLiteral(Doap.NAME, name);
  }

  public String getCreated() {
    Statement created = jenaResource.getProperty(Doap.CREATED);
    if (created != null) {
      return created.getString();
    } else {
      return "";
    }
  }

  public String getDescription() {
    Statement resource = jenaResource.getProperty(Doap.DESCRIPTION);
    String desc;
    if (resource == null) {
      desc = getShortDesc();
    } else {
      desc = resource.getString().trim(); 
    }
    return desc;
  }

  public Set<IDoapLicence> getLicences() {
    StmtIterator itr = jenaResource.listProperties(Doap.LICENSE);
    Set<IDoapLicence> results = new HashSet<IDoapLicence>();
    while (itr.hasNext()) {
      results.add(new Licence(itr.nextStatement().getResource()));
    }
    return results;
  }

  public String getName() {
    Statement name = jenaResource.getProperty(Doap.NAME);
    if (name != null) {
      return name.getString();
    } else {
      name = jenaResource.getProperty(RDFS.label);
      if (name != null) {
        return name.getString();
      } else {
        return getURI();
      }
    }
  }

  public Set<String> getNames() {
    StmtIterator itr = jenaResource.listProperties(Doap.NAME);
    Set<String> results = new HashSet<String>();
    while (itr.hasNext()) {
      results.add(itr.nextStatement().getString().trim());
    }
    return results;
  }

  public String getShortDesc() {
    Statement desc = jenaResource.getProperty(Doap.SHORTDESC);
    if (desc != null) {
      return desc.getString().trim();
    } else {
      return null;
    }
  }

  public void setCreated(String newCreated) throws SimalRepositoryException {
    jenaResource.removeAll(Doap.CREATED);
    jenaResource.addLiteral(Doap.CREATED, newCreated);
  }

  public void setDescription(String newDescription) {
    jenaResource.removeAll(Doap.DESCRIPTION);
    jenaResource.addLiteral(Doap.DESCRIPTION, newDescription);
  }

  public void setShortDesc(String shortDesc) {
    jenaResource.removeAll(Doap.SHORTDESC);
    jenaResource.addLiteral(Doap.SHORTDESC, shortDesc);
  }

  public String toString() {
    return getLabel() + " (" + getNames() + ")";
  }

  public String toJSON() throws SimalRepositoryException {
    StringBuffer json = new StringBuffer();
    json.append("{ \"items\": [");
    json.append(toJSONRecord());
    json.append("]}");
    return json.toString();
  }

  public String toJSONRecord() throws SimalRepositoryException {
    StringBuffer json = new StringBuffer();
    json.append("{");
    json.append(toJSONRecordContent());
    json.append("}");
    return json.toString();
  }

  protected String toJSONRecordContent() throws SimalRepositoryException {
    StringBuffer json = new StringBuffer();
    json.append("\"id\":\"" + getURI() + "\",");
    json.append("\"label\":\"" + getLabel() + "\",");
    json.append("\"name\":\"" + getName() + "\",");
    json.append("\"shortdesc\":\"" + StringEscapeUtils.escapeJavaScript(getShortDesc()) + "\"");
    return json.toString();
  }

}
