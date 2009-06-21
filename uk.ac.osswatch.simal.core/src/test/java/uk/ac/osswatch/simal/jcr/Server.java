package uk.ac.osswatch.simal.jcr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class Server extends BaseJcrTest {

	private static Node root;
	
	static String msg =  "Hello, World!";

	@BeforeClass
	public static void addContent() throws RepositoryException {
		root = session.getRootNode();
        Node hello = root.addNode("hello");
        Node world = hello.addNode("world");
        world.setProperty("message", msg);
        session.save();
	}
	
	@AfterClass
	public static void removeContent() throws VersionException, LockException, ConstraintViolationException, PathNotFoundException, RepositoryException {
        root.getNode("hello").remove();
        session.save();
	}
	
	@Test
	public void serverStarted() {
		assertTrue("The server has not started correctly", session.isLive());
	}
	
	@Test
	public void retrieveContent() throws PathNotFoundException, RepositoryException {
        Node node = root.getNode("hello/world");
        assertEquals("Content message is incorrect", msg, node.getProperty("message").getString());
	}
	
}
