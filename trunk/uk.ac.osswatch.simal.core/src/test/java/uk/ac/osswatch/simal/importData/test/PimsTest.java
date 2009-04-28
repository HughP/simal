package uk.ac.osswatch.simal.importData.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import  uk.ac.osswatch.simal.importData.Pims;
import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryFactory;

public class PimsTest extends BaseRepositoryTest {
	
	@Test
	public void testDumpData() throws FileNotFoundException, IOException, SimalRepositoryException, DuplicateURIException {
		Pims pims = new Pims();
		ISimalRepository repo = SimalRepositoryFactory.getInstance();
		Iterator<IProject> projects = repo.getAllProjects().iterator();
		
		boolean projectAIsValid = false;
		while (projects.hasNext()) {
			IProject project = projects.next();
			String name = project.getName();
			Set<IDoapHomepage> homepages = project.getHomepages();
			if (name.equals("Project A")) {
				if (!homepages.toString().contains("Homepage")) {
					break;
				}
				projectAIsValid = true;
			}
		}
		assertTrue("Project A is not correctly configured", projectAIsValid);
	}

}
