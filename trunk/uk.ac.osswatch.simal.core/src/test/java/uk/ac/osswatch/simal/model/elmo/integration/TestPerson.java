package uk.ac.osswatch.simal.model.elmo.integration;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.osswatch.simal.model.IPerson;

public class TestPerson extends AbstractTestDOAP {

	private static Set<IPerson> maintainers;
	private static Set<IPerson> developers;
	private static Set<IPerson> documenters;
	private static Set<IPerson> helpers;
	private static Set<IPerson> translators;
	private static Set<IPerson> testers;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		project1 = getSimalTestProject(false);
		maintainers = project1.getMaintainers();
		developers = project1.getDevelopers();
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
}
