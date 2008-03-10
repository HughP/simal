package uk.ac.osswatch.simal.integrationTest.model.elmo;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.*;

import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.concepts.foaf.Person;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.elmo.FoafPersonBehaviour;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestPerson extends BaseRepositoryTest {

  private static Set<Person> maintainers;
  private static Set<Person> developers;
  private static Set<Person> documenters;
  private static Set<Person> helpers;
  private static Set<Person> translators;
  private static Set<Person> testers;
  private static Person developer;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    createRepository();

    project1 = getSimalTestProject(false);
    maintainers = project1.getDoapMaintainers();

    developers = project1.getDoapDevelopers();
    developer = (Person)developers.toArray()[0];

    helpers = project1.getDoapHelpers();
    documenters = project1.getDoapHelpers();
    translators = project1.getDoapTranslators();
    testers = project1.getDoapTesters();
  }

  @Test
  public void testPersonsLoaded() {
    assertEquals("Should have one maintainer", 2, maintainers.size());
    assertEquals("Should have one developers", 1, developers.size());
    assertEquals("Should have one helpers", 1, helpers.size());
    assertEquals("Should have one documenters", 1, documenters.size());
    assertEquals("Should have one translators", 1, translators.size());
    assertEquals("Should have one testers", 1, testers.size());
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
    // FIXME: should not need to create behaviour manually
    FoafPersonBehaviour behaviour = new FoafPersonBehaviour(developer);
    String json = behaviour.toJSON(false);
    assertTrue("JSON does not include person name: JSON = " + json, json
        .contains("\"label\":\"" + developer.getFoafGivennames() + "\""));
  }

  @Test
  public void testKnows() throws SimalRepositoryException {
    Set<Person> knows = developer.getFoafKnows();
    assertNotNull("Should know some people", knows);
    Person person = knows.iterator().next();
    String givenName = (String)person.getFoafGivennames().toArray()[0];
    assertTrue("Should know Dan Brickley", givenName.contains("Dan Brickley"));
  }

  @Test
  public void testGetColleagues() throws SimalRepositoryException {
    // FIXME: should not need to create behaviour manually
    FoafPersonBehaviour behaviour = new FoafPersonBehaviour(developer);
    Set<Person> colleagues = behaviour.getColleagues();
    assertNotNull(colleagues);
    assertEquals("Got an incorrect number of colleagues", 6, colleagues.size());
  }
}
