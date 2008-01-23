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
