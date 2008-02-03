package uk.ac.osswatch.simal.rdf.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class SimalRepositoryExceptionTest {

	@Test
	public void testSimalRepositoryException() {
		SimalRepositoryException e = new SimalRepositoryException("Just testing", null);
		assertEquals("Just testing", e.getMessage());
		assertNull(e.getCause());
		
		e = new SimalRepositoryException("Just testing", new IOException("Just testing an IO exception"));
		assertEquals("Just testing", e.getMessage());
		assertNotNull(e.getCause());
	}

}
