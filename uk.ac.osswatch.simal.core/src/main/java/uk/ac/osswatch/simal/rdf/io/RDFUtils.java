package uk.ac.osswatch.simal.rdf.io;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * A set of RDF utils for working with RDF data.
 * 
 */
public class RDFUtils {

  public static final String DOAP_NS = "http://usefulinc.com/ns/doap#";
  public static final String FOAF_NS = "http://xmlns.com/foaf/0.1/";
  public static final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

  /**
   * Load the RDF from a file and add rdf:about to all blank nodes. The
   * resulting blank nodes will look like:<br/>
   * 
   * 
   * Project: http://simal.oss-watch.ac.uk/doap/PROJECT_NAME#Project<br/>
   * Repository:
   * 
   * @rdf:resource/doap#Repository<br/> ArchRepository:
   * @rdf:resource/doap#ArchRepository<br/> BKRepository:
   * @rdf:resource/doap#BKRepository<br/> CVSRepository:
   * @rdf:resource/doap#CVSRepository<br/> SVNRepository:
   * @rdf:resource/doap#SVNRepository<br/> Version:
   *                                  http://simal.oss-watch.ac.uk/doap/PROJECT_NAME/@revision#Version<br/>
   *                                  http://simal.oss-watch.ac.uk/foaf/PERSON_NAME#Person<br/>
   * 
   * @param url
   *          the URL of the file to parse
   * @return
   * @throws URISyntaxException
   */
  public static Document removeBNodes(URL url) throws URISyntaxException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    Document dom = null;

    try {
      DocumentBuilder db = dbf.newDocumentBuilder();
      dom = db.parse(new File(url.toURI()));
      NodeList nl = dom.getElementsByTagNameNS(DOAP_NS, "Project");
      removeBlankProjectNodes(nl);

      nl = dom.getElementsByTagNameNS(DOAP_NS, "Repository");
      removeBlankRepositoryNodes(nl);

      nl = dom.getElementsByTagNameNS(DOAP_NS, "ArchRepository");
      removeBlankRepositoryNodes(nl);

      nl = dom.getElementsByTagNameNS(DOAP_NS, "BKRepository");
      removeBlankRepositoryNodes(nl);

      nl = dom.getElementsByTagNameNS(DOAP_NS, "CVSRepository");
      removeBlankRepositoryNodes(nl);

      nl = dom.getElementsByTagNameNS(DOAP_NS, "SVNRepository");
      removeBlankRepositoryNodes(nl);

      nl = dom.getElementsByTagNameNS(DOAP_NS, "Version");
      removeBlankVersionNodes(nl);

      nl = dom.getElementsByTagNameNS(FOAF_NS, "Person");
      removeBlankPersonNodes(nl);
    } catch (ParserConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SAXException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return dom;
  }

  /**
   * Examine each of the repository nodes in the supplied node list and check
   * they have a rdf:about attribute. If any do not have the attribute then add
   * it.
   * 
   * @param nl
   * @throws DOMException
   */
  private static void removeBlankRepositoryNodes(NodeList nl) {
    if (nl != null && nl.getLength() > 0) {
      for (int i = 0; i < nl.getLength(); i++) {
        Element el = (Element) nl.item(i);
        if (!el.hasAttributeNS(RDF_NS, "about")) {
          Node locationNode = el.getElementsByTagNameNS(DOAP_NS, "location")
              .item(0);
          String uri = locationNode.getAttributes().getNamedItemNS(RDF_NS,
              "resource").getNodeValue();
          if (!uri.endsWith("/")) {
            uri = uri + "/";
          }
          uri = uri + "doap#";
          uri = uri + el.getNodeName();
          el.setAttributeNS(RDF_NS, "rdf:about", uri);
        }
      }
    }
  }

  /**
   * Examine each of the project nodes in the supplied node list and check they
   * have a rdf:about attribute. If any do not have the attribute then add it.
   * 
   * @param nl
   * @throws DOMException
   */
  private static void removeBlankProjectNodes(NodeList nl) throws DOMException {
    if (nl != null && nl.getLength() > 0) {
      for (int i = 0; i < nl.getLength(); i++) {
        Element el = (Element) nl.item(i);
        if (!el.hasAttributeNS(RDF_NS, "about")) {
          String uri = "http://simal.oss-watch.ac.uk/doap/";
          Node nameNode = el.getElementsByTagNameNS(DOAP_NS, "name").item(0);
          uri = uri + nameNode.getFirstChild().getNodeValue();
          uri = uri + "#Project";
          el.setAttributeNS(RDF_NS, "rdf:about", uri);
        }
      }
    }
  }

  /**
   * Examine each of the person nodes in the supplied node list and check they
   * have a rdf:about attribute. If any do not have the attribute then add it.
   * 
   * @param nl
   * @throws DOMException
   */
  private static void removeBlankPersonNodes(NodeList nl) throws DOMException {
    if (nl != null && nl.getLength() > 0) {
      for (int i = 0; i < nl.getLength(); i++) {
        Element el = (Element) nl.item(i);
        if (!el.hasAttributeNS(RDF_NS, "about")) {
          String uri = "http://simal.oss-watch.ac.uk/foaf/";
          Node nameNode = el.getElementsByTagNameNS(FOAF_NS, "name").item(0);
          String name = nameNode.getFirstChild().getNodeValue();
          if (name == null || !nameNode.getParentNode().equals((Node) el)) {
            nameNode = el.getElementsByTagNameNS(FOAF_NS, "givenname").item(0);
            if (nameNode != null) {
              name = nameNode.getFirstChild().getNodeValue();
            }
            nameNode = el.getElementsByTagNameNS(FOAF_NS, "family_name")
                .item(0);
            if (nameNode != null) {
              name = name + " " + nameNode.getFirstChild().getNodeValue();
            }
          }
          uri = uri + name;
          uri = uri + "#Person";
          el.setAttributeNS(RDF_NS, "rdf:about", uri);
        }
      }
    }
  }

  /**
   * Examine each of the version nodes in the supplied node list and check they
   * have a rdf:about attribute. If any do not have the attribute then add it.
   * 
   * @param nl
   * @throws DOMException
   */
  private static void removeBlankVersionNodes(NodeList nl) throws DOMException {
    if (nl != null && nl.getLength() > 0) {
      for (int i = 0; i < nl.getLength(); i++) {
        Element el = (Element) nl.item(i);
        if (!el.hasAttributeNS(RDF_NS, "about")) {
          String uri = "http://simal.oss-watch.ac.uk/doap/";
          Node nameNode = el.getElementsByTagNameNS(DOAP_NS, "name").item(0);
          uri = uri + nameNode.getFirstChild().getNodeValue();
          Node revisionNode = el.getElementsByTagNameNS(DOAP_NS, "revision")
              .item(0);
          uri = uri + "/" + revisionNode.getFirstChild().getNodeValue();
          uri = uri + "#Version";
          el.setAttributeNS(RDF_NS, "rdf:about", uri);
        }
      }
    }
  }
}
