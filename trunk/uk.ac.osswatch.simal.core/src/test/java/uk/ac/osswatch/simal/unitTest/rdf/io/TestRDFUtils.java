package uk.ac.osswatch.simal.unitTest.rdf.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import uk.ac.osswatch.simal.rdf.io.RDFUtils;

public class TestRDFUtils {
  static Document dom;

  @BeforeClass
  public static void beforeClass() throws URISyntaxException, IOException {
    URL url = TestRDFUtils.class.getResource("/testData/testNoRDFAboutDOAP.xml");
    dom = RDFUtils.removeBNodes(url);

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
    Node projectNode = dom.getElementsByTagNameNS(RDFUtils.DOAP_NS, "Project").item(0);
    NamedNodeMap atts = projectNode.getAttributes();
    String about = atts.getNamedItemNS(RDFUtils.RDF_NS, "about").getNodeValue();
    assertEquals("rdf:about incorrect", "http://simal.oss-watch.ac.uk/doap/Simal (No rdf:about) DOAP Test#Project", about);
  }
}
