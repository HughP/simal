package uk.ac.osswatch.simal.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;

@Entity
public class Contributor implements Serializable {
    private static final long serialVersionUID = -8215136669662516435L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false)
    private long id;
    private String name;
    private String email;
    private String url;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity=uk.ac.osswatch.simal.model.Project.class)
    private Collection<Project> projects = new HashSet<Project>();
    private Date created;
    
    public Contributor() {
        setCreated(new Date(System.currentTimeMillis()));
    }
    
    /**
     * Minimal constructor.
     *
     */
    public Contributor(String name, String email) {
        this();
        setName(name);
        setEmail(email);
    }
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        if (name == null || name.length() == 0 ) {
            return null;
        }
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    
    public Collection<Project> getProjects() {
        return projects;
    }
    public void setProjects(Collection<Project> projects) {
        this.projects = projects;
    }
    
    public String toString() {
        if (getName() != null) {
            return name;
        } else {
            return "UNDEFINED";
        }
    }

    public Date getCreated() {
        return created;
    }
    protected void setCreated(Date created) {
        this.created = created;
    }
}
