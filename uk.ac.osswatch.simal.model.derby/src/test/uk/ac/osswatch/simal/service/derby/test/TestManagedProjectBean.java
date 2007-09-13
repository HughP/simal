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
package uk.ac.osswatch.simal.service.derby.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import uk.ac.osswatch.simal.model.Contributor;
import uk.ac.osswatch.simal.model.Event;
import uk.ac.osswatch.simal.model.Project;
import uk.ac.osswatch.simal.service.derby.ManagedProjectBean;

public class TestManagedProjectBean {

    @Test
    public void testCreateNewProject() {
        Contributor contributor = new Contributor("Contributor 1", "cont1@test.com");
        Event event = new Event("Event 1", "The first event", new Date());
        Project project = new Project("Test1", "Test 1", "First Test Project", contributor);
        project.addEvent(event);
        ManagedProjectBean pb = new ManagedProjectBean();
        pb.createNewProject(project);
        
        Project readProject = pb.findProject(project.getId());
        assertNotNull(readProject);
        assert(readProject.getContributors().size() == 1);
        assert(readProject.getEvents().size() == 1);
    }

}
