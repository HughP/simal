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
package uk.ac.osswatch.simal.unitTest.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.tools.PTSWImport;

public class TestPTSWImport {
  private static final int NUM_OF_PINGS = 3;
  static PTSWImport importer;
  static Document ptswExport;

  @BeforeClass
  public static void createImporter() {
    importer = new PTSWImport();
  }

  @BeforeClass
  public static void readExportDoc() throws ParserConfigurationException,
      SAXException, IOException {
    URL url = TestPTSWImport.class.getResource("/testData/ptswExport.xml");

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    DocumentBuilder db = dbf.newDocumentBuilder();
    ptswExport = db.parse(url.openStream());
  }

  @Test
  public void testListOfPings() {
    Set<URL> pings = importer.getListOfPings(ptswExport);
    assertEquals("Incorrect number of pings", NUM_OF_PINGS, pings.size());
  }

  /**
   * Test the generation of an RDF/XML document containing all pinged DOAP
   * files.
   * 
   * @throws SimalException
   * @throws IOException 
   */
  @Test
  public void testRDFXML() throws SimalException, IOException {
    Document doc = importer.getPingsAsRDF(ptswExport);
    Element root = doc.getDocumentElement();
    NodeList projects = root.getElementsByTagNameNS(RDFUtils.DOAP_NS, "Project");
    assertEquals("Incorrect number of proejct elements", NUM_OF_PINGS, projects.getLength());
    
    assertTrue("RDF namespaces does not seem to be defined", serialise(doc).contains(RDFUtils.RDF_NS));
  }
  
  private String serialise(Document doc) throws IOException {
    OutputFormat format = new OutputFormat (doc); 
    StringWriter writer = new StringWriter();
    XMLSerializer serial   = new XMLSerializer (writer, 
        format);
    serial.serialize(doc);
    return writer.toString();
  }
}
