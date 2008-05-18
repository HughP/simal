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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;

public class TestRDFUtils extends BaseRepositoryTest {
  static Document dom;

  @BeforeClass
  public static void beforeClass() throws URISyntaxException, IOException, SimalRepositoryException, ParserConfigurationException, SAXException {
    URL url = TestRDFUtils.class.getResource("/testData/testNoRDFAboutDOAP.xml");
    RDFUtils.preProcess(url, SimalRepository.TEST_FILE_BASE_URL, repository);
    File processedFile = RDFUtils.getAnnotatedDoapFile(url); 
    
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
  public void testRemovePersonBNodes() {
    NamedNodeMap atts;
    String about;
    NodeList personNL = dom.getElementsByTagNameNS(RDFUtils.FOAF_NS, "Person");
    Node personNode = personNL.item(0);
    atts = personNode.getAttributes();
    about = atts.getNamedItemNS(RDFUtils.RDF_NS, "about").getNodeValue();
    assertEquals("rdf:about incorrect", "http://simal.oss-watch.ac.uk/foaf/Joe Blogs Maintainer#Person", about);

    personNode = personNL.item(1);
    atts = personNode.getAttributes();
    about = atts.getNamedItemNS(RDFUtils.RDF_NS, "about").getNodeValue();
    assertEquals("rdf:about incorrect", "http://simal.oss-watch.ac.uk/foaf/Jane Blogs Maintainer#Person", about);
    
    personNode = personNL.item(2);
    atts = personNode.getAttributes();
    about = atts.getNamedItemNS(RDFUtils.RDF_NS, "about").getNodeValue();
    assertEquals("rdf:about incorrect", "http://simal.oss-watch.ac.uk/foaf/Janet Blogo#Person", about);
  }

  @Test
  public void testRemoveVersionBNodes() {
    NamedNodeMap atts;
    String about;
    Node revisionNode = dom.getElementsByTagNameNS(RDFUtils.DOAP_NS, "Version").item(0);
    atts = revisionNode.getAttributes();
    about = atts.getNamedItemNS(RDFUtils.RDF_NS, "about").getNodeValue();
    assertEquals("rdf:about incorrect", "http://simal.oss-watch.ac.uk/doap/testNoRDFAboutDOAP/0.2#Version", about);
  }

  @Test
  public void testRemoveSVNRepositoryBNodes() {
    NamedNodeMap atts;
    String about;
    Node repositoryNode = dom.getElementsByTagNameNS(RDFUtils.DOAP_NS, "SVNRepository").item(0);
    atts = repositoryNode.getAttributes();
    about = atts.getNamedItemNS(RDFUtils.RDF_NS, "about").getNodeValue();
    assertEquals("rdf:about incorrect", "https://svn.foo.org/svnroot/simalTest/doap#SVNRepository", about);
  }

  @Test
  public void testRemoveProjectBNodes() {
    Node projectNode = getProjectNode();
    NamedNodeMap atts = projectNode.getAttributes();
    String about = atts.getNamedItemNS(RDFUtils.RDF_NS, "about").getNodeValue();
    assertEquals("rdf:about incorrect", "http://simal.oss-watch.ac.uk/doap/Simal (No rdf:about) DOAP Test#Project", about);
  }

  private Node getProjectNode() {
    Node projectNode = dom.getElementsByTagNameNS(RDFUtils.DOAP_NS, "Project").item(0);
    return projectNode;
  }
  
  @Test
  public void testGetProjectName() {
    Node projectNode = getProjectNode();
    String name = RDFUtils.getProjectName((Element) projectNode);
    assertEquals("Project name is incorrect", "Simal (No rdf:about) DOAP Test", name);
  }
  
  @Test
  public void testSimalPersonID() {
    NodeList personNL = dom.getElementsByTagNameNS(RDFUtils.FOAF_NS, "Person");
    Element personElement = (Element)personNL.item(0);
    NodeList personIDNL = personElement.getElementsByTagNameNS(RDFUtils.SIMAL_NS, "personId");
    assertEquals("Incorrect number of simal:personId elements", 1, personIDNL.getLength());
  }
  
  @Test
  public void testSimalProjectID() {
    NodeList projectNL = dom.getElementsByTagNameNS(RDFUtils.DOAP_NS, "Project");
    Element projectElement = (Element)projectNL.item(0);
    NodeList projectIDNL = projectElement.getElementsByTagNameNS(RDFUtils.SIMAL_NS, "projectId");
    assertEquals("Incorrect number of simal:projectId elements", 1, projectIDNL.getLength());
  }
  
  @Test
  public void testProjectSeeAlso() {
    NodeList projectNL = dom.getElementsByTagNameNS(RDFUtils.DOAP_NS, "Project");
    Element projectElement = (Element)projectNL.item(0);
    NodeList projectSeeAlso = projectElement.getElementsByTagNameNS(RDFUtils.RDFS_NS, "seeAlso");
    assertTrue("No rdfs:seeAlso elements defined for the project", projectSeeAlso.getLength() > 0);
  }
}
