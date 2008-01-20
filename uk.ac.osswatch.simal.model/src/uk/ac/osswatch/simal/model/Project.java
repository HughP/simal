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
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Date;
import java.util.Collection;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLContext;
import org.xml.sax.helpers.DefaultHandler;

@Entity
public class Project extends DefaultHandler implements Serializable {
	private static final long serialVersionUID = -6496895896500052023L;
	private static Logger logger = Logger
			.getLogger("uk.ac.osswatch.simal.model.Project");

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

	/**
	 * Read an XML document reading a project. The XML document should be a DOAP
	 * document.
	 * 
	 * @param url
	 *            location of the XML document to read
	 * @return
	 * @throws MappingException 
	 * @throws IOException 
	 * @throws ValidationException 
	 * @throws MarshalException 
	 */
	public static Project readProject(URL url) throws IOException, MappingException, MarshalException, ValidationException {
		Project project = new Project();
		Mapping mapping = XMLContext.createMapping();
		mapping.loadMapping(project.getClass().getResource("xmlMapping.xml"));
		XMLContext context = new XMLContext();
		context.addMapping(mapping);

		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setClass(Project.class);
		unmarshaller.setIgnoreExtraElements(true);

		project = (Project) unmarshaller.unmarshal(new InputStreamReader(url
				.openStream()));

		return project;
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

	public String toXML() {
		StringWriter sw = new StringWriter();
		XMLContext context = new XMLContext();
		Mapping mapping = XMLContext.createMapping();

		try {
			mapping.loadMapping(this.getClass().getResource("xmlMapping.xml"));
			context.addMapping(mapping);

			Marshaller marshaller = context.createMarshaller();
			marshaller.setWriter(sw);
			marshaller.marshal(this);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Unable to unmarshal Project", e);
		} catch (MarshalException e) {
			logger.log(Level.SEVERE, "Unable to unmarshal Project", e);
		} catch (ValidationException e) {
			logger.log(Level.SEVERE, "Unable to unmarshal Project", e);
		} catch (MappingException e) {
			logger.log(Level.SEVERE, "Unable to unmarshal Project", e);
		}

		return sw.toString();
	}

}
