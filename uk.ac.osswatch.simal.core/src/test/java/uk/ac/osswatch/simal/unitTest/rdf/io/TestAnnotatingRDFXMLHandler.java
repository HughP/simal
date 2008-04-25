/*
 * Copyright 2007 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.osswatch.simal.unitTest.rdf.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
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
    annotatedFile = new File("target/testAnnotatedRDFXML.xml");
    annotatingHandler = new AnnotatingRDFXMLHandler(
        annotatedFile, repository);
    annotateLocalTestFile();
  }

  @AfterClass
  public static void classCleanUp() {
    if (annotatedFile != null) {
      annotatedFile.delete();
    }
  }

  private static boolean isProjectSeeAlsoPresent;
  
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
    TestAnnotatingRDFXMLHandler.annotateLocalTestFile();
    assertFalse("Ids are not unique", lastId.equals(thisId));
  }
  
  @Test
  public void testSeeAlso() throws RDFParseException, RDFHandlerException, IOException {
    // The remote file annotation test is desabled at present as it breaks the MissingQName test when built from Maven (to many projects added)
    //TestAnnotatingRDFXMLHandler.annotateRemoteTestFile();
    assertTrue("rdf:seeAlso is not present", isProjectSeeAlsoPresent);
  }
  
  private static void annotateLocalTestFile() throws IOException, RDFParseException, RDFHandlerException {
    URL url = SimalRepository.class.getResource(SimalRepository.TEST_FILE_URI_NO_QNAME);
    RDFParser parser = new RDFXMLParser();
    annotatingHandler.setSourceURL(url);
    parser.setRDFHandler(annotatingHandler);
    parser.setVerifyData(true);

    parser.parse(url.openStream(),
        SimalRepository.TEST_FILE_BASE_URL);

    validate(annotatedFile);
  }
  
  private static void annotateRemoteTestFile() throws IOException, RDFParseException, RDFHandlerException {
    URL url =  new URL(SimalRepository.TEST_FILE_REMOTE_URL);
    RDFParser parser = new RDFXMLParser();
    annotatingHandler.setSourceURL(url);
    parser.setRDFHandler(annotatingHandler);
    parser.setVerifyData(true);

    parser.parse(url.openStream(), url.getHost());

    validate(annotatedFile);
  }
  
  private static void validate(File annotatedFile) throws FileNotFoundException {
    isProjectSeeAlsoPresent = false;
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
      } else if (token.contains("<rdfs:seeAlso")) {
        token = scanner.next();
        isProjectSeeAlsoPresent = isProjectSeeAlsoPresent || token.contains(SimalRepository.TEST_FILE_REMOTE_URL) || token.contains(SimalRepository.class.getResource(SimalRepository.TEST_FILE_URI_NO_QNAME).toString());
      }
    }
    scanner.close();    
  }
}
