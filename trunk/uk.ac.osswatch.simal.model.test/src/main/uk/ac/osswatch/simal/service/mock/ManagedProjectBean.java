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
package uk.ac.osswatch.simal.service.mock;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import uk.ac.osswatch.simal.model.Contributor;
import uk.ac.osswatch.simal.model.Event;
import uk.ac.osswatch.simal.model.Project;
import uk.ac.osswatch.simal.service.IProjectService;

public class ManagedProjectBean implements IProjectService {
    Set<Project> projects;
        
    public void init() {
        ManagedContributorBean contributorDAO = new ManagedContributorBean();
        Collection<Contributor> contributors = contributorDAO.findAll();
        ManagedEvenBean eventDAO = new ManagedEvenBean();
        Set<Event> events = eventDAO.findAll();
        
        projects = new HashSet<Project>();
        
        Project project = new Project(
                "p1",
                "Project 1",
                "This is the first test project",
                (Contributor)contributors.toArray()[0]);
        try {
            project.setURL(new URL("http://www.google.com"));
        } catch (MalformedURLException e) {
            // Shouldn't be thrown as we hard coded the URL
            e.printStackTrace();
        }
        project.addContributor((Contributor)contributors.toArray()[1]);
        project.addEvent((Event)events.toArray()[0]);
        projects.add(project);
        
        project = new Project(
                "p2",
                "Project 2",
                "This is the second test project",
                (Contributor)contributors.toArray()[1]);
        project.addEvent((Event)events.toArray()[0]);
        project.addEvent((Event)events.toArray()[1]);
        projects.add(project);
        
        project = new Project(
                "p3",
                "Project 3",
                "This is the third test project",
                (Contributor)contributors.toArray()[2]);
        projects.add(project);        
    }

    public boolean delete(Project project) {
        // TODO Auto-generated method stub
        return false;
    }

    public Project findProject(long id) {
        // TODO Auto-generated method stub
        return null;
    }

    public int save(Project project) {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean update(Project project) {
        // TODO Auto-generated method stub
        return false;
    }
    
    public Set<Project> findAll() {
        if (projects == null) init();
        return projects;
    }

    public void createNewProject(Project project) {
        // TODO Auto-generated method stub
        
    }

    public Project findProject(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    public Project findProjectByShortName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

}
