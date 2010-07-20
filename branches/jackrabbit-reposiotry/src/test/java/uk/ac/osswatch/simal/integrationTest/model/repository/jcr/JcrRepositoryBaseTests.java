package uk.ac.osswatch.simal.integrationTest.model.repository.jcr;
/*
 * Copyright 2007 University of Oxford 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IDoapBugDatabase;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IDoapMailingList;
import uk.ac.osswatch.simal.model.IDoapRelease;
import uk.ac.osswatch.simal.model.IDoapRepository;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.jcr.JcrSimalRepository;
import uk.ac.osswatch.simal.model.jcr.Project;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.IBugDatabaseService;
import uk.ac.osswatch.simal.service.IHomepageService;
import uk.ac.osswatch.simal.service.IMailingListService;
import uk.ac.osswatch.simal.service.IPersonService;
import uk.ac.osswatch.simal.service.IProjectService;
import uk.ac.osswatch.simal.service.IReleaseService;
import uk.ac.osswatch.simal.service.IRepositoryService;

/**
 * A set of tests that ensure the JCR repository is working in isolation of test
 * data. That is it tests simple CRUD operations on the repository.
 * 
 */
public class JcrRepositoryBaseTests {
	public static final Logger logger = LoggerFactory
			.getLogger(JcrRepositoryBaseTests.class);
	static ISimalRepository repo;
	private static ObjectContentManager ocm;

	public static final String DETAILED_PROJECT_LABEL = "Detailed Project";
	private static final String MAINTAINER_URI = "http://foo.org/maintainer";
	private static final String RELEASE_ONE_URI = "http://foo.org/releaseTwo";
	private static final String RELEASE_TWO_URI = "http://foo.org/releaseOne";
	public static String detailedProjectURI = "http://foo.org/detailedTestProject";
	public static String skeletonProjectURI = "http://foo.org/skeletonTestProject";
	public static String rcsURI = "http://foo.org/testRCS";
	public static String homepageURI = "http://foo.org";
	public static String issuesURI = "http://foo.org/issues";
	public static String LIST_URI = "http://foo.org/list";
	
	@BeforeClass
	public static void initialise() throws SimalRepositoryException,
			RepositoryException, DuplicateURIException {
		repo = SimalRepositoryFactory.getInstance(SimalRepositoryFactory.JCR);
		repo.initialise(null);

		ocm = ((JcrSimalRepository) repo).getObjectContentManager();
		assertTrue("Unable to persist projects", ocm
				.isPersistent(Project.class));

		Session session = ocm.getSession();
		Node root = session.getRootNode();
		assertNotNull("Repository root node is null", root);

		session.getRootNode().addNode("test");
		root.save();
		assertTrue("test node doesn't exist", session.itemExists("/test"));
		Node test = (Node) session.getItem("/test");
		test.remove();
		session.save();

		assertFalse("test node exists", session.itemExists("/test"));
		
		createDetailedProject();
		createSkeletonProject();
		
		//session.ex
		//logger.debug(msg)
	}

	private static void createSkeletonProject()
			throws SimalRepositoryException, DuplicateURIException {
		IProjectService projectService = SimalRepositoryFactory.getProjectService();
		IProject project = projectService.createProject(skeletonProjectURI);
		SimalRepositoryFactory.getProjectService().save(project);
		assertNotNull("Created project is a null object", project);
	}

	private static void createDetailedProject()
			throws SimalRepositoryException, DuplicateURIException {
		IProjectService projectService = SimalRepositoryFactory.getProjectService();
		IProject project = projectService.createProject(detailedProjectURI);
		
		addRepository(project);
		addHomePage(project);
		addIssueTrackers(project);
		addLabel(project);
		addMailingLists(project);
		addMaintainers(project);
		addReleases(project);
		
		SimalRepositoryFactory.getProjectService().save(project);
		assertNotNull("Created project is a null object", project);
		assertEquals("Project URI is not correct", detailedProjectURI, project.getURI());
	}

	private static void addLabel(IProject project) {
		project.setLabel(DETAILED_PROJECT_LABEL);
	}

	private static void addRepository(IProject project)
			throws SimalRepositoryException, DuplicateURIException {
		IRepositoryService rcsService = SimalRepositoryFactory.getRepositoryService();
		IDoapRepository rcs = rcsService.create(rcsURI);
		assertNotNull(rcs);
		project.addRepository(rcs);
		assertEquals("Don't seem to have added the repository", 1, project.getRepositories().size());
	}

	private static void addHomePage(IProject project)
			throws SimalRepositoryException, DuplicateURIException {
		IHomepageService homepageService = SimalRepositoryFactory.getHomepageService();
		IDoapHomepage homepage = homepageService.create(homepageURI);
		assertNotNull(homepage);
		project.addHomepage(homepage);
		assertEquals("Don't seem to have added the homepage", 1, project.getHomepages().size());
	}

	private static void addIssueTrackers(IProject project)
			throws SimalRepositoryException, DuplicateURIException {
		IBugDatabaseService service = SimalRepositoryFactory.getBugDatabaseService();
		IDoapBugDatabase tracker = service.create(issuesURI);
		assertNotNull(tracker);
		project.addIssueTracker(tracker);
		assertEquals("Don't seem to have added the issue tracker", 1, project.getIssueTrackers().size());
	}

	private static void addMailingLists(IProject project)
			throws SimalRepositoryException, DuplicateURIException {
		IMailingListService service = SimalRepositoryFactory.getMailingListService();
		IDoapMailingList list = service.create(LIST_URI);
		assertNotNull(list);
		project.addMailingList(list);
		assertEquals("Don't seem to have added the mailing list", 1, project.getMailingLists().size());
	}

	private static void addMaintainers(IProject project)
			throws SimalRepositoryException, DuplicateURIException {
		IPersonService service = SimalRepositoryFactory.getPersonService();
		IPerson person = service.create(MAINTAINER_URI);
		assertNotNull(person);
		project.addMaintainer(person);
		assertEquals("Don't seem to have added the maintainer", 1, project.getMaintainers().size());
	}

	private static void addReleases(IProject project)
			throws SimalRepositoryException, DuplicateURIException {
		IReleaseService service = SimalRepositoryFactory.getReleaseService();
		IDoapRelease release = service.create(RELEASE_ONE_URI);
		assertNotNull(release);
		project.addRelease(release);
		assertEquals("Don't seem to have added release one", 1, project.getReleases().size());

		release = service.create(RELEASE_TWO_URI);
		project.addRelease(release);
		assertEquals("Don't seem to have added release one", 2, project.getReleases().size());
	}

	@AfterClass
	public static void cleanUpRepisotory() {
		try {
			ocm = ((JcrSimalRepository) repo).getObjectContentManager();
			Session session = ocm.getSession();
			NodeIterator nodeIterator = session.getRootNode().getNodes();

			while (nodeIterator.hasNext()) {
				Node node = nodeIterator.nextNode();
				if (!node.getName().startsWith("jcr:")) {
					logger.debug("tearDown - remove : " + node.getPath());
					node.remove();
				}
			}
			session.save();
			repo.destroy();
		} catch (Exception e) {
			logger.error("cleanUpRepository failed", e);
		}
	}
}