package uk.ac.osswatch.simal.integrationTest.model.repository;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
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
import uk.ac.osswatch.simal.model.AbstractResource;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.jcr.JcrSimalRepository;
import uk.ac.osswatch.simal.model.jcr.Project;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.IProjectService;

/**
 * A set of tests that ensure the JCR repository is working in isolation of test
 * data. That is it tests simple CRUD operations on the repositry.
 * 
 */
public class JcrRepositoryBaseTests {
	public static final Logger logger = LoggerFactory
			.getLogger(JcrRepositoryBaseTests.class);
	static ISimalRepository repo;
	private static ObjectContentManager ocm;

	static String uri = "http://foo.org/test";
	
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
		
		IProjectService service = SimalRepositoryFactory.getProjectService();
		IProject project = service.createProject(uri);
		assertNotNull("Created project is a null object", project);
		assertEquals("Project URI is not correct", uri, project.getURI());
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

	@Test
	public void getAllProjects() throws SimalRepositoryException {
		Set<IProject> projects = repo.getAllProjects();
		assertEquals("Got an incorrect number of projects", 1, projects.size());
		Iterator<IProject> itr = projects.iterator();
		while (itr.hasNext()) {
			logger.debug("Got a project with uri " + itr.next().getURI());
		}
	}
	
	@Test
	public void addProject() throws SimalRepositoryException {
		IProjectService service = SimalRepositoryFactory.getProjectService();
		IProject project = service.getProject(uri);
		assertNotNull("Retrieved project is a null object", project);

		ocm.remove(project);
	}

}
