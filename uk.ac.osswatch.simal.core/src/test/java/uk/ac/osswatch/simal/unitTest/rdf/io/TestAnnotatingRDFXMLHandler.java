package uk.ac.osswatch.simal.unitTest.rdf.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

import javax.xml.namespace.QName;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.rdfxml.RDFXMLParser;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.io.AnnotatingRDFXMLHandler;

public class TestAnnotatingRDFXMLHandler extends BaseRepositoryTest {
  static File annotatedFile;
  static AnnotatingRDFXMLHandler annotatingHandler;
  
  static boolean hasProjectQName = false;
  static boolean hasPersonQName = false;
  static boolean hasFoafName = false;
  static boolean hasID = false;
  private static String lastId;
  private static String thisId;

  @BeforeClass
  public static void classSetUp() throws IOException, RDFParseException, RDFHandlerException {
    annotatedFile = new File("testAnnotatedRDFXML.xml");
    annotatingHandler = new AnnotatingRDFXMLHandler(
        annotatedFile, repository);
    annotateTestFile();
  }

  @AfterClass
  public static void classCleanUp() {
    if (annotatedFile != null) {
      annotatedFile.delete();
    }
  }
  
  @Test
  public void testMissingQName() {    
    Set<QName> qnames = annotatingHandler.getProjectQNames();
    assertEquals(
        "We don't seem to have collected the right number of project QNames",
        1, qnames.size());
    QName qname = qnames.iterator().next();
    assertTrue("Generated project QName doesn't appear to be correct", qname
        .getLocalPart().startsWith(
            SimalRepository.DEFAULT_PROJECT_NAMESPACE_URI));
    
    assertTrue("Missing a project QName", hasProjectQName);
    assertTrue("Missing a person QName", hasPersonQName);
  }
  
  @Test
  public void testFoaf() {
    assertTrue("foaf:name not converted to foaf:givenname", !hasFoafName);
  }
  
  @Test 
  public void testID() throws RDFParseException, RDFHandlerException, IOException {
    assertTrue("simal:id not present", hasID);
    TestAnnotatingRDFXMLHandler.annotateTestFile();
    assertFalse("Ids are not unique", lastId.equals(thisId));
  }
  
  private static void annotateTestFile() throws IOException, RDFParseException, RDFHandlerException {
    RDFParser parser = new RDFXMLParser();
    parser.setRDFHandler(annotatingHandler);
    parser.setVerifyData(true);

    parser.parse(SimalRepository.class.getResource(
        SimalRepository.TEST_FILE_URI_NO_QNAME).openStream(),
        SimalRepository.TEST_FILE_BASE_URL);

    validate(annotatedFile);
  }
  
  private static void validate(File annotatedFile) throws FileNotFoundException {
    Scanner scanner = new Scanner(annotatedFile);
    String token;
    while (scanner.hasNext()) {
      token = scanner.next();
      if (token.contains(SimalRepository.DEFAULT_PROJECT_NAMESPACE_URI)) {
        hasProjectQName = true;
      } else if (token.contains(SimalRepository.DEFAULT_PERSON_NAMESPACE_URI)) {
        hasPersonQName = true;
      } else if (token.contains("<foaf:name")) {
        hasFoafName = true;
      } else if (token.contains("<simal:projectId>")) {
        lastId = thisId;
        thisId = token.substring(token.indexOf(">") + 1, token.lastIndexOf("<"));
        hasID = true;
      }
    }
    scanner.close();    
  }
}
