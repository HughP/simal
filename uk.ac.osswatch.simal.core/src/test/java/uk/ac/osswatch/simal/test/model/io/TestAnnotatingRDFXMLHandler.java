package uk.ac.osswatch.simal.test.model.io;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.junit.Test;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.rdfxml.RDFXMLParser;

import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.io.AnnotatingRDFXMLHandler;

public class TestAnnotatingRDFXMLHandler {

	@Test
	public void testMissingQName() throws RDFParseException,
			RDFHandlerException, IOException {
		File annotatedFile = new File("testAnnotatedRDFXML.xml");
		
		RDFParser parser = new RDFXMLParser();
		parser.setRDFHandler(new AnnotatingRDFXMLHandler(annotatedFile));
		parser.setVerifyData(true);

		parser.parse(SimalRepository.class.getResource(
				SimalRepository.TEST_FILE_URI_NO_QNAME).openStream(),
				SimalRepository.TEST_FILE_BASE_URL);
		
		// cool, it didn't throw an exception, but did it annotate the file?
		
		Scanner scanner = new Scanner(annotatedFile);
		String token;
		boolean hasQName = false;
		boolean hasFoafName = false;
        while (scanner.hasNext()) {
          token = scanner.next();
          if (token.contains(SimalRepository.DEFAULT_NAMESPACE_URI)) {
        	  hasQName = true;
          }
          if (token.contains("foaf:name")) {
        	  hasFoafName = true;
          }
        }
        scanner.close();
        
        // do the tests, if any of these file the annotated file will
        // be left on disk for inspection
        assertTrue("Missing a QName", hasQName);
        assertTrue("foaf:name not converted to foaf:givenname", !hasFoafName);
        
        annotatedFile.delete();
	}
}
