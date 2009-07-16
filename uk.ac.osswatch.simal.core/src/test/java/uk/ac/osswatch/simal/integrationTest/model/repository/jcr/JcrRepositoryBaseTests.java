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

import java.util.Iterator;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IDoapBugDatabase;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.IDoapRepository;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.jcr.JcrSimalRepository;
import uk.ac.osswatch.simal.model.jcr.Project;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.IBugDatabaseService;
import uk.ac.osswatch.simal.service.IHomepageService;
import uk.ac.osswatch.simal.service.IProjectService;
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
	public static String detailedProjectURI = "http://foo.org/dtailedTestProject";
	public static String skeletonProjectURI = "http://foo.org/skeletonTestProject";
	public static String rcsURI = "http://foo.org/testRCS";
	public static String homepageURI = "http://foo.org";
	public static String issuesURI = "http://foo.org/issues";
	
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
		} catch (Exception e) {
			logger.error("cleanUpRepository failed", e);
		}
	}
}
