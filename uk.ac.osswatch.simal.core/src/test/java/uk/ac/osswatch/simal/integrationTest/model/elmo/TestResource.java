package uk.ac.osswatch.simal.integrationTest.model.elmo;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestResource extends BaseRepositoryTest {

  public TestResource() throws SimalRepositoryException {
    super();
  }

  @Test
  public void testDeleteEntity() throws SimalRepositoryException {
    assertNotNull(project1);
    project1.delete();
    IProject project = getSimalTestProject();
    assertNull("The simal project should have been deleted", project);
    // force the repo to be rebuilt
    repository.destroy();
    initRepository();
  }

}
