package uk.ac.osswatch.simal.model;

import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.sql.Date;
import java.util.Collection;
import java.util.Set;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

@Entity
public class Project implements Serializable {
    private static final long serialVersionUID = -6496895896500052023L;

    private static final int NAME = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false)
    private long id;

    private String shortName;

    private String name;

    private String shortDesc;

    private String description;

    private URL url;

    private Date created;

    private URL mailingListURL;

    private URL wikiURL;

    private URL downloadURL;

    private URL issueTrackerURL;
    
    private URL doapURL;

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
        addContributor(contributor);
    }

    public URL getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(URL downloadURL) {
        this.downloadURL = downloadURL;
    }

    public URL getIssueTrackerURL() {
        return issueTrackerURL;
    }

    public void setIssueTrackerURL(URL issueTrackerURL) {
        this.issueTrackerURL = issueTrackerURL;
    }

    public long getId() {
        return id;
    }

    public URL getURL() {
        return url;
    }

    public void setURL(URL url) {
        this.url = url;
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

    public URL getMailingListURL() {
        return mailingListURL;
    }

    public void setMailingListURL(URL mailingListURL) {
        this.mailingListURL = mailingListURL;
    }

    public URL getWikiURL() {
        return wikiURL;
    }

    public void setWikiURL(URL wikiURL) {
        this.wikiURL = wikiURL;
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

            if (getURL() != null) {
                writer.writeStartElement("homepage");
                writer.writeAttribute(rdfURI, "resource", getURL()
                        .toExternalForm());
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
    public URL getDoapURL() {
        return doapURL;
    }

    /**
     * Set the location of the doap file maintained by this project.
     * 
     */
    public void setDoapURL(URL doapURL) {
        this.doapURL = doapURL;
    }
}
