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
package uk.ac.osswatch.simal.model;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.sql.Date;
import java.util.Collection;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

@Entity
public class Project extends DefaultHandler implements Serializable {
	private static final long serialVersionUID = -6496895896500052023L;
	private static Logger logger = Logger.getLogger("uk.ac.osswatch.simal.model.Project");
	
	private static final int NAME = 0;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(updatable = false)
	private long id;

	private String shortName = "undefined";

	private String name;

	private String shortDesc;

	private String description;

	private String homepageURL;

	private Date created;

	private String mailingListURL;

	private String wikiURL;

	private String downloadURL;

	private String issueTrackerURL;

	private String doapURL;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Collection<Language> languages = new Vector<Language>();

	@Transient
	private Collection<String> operatingSystems = new Vector<String>();

	@Transient
	private Collection<String> licences = new Vector<String>();

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "projects")
	private Collection<Contributor> contributors = new Vector<Contributor>();

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "projects")
	private Collection<Event> events = new Vector<Event>();

	public Project() {
		setCreated(new Date(System.currentTimeMillis()));
	}

	/**
	 * A minimal constructor.
	 * 
	 * @param contributor
	 */
	public Project(String shortName, String name, String shortDesc,
			Contributor contributor) {
		this();
		setShortName(shortName);
		setName(name);
		setShortDesc(shortDesc);
		setDescription(shortDesc);
		addContributor(contributor);
	}

	public Project(URL url) throws ParserConfigurationException, SAXException, IOException {
    	String doapNS = "http://usefulinc.com/ns/doap#";
    	String rdfNS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(url.openStream());
		Node root;
		NodeList elements;
		
		if (document.getDocumentElement().getLocalName().equals("RDF") && document.getDocumentElement().getNamespaceURI().equals(rdfNS)) {
			elements = document.getDocumentElement().getElementsByTagNameNS(doapNS, "Project");
			root = elements.item(0);
		} else {
			root = document.getDocumentElement();
		}
				
		if (root.getLocalName().equals("Project") && root.getNamespaceURI().equals(doapNS)) {
			for (Node childNode = root.getFirstChild(); childNode != null; childNode = childNode.getNextSibling()) {
		      if (childNode.getNamespaceURI() == doapNS) {
		        if (childNode.getLocalName() == "name") {
					setName(childNode.getTextContent());
		        } else if (childNode.getLocalName() == "shortname") {
					setShortName(childNode.getTextContent());
		        } else if (childNode.getLocalName() == "shortdesc") {
					setShortDesc(childNode.getTextContent());
		        } else if (childNode.getLocalName() == "description") {
					setDescription(childNode.getTextContent());
		        } else if (childNode.getLocalName() == "homepage") {
					setHomepageURL(new URL(childNode.getTextContent()));
		        } else {
		            logger.warning("Not handling DOAP file elements with the local name " + childNode.getLocalName() + " in the namespace " + childNode.getNamespaceURI());
		        }
		      } else if (childNode.getNodeType() == Node.ELEMENT_NODE) {
		    	  logger.warning("Not handling elements in DOAP files with the namesspace " + childNode.getNamespaceURI() + " e.g. " + childNode.getLocalName());
		      }
		    }
		} else {
			throw new SAXException("Unrecognised XML document schema at " + url.toString());
		}
	}

	public String getDownloadURL() {
		return downloadURL;
	}

	public void setDownloadURL(URL downloadURL) {
		this.downloadURL = downloadURL.toExternalForm();
	}

	public String getIssueTrackerURL() {
		return issueTrackerURL;
	}

	public void setIssueTrackerURL(URL issueTrackerURL) {
		this.issueTrackerURL = issueTrackerURL.toExternalForm();
	}

	public long getId() {
		return id;
	}

	public String getHomepageURL() {
		return homepageURL;
	}

	public void setHomepageURL(URL url) {
		this.homepageURL = url.toExternalForm();
	}

	/**
	 * Get the name of the project.
	 * 
	 * @return the project name or null if the name is an empty string
	 */
	public String getName() {
		if (name == null || name.length() == 0) {
			return null;
		}
		return name;
	}

	public void setName(String name) {
		if (!name.equals(this.name)) {
			this.name = name;
			notifyChange(NAME);
		}
	}

	/**
	 * Notify all listeners of a change in one of the fields of this object.
	 * 
	 * @param field
	 *            identifier of the field that has changed
	 */
	private void notifyChange(int field) {
		// TODO Auto-generated method stub

	}

	public Date getCreated() {
		return created;
	}

	protected void setCreated(Date created) {
		this.created = created;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public String toString() {
		if (getName() != null) {
			return name;
		} else {
			return "UNDEFINED";
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMailingListURL() {
		return mailingListURL;
	}

	public void setMailingListURL(URL mailingListURL) {
		this.mailingListURL = mailingListURL.toExternalForm();
	}

	public String getWikiURL() {
		return wikiURL;
	}

	public void setWikiURL(URL wikiURL) {
		this.wikiURL = wikiURL.toExternalForm();
	}

	public Collection<Language> getLanguages() {
		return languages;
	}

	public void setLanguage(Collection<Language> languages) {
		this.languages = languages;
	}

	public Collection<String> getOperatingSystems() {
		return operatingSystems;
	}

	public void setOperatingSystems(Collection<String> operatingSystems) {
		this.operatingSystems = operatingSystems;
	}

	public Collection<String> getLicence() {
		return licences;
	}

	public void setLicence(Collection<String> licences) {
		this.licences = licences;
	}

	public Collection<Contributor> getContributors() {
		return contributors;
	}

	public void setContributors(Collection<Contributor> contributors) {
		this.contributors = contributors;
	}

	public void addContributor(Contributor contributor) {
		this.contributors.add(contributor);
	}

	public Collection<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}

	public void addEvent(Event event) {
		this.events.add(event);
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public void writeDOAP(OutputStream outputStream) {
		String rdfURI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		String rdfsURI = "http://www.w3.org/2000/01/rdf-schema#";
		String foafURI = "http://xmlns.com/foaf/0.1/";

		try {
			XMLStreamWriter writer = XMLOutputFactory.newInstance()
					.createXMLStreamWriter(outputStream);
			writer.writeStartDocument();

			writer.writeStartElement("Project");
			writer.setPrefix("rdf", rdfURI);
			writer.setPrefix("rdfs", rdfsURI);
			writer.setPrefix("foaf", foafURI);
			writer.setDefaultNamespace("http://usefulinc.com/ns/doap#");

			writer.writeStartElement("name");
			writer.writeCharacters(getName());
			writer.writeEndElement();

			if (getHomepageURL() != null) {
				writer.writeStartElement("homepage");
				writer.writeAttribute(rdfURI, "resource", getHomepageURL());
				writer.writeEndElement();
			}

			writer.writeStartElement("created");
			writer.writeCharacters(getCreated().toString());
			writer.writeEndElement();

			writer.writeStartElement("shortDesc");
			writer.writeCharacters(getShortDesc());
			writer.writeEndElement();

			if (getDescription() != null) {
				writer.writeStartElement("description");
				writer.writeCharacters(getDescription());
				writer.writeEndElement();
			}

			writer.writeEndElement();

			writer.writeEndDocument();
			writer.flush();
			writer.close();
		} catch (XMLStreamException e) {
			// TODO: handle exception properly
			e.printStackTrace();
		}
	}

	/**
	 * Get the location of the doap file maintained by this project.
	 * 
	 * @return
	 */
	public String getDoapURL() {
		return doapURL;
	}

	/**
	 * Set the location of the doap file maintained by this project.
	 * 
	 */
	public void setDoapURL(URL doapURL) {
		this.doapURL = doapURL.toExternalForm();
	}

}
