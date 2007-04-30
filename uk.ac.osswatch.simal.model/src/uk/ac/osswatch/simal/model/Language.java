package uk.ac.osswatch.simal.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Language implements Serializable {
    private static final long serialVersionUID = 3194946344691165619L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false)
    private long id;
    private String name;
    private Date created;
    
    public Language() {
        setCreated(new Date(System.currentTimeMillis()));
    }
    
    /**
     * Minimal constructor.
     *
     */
    public Language(String name) {
        this();
        setName(name);
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
