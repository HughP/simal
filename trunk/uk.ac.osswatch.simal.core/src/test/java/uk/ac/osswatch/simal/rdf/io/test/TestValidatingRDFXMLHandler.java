package uk.ac.osswatch.simal.rdf.io.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.rdfxml.RDFXMLParser;

import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.io.ValidatingRDFXMLHandler;

public class TestValidatingRDFXMLHandler {

	@Test
	public void testHandler() throws RDFParseException, IOException {
		RDFParser parser = new RDFXMLParser();
		parser.setRDFHandler(new ValidatingRDFXMLHandler());
		parser.setVerifyData(true);

		try {
			parser.parse(SimalRepository.class.getResource("testDOAP.xml")
					.openStream(), "http://exmple.org/baseURI");
		} catch (RDFHandlerException e) {
			fail("Threw excpetion when we have a QName: " + e.getMessage());
		}
		boolean hasNoQName = false;
		try {
			parser.parse(SimalRepository.class.getResource(
					"testNoRDFAboutDOAP.xml").openStream(),
					"http://exmple.org/baseURI");
		} catch (RDFHandlerException e) {
			if (e.getMessage().equals(
					ValidatingRDFXMLHandler.NO_QNAME_PRESENT)) {
				hasNoQName = true;
			}
		}
		assertTrue("Failed to detect that no QName was present", hasNoQName);
	}

}
