/*
* Copyright 2007 Oxford University
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

import org.junit.Test;

import uk.ac.osswatch.simal.model.Contributor;
import uk.ac.osswatch.simal.model.Project;
import uk.ac.osswatch.simal.service.derby.ManagedContributorBean;

public class TestManagedContributorBean {

    @Test
    public void testDelete() {
        fail("Not yet implemented");
    }

    @Test
    public void testFindProject() {
        fail("Not yet implemented");
    }

    @Test
    public void testSave() {
        Contributor contributor = createContributor();
        ManagedContributorBean cb = new ManagedContributorBean();
        Contributor readContributor = cb.findContributor(contributor.getId());
        assertNotNull(readContributor);
        
        contributor.setName("Edited Conributor");
        cb.save(contributor);
        readContributor = cb.findContributor(contributor.getId());
        assertEquals(readContributor.getName(), "Edited Conributor");
    }

    @Test
    public void testUpdate() {
        fail("Not yet implemented");
    }

    @Test
    public void testFindAll() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreateNewContributor() {
        Contributor contributor = createContributor();
        ManagedContributorBean cb = new ManagedContributorBean();
        Contributor readContributor = cb.findContributor(contributor.getId());
        assertNotNull(readContributor);
        assert(readContributor.getProjects().size() == 1);
    }
    
    private Contributor createContributor() {
        Contributor contributor = new Contributor("Contributor 1", "cont1@test.com");
        Project project = new Project("Test1", "Test 1", "First Test Project", contributor);
        ManagedContributorBean cb = new ManagedContributorBean();
        cb.save(contributor);
        return contributor;
    }

}
