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
import uk.ac.osswatch.simal.rdf.DuplicateQNameException;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestPerson extends BaseRepositoryTest {

  private static IPerson developer;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    initRepository();

    developer = repository
        .getPerson(new QName("http://foo.org/~developer/#me"));
  }

  @Test
  public void testAddPersonFromScratch() throws SimalRepositoryException,
      DuplicateQNameException {
    QName qname = new QName(SimalRepository.DEFAULT_PERSON_NAMESPACE_URI
        + "TestingPersonFromScratch");
    IPerson person;
    person = repository.createPerson(qname);

    person = repository.getPerson(qname);
    assertNotNull("Person has not been added to repository", person);

    assertNotNull("Person simalID is incorrectly set", person.getSimalId());
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
    String givenName = (String) person.getFoafGivennames().toArray()[0];
    assertTrue("Should know Dan Brickley", givenName.contains("Dan Brickley"));
  }

  @Test
  public void testGetColleagues() throws SimalRepositoryException {
    Set<IPerson> colleagues = developer.getColleagues();
    assertNotNull(colleagues);
    assertEquals("Got an incorrect nmber of colleagues", BaseRepositoryTest
        .getNumberOfParticipants() - 1, colleagues.size());
    Iterator<IPerson> people = colleagues.iterator();
    while (people.hasNext()) {
      IPerson person = people.next();
      assertNotNull("No person should have a null ID (see "
          + person.getQName().toString() + ")", person.getSimalId());
    }
  }

  @Test
  public void testSuppliedSimalId() {
    String id = developer.getSimalId();
    assertEquals("Test developer ID incorrect", "15", id);

  }

  @Test
  public void testGeneratedSimalId() throws SimalRepositoryException {
    String id = developer.getColleagues().iterator().next().getSimalId();
    assertNotNull("Test developer ID incorrect", id);
  }

  @Test
  public void testSeeAlso() {
    Set<Object> seeAlso = developer.getRdfsSeeAlso();
    assertEquals("There should be a single see also value", 1, seeAlso.size());

  }
}
