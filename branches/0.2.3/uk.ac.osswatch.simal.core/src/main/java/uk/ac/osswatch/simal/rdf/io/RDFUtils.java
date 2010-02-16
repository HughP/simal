package uk.ac.osswatch.simal.rdf.io;

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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

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

import uk.ac.osswatch.simal.HomepageLabelGenerator;
import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.simal.SimalOntology;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

import com.hp.hpl.jena.sparql.vocabulary.DOAP;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * A set of RDF utils for working with RDF data. Typically we will call
 * preProcess(url, baseURL, repository) in order to clean up RDF data from other
 * sources and to ensure that the maximum data is available to us.
 * 
 */
public class RDFUtils {
  public static final String PROJECT_NAMESPACE_URI = "http://simal.oss-watch.ac.uk/doap/";
  public static final String PERSON_NAMESPACE_URI = "http://simal.oss-watch.ac.uk/foaf/";
  public static final String REPOSITORY_NAMESPACE_URI = "http://simal.oss-watch.ac.uk/rcs/";
  public static final String SIMAL_NAMESPACE_URI = "http://oss-watch.ac.uk/ns/0.2/simal#";
  public static final String SIMAL_REVIEW_NAMESPACE_URI = SIMAL_NAMESPACE_URI + "Review";
  public static final String CATEGORY_NAMESPACE_URI = "http://simal.oss-watch.ac.uk/defaultCategoryNS#";
    
  public static final String SIMAL_NS = "http://oss-watch.ac.uk/ns/0.2/simal#";
  public static final String SIMAL_PERSON = SIMAL_NS + "Person";
  public static final String SIMAL_PROJECT = SIMAL_NS + "Project";
  public static final String SIMAL_CATEGORY_ID = "categoryId";
  public static final String SIMAL_PERSON_ID = "personId";
  public static final String SIMAL_PROJECT_ID = "projectId";

  private static final Logger logger = LoggerFactory.getLogger(RDFUtils.class);

  public static final String DOAP_NS = DOAP.NS;
  public static final String DC_NS = DC.NS;
  public static final String FOAF_NS = FOAF.NS;
  public static final String RDF_NS = RDF.getURI();
  public static final String RDFS_NS = RDFS.getURI();

  private RDFUtils() {
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
   *                                  http://simal.oss-watch.ac.uk/doap/PROJECT_NAME/@revision#Version<br/>
   *                                  http://simal.oss-watch.ac.uk/foaf/PERSON_NAME#Person<br/>
   * 
   * @param url
   *          the URL of the file to parse
   * @return
   * @throws DOMException
   * @throws UnsupportedEncodingException
   */
  public static void removeBNodes(final Document doc) throws DOMException,
      UnsupportedEncodingException {

    NodeList nl = doc.getElementsByTagNameNS(DOAP_NS, "Repository");
    removeBlankRepositoryNodes(nl);

    nl = doc.getElementsByTagNameNS(DOAP_NS, "ArchRepository");
    removeBlankRepositoryNodes(nl);

    nl = doc.getElementsByTagNameNS(DOAP_NS, "BKRepository");
    removeBlankRepositoryNodes(nl);

    nl = doc.getElementsByTagNameNS(DOAP_NS, "CVSRepository");
    removeBlankRepositoryNodes(nl);

    nl = doc.getElementsByTagNameNS(DOAP_NS, "SVNRepository");
    removeBlankRepositoryNodes(nl);

    nl = doc.getElementsByTagNameNS(DOAP_NS, "Version");
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
        if (!el.hasAttributeNS(RDF_NS, "about")) {
          String uri = null;
          Node locationNode = el.getElementsByTagNameNS(DOAP_NS, "location")
              .item(0);
          if (locationNode != null) {
            uri = locationNode.getAttributes().getNamedItemNS(RDF_NS,
                "resource").getNodeValue().trim();
          } else {
            locationNode = el.getElementsByTagNameNS(DOAP_NS, "anon-root")
                .item(0);
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
          el.setAttributeNS(RDF_NS, "rdf:about", encode(uri));
        }
      }
    }
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
        && node.getNamespaceURI().equals(DOAP_NS)) {
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
  public static String getProjectName(Element project) {
    NodeList names = project.getElementsByTagNameNS(DOAP_NS, "name");
    Node nameNode = names.item(0);
    return nameNode.getFirstChild().getNodeValue();
  }

  public static String getDefaultPersonURI(String name) {
    String uri = PERSON_NAMESPACE_URI;
    uri = uri + name;
    uri = uri + "#Person";
    uri = encode(uri);
    return uri;
  }

  public static String getDefaultReviewURI(String name) {
    String uri = "http://simal.oss-watch.ac.uk/";
    uri = uri + name;
    uri = uri + "#Review";
    uri = encode(uri);
    return uri;
  }

  public static String getDefaultProjectURI(String id) {
    String uri = PROJECT_NAMESPACE_URI;
    uri = uri + id;
    uri = uri + "#Project";
    uri = encode(uri);
    return uri;
  }

  /**
   * Encode the given string so that it can be used to create a valid URI. FOr
   * example, encode ' ' as %20
   * 
   * @param uri
   * @return
   */
  private static String encode(String uri) {
    return uri.replace(" ", "%20");
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
        if (!el.hasAttributeNS(RDF_NS, "about")) {
          StringBuilder uri = new StringBuilder(PROJECT_NAMESPACE_URI);
          Node nameNode = el.getElementsByTagNameNS(DOAP_NS, "name").item(0);
          uri.append(nameNode.getFirstChild().getNodeValue());
          Node revisionNode = el.getElementsByTagNameNS(DOAP_NS, "revision")
              .item(0);
          uri.append("/");
          uri.append(revisionNode.getFirstChild().getNodeValue());
          uri.append("#Version");
          el.setAttributeNS(RDF_NS, "rdf:about", encode(uri.toString()));
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
    NodeList nodes = doc.getElementsByTagNameNS(DOAP_NS, "homepage");
    for (int i = 0; i < nodes.getLength(); i++) {
      Element homepage = (Element) nodes.item(i);
      String uri = homepage.getAttributeNS(RDF_NS, "resource");
      String label = homepage.getAttributeNS(RDFS_NS, "label");
      if (label.length() == 0) {
    	  label =  HomepageLabelGenerator.getHomepageLabel(uri);
        homepage.setAttributeNS(RDFS_NS, "label", label);
        logger.debug("Set title of webpage at {} to {}", new String[] { uri,
            label });
      }
    }
  }

/**
   * Some of the RDF elements may contain HTML. This content should be within a
   * CData section to prevent it being interpreted as RDF. This method will look
   * at these sections and, if necessary, will mark it as CData.
   * 
   * @param doc
   */
  public static void checkCDataSections(Document doc) {
    NodeList nodes = doc.getElementsByTagNameNS(DOAP_NS, "description");
    validateCData(nodes);

    nodes = doc.getElementsByTagNameNS(DOAP_NS, "shortdesc");
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
   * Add a see also element to an existing element.
   * 
   * @param node
   * @param originalURI
   */
  private static void addSeeAlso(Node node, String uri) {
    if (uri == null || uri.equals("")) {
      return;
    }
    Element seeAlso = node.getOwnerDocument().createElementNS(RDFS_NS,
        "seeAlso");
    seeAlso.setAttributeNS(RDF_NS, "resource", uri);
    node.appendChild(seeAlso);
    logger.debug("Added rdfs:seeAlso=\"{}\"", uri);
  }

  /**
   * Check that resources are correctly defined.
   * 
   * @param doc
   * @param repo
   */
  public static void checkResources(Document doc) {
    logger.debug("Check resources found in RDF file");
    validateResourceDefinition(doc.getElementsByTagNameNS(DOAP_NS,
        "bug-database"));
    validateResourceDefinition(doc.getElementsByTagNameNS(DOAP_NS,
        "download-page"));
    validateResourceDefinition(doc.getElementsByTagNameNS(FOAF_NS, "homepage"));
    validateResourceDefinition(doc.getElementsByTagNameNS(DOAP_NS, "license"));
    validateResourceDefinition(doc.getElementsByTagNameNS(DOAP_NS,
        "mailing-list"));
    validateResourceDefinition(doc.getElementsByTagNameNS(DOAP_NS, "wiki"));
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
      Attr resource = el.getAttributeNodeNS(RDF_NS, "resource");
      if (resource == null || resource.getValue().trim().equals("")) {
        parent.removeChild(el);
        i = i - 1;
      }
    }
  }

  /**
   * For every mailbox associated with a person create an sha1 checksum.
   * 
   * @param doc
   * @throws ISimalRepositoryException
   */
  public static void checkPersonSHA1(Document doc)
      throws SimalRepositoryException {
    logger.debug("Check person SHA1 in RDF file");
    NodeList people = doc.getElementsByTagNameNS(FOAF_NS, "Person");
    Element person;
    for (int i = 0; i < people.getLength(); i = i + 1) {
      person = (Element) people.item(i);
      NodeList emailNL = person.getElementsByTagNameNS(FOAF_NS, "mbox");
      for (int i1 = 0; i1 < emailNL.getLength(); i1 = i1 + 1) {
        Element emailEl = (Element) emailNL.item(i1);
        String mbox = emailEl.getAttributeNS(RDF_NS, "resource").trim();
        String sha1;
        try {
          sha1 = getSHA1(mbox);
        } catch (NoSuchAlgorithmException e) {
          throw new SimalRepositoryException("Unable to create SHA1 Sum", e);
        }
        Element sha1Element = doc.createElementNS(FOAF_NS, "mbox_sha1sum");
        Text sha1Text = doc.createTextNode(sha1);
        sha1Element.appendChild(sha1Text);
        person.appendChild(sha1Element);
      }
    }
  }

  /**
   * A simple method for getting an SHA1 hash from a string.
   */
  public static String getSHA1(String data) throws NoSuchAlgorithmException {
    String addr;
    data = data.toLowerCase(Locale.getDefault());
    if (data.startsWith("mailto:")) {
      addr = data.substring(7);
    } else {
      addr = data;
    }
    MessageDigest md = MessageDigest.getInstance("SHA");
    StringBuffer sb = new StringBuffer();

    md.update(addr.getBytes());
    byte[] digest = md.digest();
    for (int i = 0; i < digest.length; i++) {
      String hex = Integer.toHexString(digest[i]);
      if (hex.length() == 1) {
        hex = "0" + hex;
      }
      hex = hex.substring(hex.length() - 2);
      sb.append(hex);
    }
    return sb.toString();
  }

  /**
   * Check that all categories have an ID associated with them.
   * 
   * @param doc
   * @param repo
   * @throws ISimalRepositoryException
   */
  public static void checkCategoryIDs(Document doc, ISimalRepository repo)
      throws SimalRepositoryException {
    logger.debug("Check category IDs in RDF file");
    NodeList categories = doc.getElementsByTagNameNS(DOAP_NS, "category");
    Element category;
    Attr simalIDAtt;
    String id = null;
    for (int i = 0; i < categories.getLength(); i = i + 1) {
      category = (Element) categories.item(i);
      simalIDAtt = category.getAttributeNodeNS(SimalOntology.NS,
          SIMAL_CATEGORY_ID);
      if (simalIDAtt == null) {
        IDoapCategory simalCategory;
        try {
          Attr att = category.getAttributeNodeNS(RDF_NS, "resource");
          simalCategory = SimalRepositoryFactory.getCategoryService().get(att.getNodeValue().trim());
          if (simalCategory != null) {
            id = simalCategory.getSimalID();
          } else {
            id = SimalRepositoryFactory.getPersonService().getNewID();
          }
          simalIDAtt = doc.createAttributeNS(SimalOntology.NS, SIMAL_CATEGORY_ID);
          simalIDAtt.setValue(id);
          category.setAttributeNode(simalIDAtt);
        } catch (Exception e) {
          throw new SimalRepositoryException("Unable to get category", e);
        }
      }
    }
  }

  /**
   * If there are IDs for other instances of Simal then ad
   * rdf:seeAlso nodes.
   * 
   * @param doc
   * @param repo
   * @throws ISimalRepositoryException
   */
  public static void checkPersonIDs(Document doc, ISimalRepository repo)
      throws SimalRepositoryException {
    NodeList people = doc.getElementsByTagNameNS(FOAF_NS, "Person");
    Element person;
    NodeList simalIDNL;
    String simalPersonID = null;
    for (int i = 0; i < people.getLength(); i = i + 1) {
      person = (Element) people.item(i);

      // make all simal ID's for other servers a seeAlso tuple
      simalIDNL = person.getElementsByTagNameNS(SimalOntology.NS, SIMAL_PERSON_ID);
      for (int iDidx = 0; iDidx < simalIDNL.getLength(); iDidx++) {
        Node idNode = simalIDNL.item(iDidx);
        if (idNode.getParentNode().equals(person)) {
          simalPersonID = idNode.getTextContent().trim();
          if (!simalPersonID.startsWith(SimalProperties
              .getProperty(SimalProperties.PROPERTY_SIMAL_INSTANCE_ID))) {
            addSeeAlso(person, "simal:" + simalPersonID);
            person.removeChild(idNode);
          }
        }
      }
    }
  }

  /**
   * If there are IDs for other instances of Simal
   * then ad rdf:seeAlso nodes.
   * 
   * @param doc
   * @param repo
   * @throws ISimalRepositoryException
   */
  public static void checkProjectIDs(Document doc, ISimalRepository repo)
      throws SimalRepositoryException {
    NodeList projects = doc.getElementsByTagNameNS(DOAP_NS, "Project");
    Element project;
    NodeList simalIDNL;
    String projectID = null;

    for (int i = 0; i < projects.getLength(); i = i + 1) {
      project = (Element) projects.item(i);

      // make all simal ID's for other servers a seeAlso tuple
      simalIDNL = project.getElementsByTagNameNS(SimalOntology.NS, SIMAL_PROJECT_ID);
      for (int iDidx = 0; iDidx < simalIDNL.getLength(); iDidx++) {
        Node idNode = simalIDNL.item(iDidx);
        if (idNode.getParentNode().equals(project)) {
          projectID = idNode.getTextContent().trim();
          if (!projectID.startsWith(SimalProperties
              .getProperty(SimalProperties.PROPERTY_SIMAL_INSTANCE_ID))) {
            addSeeAlso(project, "simal:" + projectID);
            project.removeChild(idNode);
          }
        }
      }
    }
  }

  /**
   * Get the File for the local, annotated version of the file with the given
   * name.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public static File getAnnotatedDoapFile(String filename)
      throws SimalRepositoryException {
    String writefile;
    File fileStoreDir;
    try {
      fileStoreDir = new File(SimalProperties
          .getProperty(SimalProperties.PROPERTY_SIMAL_DOAP_FILE_STORE)
          + File.separator + "simal-uploads");
    } catch (SimalRepositoryException e) {
      throw new SimalRepositoryException(
      "Unable to create the filestore for annotated files");
    }
    if(!fileStoreDir.mkdirs()) {
      throw new SimalRepositoryException(
      "Unable to create the filestore for annotated files");
    }

    String path = fileStoreDir.getAbsolutePath();
    if (!(filename.endsWith(".rdf") || filename.endsWith(".xml"))) {
      writefile = filename + ".rdf";
    } else {
      writefile = filename;
    }
    File file = new File(path + File.separator + System.currentTimeMillis()
        + "_" + writefile);
    return file;
  }

  /**
   * Get the File for the local, annotated version of the file located at the
   * the given URL.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public static File getAnnotatedFile(URL url) throws SimalRepositoryException {
    String filename;
    String path = url.getPath();
    int startName = path.lastIndexOf("/");
    if (startName >= 0) {
      filename = path.substring(startName + 1);
    } else {
      filename = path;
    }
    return getAnnotatedDoapFile(filename);
  }
}