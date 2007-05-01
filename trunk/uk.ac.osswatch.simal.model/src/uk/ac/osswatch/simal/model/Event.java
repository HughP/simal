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

import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 * An event is something that happened, or will happen, at a given
 * point in time, or has a set start and end time.
 *
 */
@Entity
public class Event implements Serializable {
    private static final long serialVersionUID = -6496895896500052023L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false)
    private long id;
    private String name;
    private String shortDesc;
    private Date created;
    private Date startDate;
    private Date endDate;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity=uk.ac.osswatch.simal.model.Project.class)
    private Set<Project> projects = new HashSet<Project>();

    public Event () {
        setCreated(new Date(System.currentTimeMillis()));
    }
    
    /**
     * Minimal constructor.
     * @param name
     * @param shortDesc
     */
    public Event (String name, String shortDesc) {
        setName(name);
        setShortDesc(shortDesc);
        setCreated(new Date(System.currentTimeMillis()));
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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

    public Set<Project> getProjects() {
        return projects;
    }
    public void setProject(Set<Project> projects) {
        this.projects = projects;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public String toString() {
        return name;
    }
    
}
