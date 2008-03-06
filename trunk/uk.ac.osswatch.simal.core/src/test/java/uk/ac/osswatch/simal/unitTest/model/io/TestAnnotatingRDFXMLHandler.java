package uk.ac.osswatch.simal.unitTest.model.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.rdfxml.RDFXMLParser;

import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.io.AnnotatingRDFXMLHandler;

public class TestAnnotatingRDFXMLHandler {

  @Test
  public void testMissingQName() throws RDFParseException, RDFHandlerException,
      IOException {
    File annotatedFile = new File("testAnnotatedRDFXML.xml");
    AnnotatingRDFXMLHandler annotatingHandler = new AnnotatingRDFXMLHandler(
        annotatedFile);

    RDFParser parser = new RDFXMLParser();
    parser.setRDFHandler(annotatingHandler);
    parser.setVerifyData(true);

    parser.parse(SimalRepository.class.getResource(
        SimalRepository.TEST_FILE_URI_NO_QNAME).openStream(),
        SimalRepository.TEST_FILE_BASE_URL);

    Set<QName> qnames = annotatingHandler.getProjectQNames();
    assertEquals(
        "We don't seem to have collected the right number of project QNames",
        1, qnames.size());
    QName qname = qnames.iterator().next();
    assertTrue("Generated project QName doesn't appear to be correct", qname
        .getLocalPart().startsWith(
            SimalRepository.DEFAULT_PROJECT_NAMESPACE_URI));

    Scanner scanner = new Scanner(annotatedFile);
    String token;
    boolean hasProjectQName = false;
    boolean hasPersonQName = false;
    boolean hasFoafName = false;
    while (scanner.hasNext()) {
      token = scanner.next();
      if (token.contains(SimalRepository.DEFAULT_PROJECT_NAMESPACE_URI)) {
        hasProjectQName = true;
      }
      if (token.contains(SimalRepository.DEFAULT_PERSON_NAMESPACE_URI)) {
        hasPersonQName = true;
      }
      if (token.contains("foaf:name")) {
        hasFoafName = true;
      }
    }
    scanner.close();

    // do the tests, if any of these file the annotated file will
    // be left on disk for inspection
    assertTrue("Missing a project QName", hasProjectQName);
    assertTrue("Missing a person QName", hasPersonQName);
    assertTrue("foaf:name not converted to foaf:givenname", !hasFoafName);

    annotatedFile.delete();
  }
}
