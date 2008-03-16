package uk.ac.osswatch.simal.integrationTest.model.elmo;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestPerson extends BaseRepositoryTest {

  private static IPerson developer;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    createRepository();

    developer = repository.getPerson(new QName("http://foo.org/~developer/#me"));
  }

  @Test
  public void testNames() {
    assertEquals("developer", developer.getFoafGivennames().toString());
  }

  @Test
  public void testHomePage() {
    assertEquals("http://example.org/person/developer", developer
        .getFoafHomepages().toString());
  }

  @Test
  public void testToJSON() {
    String json = developer.toJSON(false);
    assertTrue("JSON does not include person name: JSON = " + json, json
        .contains("\"label\":\"" + developer.getLabel() + "\""));
  }

  @Test
  public void testKnows() throws SimalRepositoryException {
    Set<IPerson> knows = developer.getKnows();
    assertNotNull("Should know some people", knows);
    IPerson person = knows.iterator().next();
    String givenName = (String)person.getFoafGivennames().toArray()[0];
    assertTrue("Should know Dan Brickley", givenName.contains("Dan Brickley"));
  }

  @Test
  public void testGetColleagues() throws SimalRepositoryException {
    Set<IPerson> colleagues = developer.getColleagues();
    assertNotNull(colleagues);
    assertEquals("Got an incorrect number of colleagues", 5, colleagues.size());
    Iterator<IPerson> people = colleagues.iterator();
    while (people.hasNext()) {
      IPerson person = people.next();
      assertNotNull("No person should have a null ID (see " + person.getQName().toString() + ")", person.getSimalId());
    }
  }
  
  @Test
  public void testSimalId() {
    String id = developer.getSimalId();
    assertEquals("Test developer ID incorrect", "1", id);
    // FIXME: write tests to ensure IDs are being generated
    
  }
  
  @Test
  public void testSeeAlso() {
    Set<Object> seeAlso= developer.getRdfsSeeAlso();
    assertEquals("There should be a single see also value", 1, seeAlso.size());
    
  }
}
