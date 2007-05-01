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
package uk.ac.osswatch.simal.model.test;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import uk.ac.osswatch.simal.model.Contributor;
import uk.ac.osswatch.simal.model.Event;
import uk.ac.osswatch.simal.model.Project;
import uk.ac.osswatch.simal.service.derby.ManagedProjectBean;

public class TestProject {

    @Test
    public void testProject() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetDownloadURL() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetDownloadURL() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetIssueTrackerURL() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetIssueTrackerURL() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetId() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetId() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetUrl() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetUrl() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetName() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetName() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetCreated() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetCreated() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetShortDesc() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetShortDesc() {
        fail("Not yet implemented");
    }

    @Test
    public void testToString() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetDescription() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetDescription() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetMailingListURL() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetMailingListURL() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetWikiURL() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetWikiURL() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetLanguage() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetLanguage() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetOperatingSystem() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetOperatingSystem() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetLicence() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetLicence() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetContributors() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetContributors() {
        fail("Not yet implemented");
    }

    @Test
    public void testAddContributor() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetEvents() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetEvents() {
        fail("Not yet implemented");
    }

    @Test
    public void testAddEvent() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testWriteDOAP() {
        Project project = createProject();
        project.writeDOAP(System.out);
    }
    
    private Project createProject() {
        Contributor contributor = new Contributor("Contributor 1", "cont1@test.com");
        Event event = new Event("Event 1", "The first event");
        Project project = new Project("Test1", "Test 1", "First Test Project", contributor);
        project.addEvent(event);
        try {
            project.setURL(new URL("http://www.test.com"));
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return project;
    }

}
