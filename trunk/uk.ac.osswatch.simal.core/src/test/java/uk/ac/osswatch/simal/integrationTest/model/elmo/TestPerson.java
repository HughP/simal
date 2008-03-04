package uk.ac.osswatch.simal.integrationTest.model.elmo;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.elmo.Person;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestPerson extends BaseRepositoryTest {

	private static Set<IPerson> maintainers;
	private static Set<IPerson> developers;
	private static Set<IPerson> documenters;
	private static Set<IPerson> helpers;
	private static Set<IPerson> translators;
	private static Set<IPerson> testers;
	private static Person developer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		createRepository();

		project1 = getSimalTestProject(false);
		maintainers = project1.getMaintainers();
		
		developers = project1.getDevelopers();
		developer = (Person)developers.toArray()[0];
		
		helpers = project1.getHelpers();
		documenters = project1.getHelpers();
		translators = project1.getTranslators();
		testers = project1.getTesters();
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
		assertEquals("developer", developer.getGivennames());
	}
	
	@Test
	public void testHomePage() {
		assertEquals("http://example.org/person/developer", developer.getHomepages().toString());
	}
	
	@Test
	public void testToJSON() {
		String json = developer.toJSON(false);
		assertTrue("JSON does not include person name: JSON = " + json , json.contains("\"label\":\"" + developer.getGivennames() + "\""));
	}
        
        @Test
        public void testKnows() throws SimalRepositoryException {
            Set<Person> knows = developer.getKnows();
            assertNotNull("Should know some people", knows);
            assertEquals("Should know one peson", knows.size(), 1);
            Person person = knows.iterator().next();
            String givenName = person.getGivennames();
            assertTrue("Should know Dan Brickley", givenName.contains("Dan Brickley"));
        }
}
