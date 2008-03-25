package uk.ac.osswatch.simal.integrationTest.model.elmo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;

import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestDOAPProjectBehaviour extends BaseRepositoryTest {

  @Test
  public void testGetAllPeople() throws SimalRepositoryException {
    initialiseRepository(false);

    HashSet<IPerson> people = project1.getAllPeople();
    assertNotNull(people);
    assertEquals("Got the wrong number of people", BaseRepositoryTest.getNumberOfParticipants(), people.size());
  }

}
