package uk.ac.osswatch.simal.service.derby.test;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.osswatch.simal.model.Contributor;
import uk.ac.osswatch.simal.model.Event;
import uk.ac.osswatch.simal.model.Project;
import uk.ac.osswatch.simal.service.derby.ManagedProjectBean;

public class TestManagedProjectBean {

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
        fail("Not yet implemented");
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
    public void testCreateNewProject() {
        Contributor contributor = new Contributor("Contributor 1", "cont1@test.com");
        Event event = new Event("Event 1", "The first event");
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
