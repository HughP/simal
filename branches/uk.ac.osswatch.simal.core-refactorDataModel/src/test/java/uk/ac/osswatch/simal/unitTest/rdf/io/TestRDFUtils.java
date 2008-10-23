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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.model.ModelSupport;
import uk.ac.osswatch.simal.model.SimalOntology;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.unitTest.MockRepository;

public class TestRDFUtils {
  static Document dom;
  private static ISimalRepository mockRepository = new MockRepository();

  @BeforeClass
  public static void beforeClass() throws URISyntaxException, IOException,
      SimalRepositoryException, ParserConfigurationException, SAXException {
    URL url = TestRDFUtils.class
        .getResource("/testData/testNoRDFAboutDOAP.xml");
    RDFUtils.preProcess(url, ModelSupport.TEST_FILE_BASE_URL, mockRepository);
    File processedFile = RDFUtils.getLastProcessedFile();

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);

    DocumentBuilder db = dbf.newDocumentBuilder();
    dom = db.parse(new FileInputStream(processedFile));

    OutputFormat format = new OutputFormat(dom);
    format.setIndenting(true);
    XMLSerializer serializer = new XMLSerializer(System.out, format);
    serializer.serialize(dom);

    assertNotNull(dom);
  }

  @Test
  public void testProjectAbout() {
    NodeList projectNL = dom
        .getElementsByTagNameNS(RDFUtils.DOAP_NS, "Project");
    for (int i = 0; i < projectNL.getLength(); i++) {
      Element projectElement = (Element) projectNL.item(i);
      String about = projectElement.getAttributeNS(RDFUtils.RDF_NS, "about");
      assertTrue("rdf:about is incorrect", about
          .startsWith(RDFUtils.PROJECT_NAMESPACE_URI));
    }
  }

  @Test
  public void testPersonAbout() {
    NodeList personNL = dom.getElementsByTagNameNS(RDFUtils.FOAF_NS, "Person");
    for (int i = 0; i < personNL.getLength(); i++) {
      Element personElement = (Element) personNL.item(i);
      String about = personElement.getAttributeNS(RDFUtils.RDF_NS, "about");
      assertTrue("rdf:about=" + about + " is incorrect", about
          .startsWith(RDFUtils.PERSON_NAMESPACE_URI));
    }
  }

  @Test
  public void testRemoveVersionBNodes() {
    NamedNodeMap atts;
    String about;
    Node revisionNode = dom.getElementsByTagNameNS(RDFUtils.DOAP_NS, "Version")
        .item(0);
    atts = revisionNode.getAttributes();
    about = atts.getNamedItemNS(RDFUtils.RDF_NS, "about").getNodeValue();
    assertEquals("rdf:about incorrect",
        "http://simal.oss-watch.ac.uk/doap/testNoRDFAboutDOAP/0.2#Version",
        about);
  }

  @Test
  public void testRemoveSVNRepositoryBNodes() {
    NamedNodeMap atts;
    String about;
    Node repositoryNode = dom.getElementsByTagNameNS(RDFUtils.DOAP_NS,
        "SVNRepository").item(0);
    atts = repositoryNode.getAttributes();
    about = atts.getNamedItemNS(RDFUtils.RDF_NS, "about").getNodeValue();
    assertEquals("rdf:about incorrect",
        "https://svn.foo.org/svnroot/simalTest/doap#SVNRepository", about);
  }

  private Node getProjectNode() {
    Node projectNode = dom.getElementsByTagNameNS(RDFUtils.DOAP_NS, "Project")
        .item(0);
    return projectNode;
  }

  @Test
  public void testGetProjectName() {
    Node projectNode = getProjectNode();
    String name = RDFUtils.getProjectName((Element) projectNode);
    assertEquals("Project name is incorrect", "Simal (No rdf:about) DOAP Test",
        name);
  }

  @Test
  public void testSimalPersonID() throws SimalRepositoryException {
    int idCount;
    NodeList personNL = dom.getElementsByTagNameNS(RDFUtils.FOAF_NS, "Person");
    for (int idx = 0; idx < personNL.getLength(); idx++) {
      idCount = 0;
      Element personElement = (Element) personNL.item(idx);
      NodeList personIDNL = personElement.getElementsByTagNameNS(
          SimalOntology.NS, "personId");
      for (int idIdx = 0; idIdx < personIDNL.getLength(); idIdx++) {
        Node id = personIDNL.item(idIdx);
        if (id.getParentNode().equals(personElement)) {
          idCount++;
          String text = id.getTextContent();
          assertNotNull("ID should not be null", text);
          assertTrue("ID should not be an empty string", text.length() > 0);
          assertTrue("ID should be world unique", text
              .startsWith(SimalProperties
                  .getProperty(SimalProperties.PROPERTY_SIMAL_INSTANCE_ID)));
        }
      }
      assertEquals("Incorrect number of simal:personId elements in person: "
          + getPersonName(personElement), 1, idCount);
    }
  }

  private String getPersonName(Element personElement) {
    NodeList names = personElement.getElementsByTagNameNS(RDFUtils.FOAF_NS,
        "name");
    if (names.getLength() == 0) {
      names = personElement.getElementsByTagNameNS(RDFUtils.FOAF_NS,
          "givennames");
    }
    return names.item(0).getTextContent();
  }

  @Test
  public void testSimalProjectID() {
    NodeList projectNL = dom
        .getElementsByTagNameNS(RDFUtils.DOAP_NS, "Project");
    Element projectElement = (Element) projectNL.item(0);
    NodeList projectIDNL = projectElement.getElementsByTagNameNS(
        SimalOntology.NS, "projectId");
    assertEquals("Incorrect number of simal:projectId elements", 1, projectIDNL
        .getLength());
  }

  @Test
  public void testProjectSeeAlso() {
    NodeList projectNL = dom
        .getElementsByTagNameNS(RDFUtils.DOAP_NS, "Project");
    Element projectElement = (Element) projectNL.item(0);
    NodeList projectSeeAlso = projectElement.getElementsByTagNameNS(
        RDFUtils.RDFS_NS, "seeAlso");
    assertTrue("No rdfs:seeAlso elements defined for the project",
        projectSeeAlso.getLength() > 0);
  }

  @Test
  public void testAddMultipleProjectsInASingleFile()
      throws SimalRepositoryException {
    URL url = TestRDFUtils.class
        .getResource("/testData/testMultipleProjects.xml");
    Set<File> files = RDFUtils.preProcess(url, ModelSupport.TEST_FILE_BASE_URL,
        mockRepository);
    assertEquals("Incorrect number of project files created", 3, files.size());
  }

  /**
   * Check to see that elements that may contain HTML or other such markup are
   * correctly marked up as CData.
   */
  @Test
  public void testCData() {
    Node desc = dom.getElementsByTagNameNS(RDFUtils.DOAP_NS, "description")
        .item(0);
    assertTrue("Description node content is not a CData section", desc
        .getFirstChild().getNodeType() == Node.CDATA_SECTION_NODE);
  }

  @Test
  public void testRemoveBlankPersonNodes() throws SimalRepositoryException,
      ParserConfigurationException, FileNotFoundException, SAXException,
      IOException {
    URL url = TestRDFUtils.class.getResource("/testData/testDOAP.xml");
    RDFUtils.preProcess(url, ModelSupport.TEST_FILE_BASE_URL, mockRepository);
    File processedFile = RDFUtils.getLastProcessedFile();

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);

    DocumentBuilder db = dbf.newDocumentBuilder();
    FileInputStream fis = new FileInputStream(processedFile);
    dom = db.parse(fis);
    fis.close();

    NodeList personNL = dom.getElementsByTagNameNS(RDFUtils.FOAF_NS, "Person");
    Element person = (Element) personNL.item(0);
    String about = person.getAttributeNS(RDFUtils.RDF_NS, "about");
    assertFalse("rdf:about is empty", about.equals(""));
    assertTrue("rdf:about contains spaces - " + about, about.indexOf(" ") < 0);
  }
  
  @Test
  public void testWebServicesResponse() throws SimalRepositoryException, ParserConfigurationException, SAXException, IOException {
    URL url = TestRDFUtils.class.getResource("/testData/webServicesResponse.xml");
    RDFUtils.preProcess(url, ModelSupport.TEST_FILE_BASE_URL, mockRepository);
    File processedFile = RDFUtils.getLastProcessedFile();
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    DocumentBuilder db = dbf.newDocumentBuilder();
    FileInputStream fis = new FileInputStream(processedFile);
    dom = db.parse(fis);
    fis.close();

    NodeList projectNL = dom.getElementsByTagNameNS(RDFUtils.DOAP_NS, "Project");
    assertEquals("The web services response should yield one project", 1, projectNL.getLength());
    
    NodeList personNL = dom.getElementsByTagNameNS(RDFUtils.FOAF_NS, "Person");
    assertEquals("The web services response should yield two people", 2, personNL.getLength());
  }
}
