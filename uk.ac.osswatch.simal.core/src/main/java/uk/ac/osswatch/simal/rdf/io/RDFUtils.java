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
package uk.ac.osswatch.simal.rdf.io;

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
      dom = db.parse(url.openStream());
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
          String uri = null;
          Node locationNode = el.getElementsByTagNameNS(DOAP_NS, "location")
              .item(0);
          if (locationNode != null) {
            uri = locationNode.getAttributes().getNamedItemNS(RDF_NS,
                "resource").getNodeValue();
          } else {
            locationNode = el.getElementsByTagNameNS(DOAP_NS, "anon-root")
                .item(0);
            if (locationNode != null) {
              uri = locationNode.getFirstChild().getNodeValue();
            }
          }
          
          if (uri == null) {
            uri = "http://simal.oss-watch.ac.uk/" + getProjectName((Element) el.getParentNode()) + "#" + el.getLocalName();
          }
          
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
   * Get the project name from the supplied doiap:Project node.
   * 
   * @param project
   * @return
   */
  public static String getProjectName(Element project) {
    NodeList names = project.getElementsByTagNameNS(DOAP_NS, "name");
    Node nameNode = names.item(0);
    return nameNode.getFirstChild().getNodeValue();
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
          String name = null; 
          String uri = "http://simal.oss-watch.ac.uk/foaf/";
          Node nameNode = el.getElementsByTagNameNS(FOAF_NS, "name").item(0);
          if (nameNode != null && nameNode.getParentNode().equals((Node) el)) {
              name = nameNode.getFirstChild().getNodeValue();
          } else {
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
