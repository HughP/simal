/*
 * Copyright 2010 University of Oxford 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

package uk.ac.osswatch.simal.rdf.io;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.ac.osswatch.simal.HomepageLabelGenerator;
import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.simal.SimalOntology;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Utility class for specific RDF/XML functions.
 */
public final class RDFXMLUtils {
  private static final Logger LOGGER = LoggerFactory
      .getLogger(RDFXMLUtils.class);

  private RDFXMLUtils() {
    // don't allow instantation
  }

  public static Document convertXmlStringToDom(String xmlSource) throws SimalException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    DocumentBuilder builder;
    Document domResult = null;
    if (xmlSource != null) {
      try {
        builder = factory.newDocumentBuilder();
        domResult = builder.parse(new InputSource(new StringReader(xmlSource)));
      } catch (ParserConfigurationException e) {
        LOGGER.warn("Unexpected ParserConfigurationException: " + e.getMessage(),e);
      } catch (SAXException e) {
        LOGGER.warn("Unexpected SAXException: " + e.getMessage(),e);
        throw new SimalException("Tried to create DOM Document from invalid XML", e);
      } catch (IOException e) {
        LOGGER.warn("Unexpected IOException: " + e.getMessage(),e);
      }
    }
    return domResult;
  }

  /**
   * Parse the Document and write to the model.
   * 
   * @param doc
   * @param model
   * @throws SimalRepositoryException
   */
  public static void addRDFXMLToModel(Document doc, Model model)
      throws SimalRepositoryException {
    StringWriter xmlAsWriter = new StringWriter();
    StreamResult result = new StreamResult(xmlAsWriter);
    try {
      TransformerFactory.newInstance().newTransformer().transform(
          new DOMSource(doc), result);
    } catch (TransformerConfigurationException e) {
      throw new SimalRepositoryException("Unable configure XSL Transformer", e);
    } catch (TransformerException e) {
      throw new SimalRepositoryException("Unable to transform document", e);
    } catch (TransformerFactoryConfigurationError e) {
      throw new SimalRepositoryException("Unable to create XSL Transformer", e);
    }
    String xml = xmlAsWriter.toString();
    StringReader xmlReader = new StringReader(xml);
    // FIXME Are we sure this is the right way? JavaDoc suggests otherwise.
    model.read(xmlReader, "");
  }

  /**
   * Add rdf:about to all blank nodes other than project and people. Projects
   * and people are dealt with separately once we have worked out the simalID
   * value.
   * 
   * @rdf:resource/doap#Repository<br/> ArchRepository:
   * @rdf:resource/doap#ArchRepository<br/> BKRepository:
   * @rdf:resource/doap#BKRepository<br/> CVSRepository:
   * @rdf:resource/doap#CVSRepository<br/> SVNRepository:
   * @rdf:resource/doap#SVNRepository<br/> Version:
   *                                       http://simal.oss-watch.ac.uk/
   *                                       doap/PROJECT_NAME/@revision#Version<br/>
   *                                       http://simal.oss-watch.ac.uk/foaf/PERSON_NAME
   *                                       #Person<br/>
   * 
   * @param url
   *          the URL of the file to parse
   * @return
   * @throws DOMException
   * @throws UnsupportedEncodingException
   */
  public static void removeBNodes(final Document doc) throws DOMException,
      UnsupportedEncodingException {

    NodeList nl = doc.getElementsByTagNameNS(RDFUtils.DOAP_NS, "Repository");
    removeBlankRepositoryNodes(nl);

    nl = doc.getElementsByTagNameNS(RDFUtils.DOAP_NS, "ArchRepository");
    removeBlankRepositoryNodes(nl);

    nl = doc.getElementsByTagNameNS(RDFUtils.DOAP_NS, "BKRepository");
    removeBlankRepositoryNodes(nl);

    nl = doc.getElementsByTagNameNS(RDFUtils.DOAP_NS, "CVSRepository");
    removeBlankRepositoryNodes(nl);

    nl = doc.getElementsByTagNameNS(RDFUtils.DOAP_NS, "SVNRepository");
    removeBlankRepositoryNodes(nl);

    nl = doc.getElementsByTagNameNS(RDFUtils.DOAP_NS, "Version");
    removeBlankVersionNodes(nl);
  }

  /**
   * Examine each of the repository nodes in the supplied node list and check
   * they have a rdf:about attribute. If any do not have the attribute then add
   * it.
   * 
   * @param nl
   * @throws UnsupportedEncodingException
   * @throws DOMException
   * @throws DOMException
   */
  private static void removeBlankRepositoryNodes(NodeList nl)
      throws DOMException, UnsupportedEncodingException {
    if (nl != null && nl.getLength() > 0) {
      for (int i = 0; i < nl.getLength(); i++) {
        Element el = (Element) nl.item(i);
        if (!el.hasAttributeNS(RDFUtils.RDF_NS, "about")) {
          String uri = null;
          Node locationNode = el.getElementsByTagNameNS(RDFUtils.DOAP_NS,
              "location").item(0);
          if (locationNode != null) {
            uri = locationNode.getAttributes().getNamedItemNS(RDFUtils.RDF_NS,
                "resource").getNodeValue().trim();
          } else {
            locationNode = el.getElementsByTagNameNS(RDFUtils.DOAP_NS,
                "anon-root").item(0);
            if (locationNode != null) {
              uri = locationNode.getFirstChild().getNodeValue();
              uri = uri.substring(uri.indexOf("@") + 1);
              uri = "cvs:" + uri.substring(0, uri.indexOf(":"))
                  + uri.substring(uri.indexOf(":") + 1);
            }
          }

          if (uri == null) {
            uri = "http://simal.oss-watch.ac.uk/"
                + getProjectName(getProjectElement(el)) + "#"
                + el.getLocalName();
          }

          if (!uri.endsWith("/")) {
            uri = uri + "/";
          }
          uri = uri + "doap#";
          uri = uri + el.getNodeName();
          el.setAttributeNS(RDFUtils.RDF_NS, "rdf:about", RDFUtils.encode(uri));
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
   * @throws UnsupportedEncodingException
   */
  private static void removeBlankVersionNodes(NodeList nl) throws DOMException,
      UnsupportedEncodingException {
    if (nl != null && nl.getLength() > 0) {
      for (int i = 0; i < nl.getLength(); i++) {
        Element el = (Element) nl.item(i);
        if (!el.hasAttributeNS(RDFUtils.RDF_NS, "about")) {
          StringBuilder uri = new StringBuilder(RDFUtils.PROJECT_NAMESPACE_URI);
          Node nameNode = el.getElementsByTagNameNS(RDFUtils.DOAP_NS, "name")
              .item(0);
          uri.append(nameNode.getFirstChild().getNodeValue());
          Node revisionNode = el.getElementsByTagNameNS(RDFUtils.DOAP_NS,
              "revision").item(0);
          uri.append("/");
          uri.append(revisionNode.getFirstChild().getNodeValue());
          uri.append("#Version");
          el.setAttributeNS(RDFUtils.RDF_NS, "rdf:about", RDFUtils.encode(uri
              .toString()));
        }
      }
    }
  }

  /**
   * This method looks at all the homepage nodes in the document and attempts to
   * provide a useful label for each one.
   * 
   * @param doc
   */
  public static void checkHomePageNodes(Document doc) {
    NodeList nodes = doc.getElementsByTagNameNS(RDFUtils.DOAP_NS, "homepage");
    for (int i = 0; i < nodes.getLength(); i++) {
      Element homepage = (Element) nodes.item(i);
      String uri = homepage.getAttributeNS(RDFUtils.RDF_NS, "resource");
      String label = homepage.getAttributeNS(RDFUtils.RDFS_NS, "label");
      if (label.length() == 0) {
        label = HomepageLabelGenerator.getHomepageLabel(uri);
        homepage.setAttributeNS(RDFUtils.RDFS_NS, "label", label);
        LOGGER.debug("Set title of webpage at {} to {}", new String[] {
            uri, label });
      }
    }
  }

  /**
   * Check that all categories have an ID associated with them.
   * 
   * @param doc
   * @throws ISimalRepositoryException
   */
  public static void checkCategoryIDs(Document doc)
      throws SimalRepositoryException {
    LOGGER.debug("Check category IDs in RDF file");
    NodeList categories = doc.getElementsByTagNameNS(RDFUtils.DOAP_NS,
        "category");
    Element category;
    Attr simalIDAtt;
    String id = null;
    for (int i = 0; i < categories.getLength(); i = i + 1) {
      category = (Element) categories.item(i);
      simalIDAtt = category.getAttributeNodeNS(SimalOntology.NS,
          RDFUtils.SIMAL_CATEGORY_ID);
      if (simalIDAtt == null) {
        IDoapCategory simalCategory;
        try {
          Attr att = category.getAttributeNodeNS(RDFUtils.RDF_NS, "resource");
          simalCategory = SimalRepositoryFactory.getCategoryService().get(
              att.getNodeValue().trim());
          if (simalCategory != null) {
            id = simalCategory.getSimalID();
          } else {
            id = SimalRepositoryFactory.getPersonService().getNewID();
          }
          simalIDAtt = doc.createAttributeNS(SimalOntology.NS,
              RDFUtils.SIMAL_CATEGORY_ID);
          simalIDAtt.setValue(id);
          category.setAttributeNode(simalIDAtt);
        } catch (Exception e) {
          throw new SimalRepositoryException("Unable to get category", e);
        }
      }
    }
  }

  /**
   * If there are IDs for other instances of Simal then ad rdf:seeAlso nodes.
   * 
   * @param doc
   * @throws ISimalRepositoryException
   */
  public static void checkPersonIDs(Document doc)
      throws SimalRepositoryException {
    NodeList people = doc.getElementsByTagNameNS(RDFUtils.FOAF_NS, "Person");
    Element person;
    NodeList simalIDNL;
    String simalPersonID = null;
    for (int i = 0; i < people.getLength(); i = i + 1) {
      person = (Element) people.item(i);

      // make all simal ID's for other servers a seeAlso tuple
      simalIDNL = person.getElementsByTagNameNS(SimalOntology.NS,
          RDFUtils.SIMAL_PERSON_ID);
      for (int iDidx = 0; iDidx < simalIDNL.getLength(); iDidx++) {
        Node idNode = simalIDNL.item(iDidx);
        if (idNode.getParentNode().equals(person)) {
          simalPersonID = idNode.getTextContent().trim();
          if (!simalPersonID.startsWith(SimalProperties
              .getProperty(SimalProperties.PROPERTY_SIMAL_INSTANCE_ID))) {
            RDFXMLUtils.addSeeAlso(person, "simal:" + simalPersonID);
            person.removeChild(idNode);
          }
        }
      }
    }
  }

  /**
   * If there are IDs for other instances of Simal then ad rdf:seeAlso nodes.
   * 
   * @param doc
   * @throws ISimalRepositoryException
   */
  public static void checkProjectIDs(Document doc)
      throws SimalRepositoryException {
    NodeList projects = doc.getElementsByTagNameNS(RDFUtils.DOAP_NS, "Project");
    Element project;
    NodeList simalIDNL;
    String projectID = null;

    for (int i = 0; i < projects.getLength(); i = i + 1) {
      project = (Element) projects.item(i);

      // make all simal ID's for other servers a seeAlso tuple
      simalIDNL = project.getElementsByTagNameNS(SimalOntology.NS,
          RDFUtils.SIMAL_PROJECT_ID);
      for (int iDidx = 0; iDidx < simalIDNL.getLength(); iDidx++) {
        Node idNode = simalIDNL.item(iDidx);
        if (idNode.getParentNode().equals(project)) {
          projectID = idNode.getTextContent().trim();
          if (!projectID.startsWith(SimalProperties
              .getProperty(SimalProperties.PROPERTY_SIMAL_INSTANCE_ID))) {
            RDFXMLUtils.addSeeAlso(project, "simal:" + projectID);
            project.removeChild(idNode);
          }
        }
      }
    }
  }

  /**
   * Add a see also element to an existing element.
   * 
   * @param node
   * @param originalURI
   */
  private static void addSeeAlso(Node node, String uri) {
    if (uri == null || uri.equals("")) {
      return;
    }
    Element seeAlso = node.getOwnerDocument().createElementNS(RDFUtils.RDFS_NS,
        "seeAlso");
    seeAlso.setAttributeNS(RDFUtils.RDF_NS, "resource", uri);
    node.appendChild(seeAlso);
    LOGGER.debug("Added rdfs:seeAlso=\"{}\"", uri);
  }

  /**
   * For every mailbox associated with a person create an sha1 checksum.
   * 
   * @param doc
   * @throws ISimalRepositoryException
   */
  public static void checkPersonSHA1(Document doc)
      throws SimalRepositoryException {
    LOGGER.debug("Check person SHA1 in RDF file");
    NodeList people = doc.getElementsByTagNameNS(RDFUtils.FOAF_NS, "Person");
    Element person;
    for (int i = 0; i < people.getLength(); i = i + 1) {
      person = (Element) people.item(i);
      NodeList emailNL = person
          .getElementsByTagNameNS(RDFUtils.FOAF_NS, "mbox");
      for (int i1 = 0; i1 < emailNL.getLength(); i1 = i1 + 1) {
        Element emailEl = (Element) emailNL.item(i1);
        String mbox = emailEl.getAttributeNS(RDFUtils.RDF_NS, "resource")
            .trim();
        String sha1;
        try {
          sha1 = RDFUtils.getSHA1(mbox);
        } catch (NoSuchAlgorithmException e) {
          throw new SimalRepositoryException("Unable to create SHA1 Sum", e);
        }
        Element sha1Element = doc.createElementNS(RDFUtils.FOAF_NS,
            "mbox_sha1sum");
        Text sha1Text = doc.createTextNode(sha1);
        sha1Element.appendChild(sha1Text);
        person.appendChild(sha1Element);
      }
    }
  }

  /**
   * Check that resources are correctly defined.
   * 
   * @param doc
   * @param repo
   */
  public static void checkResources(Document doc) {
    LOGGER.debug("Check resources found in RDF file");
    RDFXMLUtils.validateResourceDefinition(doc.getElementsByTagNameNS(
        RDFUtils.DOAP_NS, "bug-database"));
    RDFXMLUtils.validateResourceDefinition(doc.getElementsByTagNameNS(
        RDFUtils.DOAP_NS, "download-page"));
    RDFXMLUtils.validateResourceDefinition(doc.getElementsByTagNameNS(
        RDFUtils.FOAF_NS, "homepage"));
    RDFXMLUtils.validateResourceDefinition(doc.getElementsByTagNameNS(
        RDFUtils.DOAP_NS, "license"));
    RDFXMLUtils.validateResourceDefinition(doc.getElementsByTagNameNS(
        RDFUtils.DOAP_NS, "mailing-list"));
    RDFXMLUtils.validateResourceDefinition(doc.getElementsByTagNameNS(
        RDFUtils.DOAP_NS, "wiki"));
  }

  /**
   * Some of the RDF elements may contain HTML. This content should be within a
   * CData section to prevent it being interpreted as RDF. This method will look
   * at these sections and, if necessary, will mark it as CData.
   * 
   * @param doc
   */
  public static void checkCDataSections(Document doc) {
    NodeList nodes = doc
        .getElementsByTagNameNS(RDFUtils.DOAP_NS, "description");
    validateCData(nodes);

    nodes = doc.getElementsByTagNameNS(RDFUtils.DOAP_NS, "shortdesc");
    validateCData(nodes);
  }

  private static void validateCData(NodeList nodes) {
    for (int i = 0; i < nodes.getLength(); i++) {
      NodeList dataNodes = nodes.item(i).getChildNodes();
      for (int childIdx = 0; childIdx < dataNodes.getLength(); childIdx++) {
        Node child = dataNodes.item(childIdx);
        if (child.getNodeType() != Node.CDATA_SECTION_NODE) {
          CDATASection newDataNode = child.getOwnerDocument()
              .createCDATASection(makeTextual(child));
          child.getParentNode().replaceChild(newDataNode, child);
        }
      }
    }
  }

  /**
   * Convert any elements within a node into their textual representation.
   * 
   * @param item
   */
  private static String makeTextual(Node item) {
    if (item.getNodeType() == Node.TEXT_NODE) {
      return item.getTextContent();
    }
    StringBuffer data = new StringBuffer();
    NodeList children = item.getChildNodes();
    data.append("<");
    data.append(item.getNodeName());
    data.append(">");
    for (int childIdx = 0; childIdx < children.getLength(); childIdx++) {
      data.append(makeTextual(children.item(childIdx)));
    }
    data.append("</");
    data.append(item.getNodeName());
    data.append(">");
    return data.toString();
  }

  /**
   * Work up the tree until we find the parent project element.
   * 
   * @param el
   * @return
   */
  private static Element getProjectElement(Element el) {
    Node node = el.getParentNode();
    if (node.getLocalName().equals("Project")
        && node.getNamespaceURI().equals(RDFUtils.DOAP_NS)) {
      return (Element) node;
    } else {
      return getProjectElement((Element) node);
    }
  }

  /**
   * Get the project name from the supplied doap:Project node.
   * 
   * @param project
   * @return
   */
  private static String getProjectName(Element project) {
    NodeList names = project.getElementsByTagNameNS(RDFUtils.DOAP_NS, "name");
    Node nameNode = names.item(0);
    return nameNode.getFirstChild().getNodeValue();
  }

  /**
   * Checks to see if resources are correctly identified. If not then drop the
   * element.
   * 
   * @TODO This is a bit draconian, we should be looking to see if the DOAP is
   *       incorrectly encoded (i.e. provides a URI as content) and try and
   *       correct things rather than just dumping them.
   * 
   * @param nodes
   */
  public static void validateResourceDefinition(NodeList nodes) {
    Element el;
    for (int i = 0; i < nodes.getLength(); i = i + 1) {
      el = (Element) nodes.item(i);
      Node parent = el.getParentNode();
      Attr resource = el.getAttributeNodeNS(RDFUtils.RDF_NS, "resource");
      if (resource == null || resource.getValue().trim().equals("")) {
        parent.removeChild(el);
        i = i - 1;
      }
    }
  }

}
