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
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;

import uk.ac.osswatch.simal.model.Doap;
import uk.ac.osswatch.simal.model.IDoapLicence;
import uk.ac.osswatch.simal.model.IDoapResource;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDFS;

public class DoapResource extends Resource implements IDoapResource {
  private static final long serialVersionUID = 1L;

  public DoapResource(com.hp.hpl.jena.rdf.model.Resource resource) {
    super(resource);
  }

  public void addName(String name) {
    getJenaResource().addLiteral(Doap.NAME, name);
  }

  public String getCreated() {
    String created = getLiteralValue(Doap.CREATED);
    if (created != null) {
      return created;
    } else {
      return "";
    }
  }

  public String getDescription() {
    String desc = getLiteralValue(Doap.DESCRIPTION);
    
    if (desc == null) {
      desc = getShortDesc();
    }
    
    return desc;
  }

  public Set<IDoapLicence> getLicences() {
    Iterator<Statement> props = listProperties(Doap.LICENSE).iterator();
    Set<IDoapLicence> results = new HashSet<IDoapLicence>();
    while (props.hasNext()) {
      results.add(new Licence(props.next().getResource()));
    }
    return results;
  }

  public String getName() {
    String name = getLiteralValue(Doap.NAME);
    
    if (name == null) {
      name = getLiteralValue(RDFS.label);
    }
    
    if (name == null) {
      return getURI();
    }
    return name;
  }

  @Override
  public String getLabel(String defaultLabel) {
    String name = this.getName();
    if (name == null) {
    	return super.getLabel(defaultLabel);
    } else {
      return name;
    }
  }

@Override
  public String getLabel() {
    String name = this.getName();
    if (name == null) {
      return super.getLabel();
    } else {
      return name;
    }
  }

  public Set<String> getNames() {
    Iterator<Statement> itr = listProperties(Doap.NAME).iterator();
    Set<String> results = new HashSet<String>();
    while (itr.hasNext()) {
      results.add(itr.next().getString().trim());
    }
    return results;
  }

  public String getShortDesc() {
    String desc = getLiteralValue(Doap.SHORTDESC);
    if (desc == null) {
      desc = getLiteralValue(Doap.DESCRIPTION);
      if (desc != null && 0 < desc.length() && desc.length() > 200) {
        desc = desc.substring(0, 199);
      }
      if (desc == null) {
        desc = "No description available";
      }
    }
    return desc;
  }

  public void setCreated(String newCreated) throws SimalRepositoryException {
    getJenaResource().removeAll(Doap.CREATED);
    getJenaResource().addLiteral(Doap.CREATED, newCreated);
  }

  public void setDescription(String newDescription) {
    getJenaResource().removeAll(Doap.DESCRIPTION);
    getJenaResource().addLiteral(Doap.DESCRIPTION, newDescription);
  }

  public void setShortDesc(String shortDesc) {
    getJenaResource().removeAll(Doap.SHORTDESC);
    getJenaResource().addLiteral(Doap.SHORTDESC, shortDesc);
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
    json.append("\"label\":\""
        + StringEscapeUtils.escapeJavaScript(getLabel().trim()) + "\",");
    json.append("\"name\":\""
        + StringEscapeUtils.escapeJavaScript(getName().trim()) + "\",");
    String desc = getShortDesc();
    if (desc == null || desc.equals("")) {
      desc = getDescription();
      if (desc != null && desc.length() > 128) {
        desc = desc.substring(128);
      }
    }
    if (desc == null) {
      desc = "No description available";
    }
    json.append("\"shortdesc\":\""
        + StringEscapeUtils.escapeJavaScript(desc.trim()) + "\"");
    return json.toString();
  }

	public void removeName(String name) throws SimalRepositoryException {
	    Model model = getJenaResource().getModel();
	    StmtIterator statements = model.listStatements(getJenaResource(), Doap.NAME, name);
	    if (statements == null) {
	    	throw new SimalRepositoryException("Name does not exist in resource, cannot remove: " + name);
	    }
	    model.remove(statements);
	}

}
