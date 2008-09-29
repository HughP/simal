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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
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
import org.xml.sax.SAXException;

import com.hp.hpl.jena.sparql.vocabulary.DOAP;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.DC_11;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.SimalOntology;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.jena.SimalRepository;

/**
 * A set of RDF utils for working with RDF data. Typically we will call
 * preProcess(url, baseURL, repository) in order to clean up RDF data from other
 * sources and to ensure that the maximum data is available to us.
 * 
 */
public class RDFUtils {
  public static final String PROJECT_NAMESPACE_URI = "http://simal.oss-watch.ac.uk/doap/";
  public static final String PERSON_NAMESPACE_URI = "http://simal.oss-watch.ac.uk/foaf/";
  public static final String CATEGORY_NAMESPACE_URI = "http://simal.oss-watch.ac.uk/defaultCategoryNS#";

  private static final String SIMAL_CATEGORY_ID = "categoryId";
  private static final String SIMAL_PERSON_ID = "personId";
  private static final String SIMAL_PROJECT_ID = "projectId";

  private static final Logger logger = LoggerFactory.getLogger(RDFUtils.class);

  public static final String DOAP_NS = DOAP.NS;
  public static final String DC_NS = DC.NS;
  public static final String FOAF_NS = FOAF.NS;
  public static final String RDF_NS = RDF.getURI();
  public static final String RDFS_NS = RDFS.getURI();
  private static File lastFile;

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
  private static void removeBNodes(final Document doc) throws DOMException,
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
   * Prepare the file at the supplied URL for addition to the Simal repository.
   * Each individual doap:Project element found within the file is separated
   * out. Each file is then processed to do useful things like remove blank
   * nodes, merge duplicates, filtering content etc. The result is a set of
   * files ready for importation into a Simal Repository.
   * 
   * @param url
   * @param baseURI
   * @param repo
   *          the repository that we are preaparing the data for
   * @return
   * @throws URISyntaxException
   * @throws IOException
   * @throws ISimalRepositoryException
   * @refactor there is no need for the baseURI
   */
  public static Set<File> preProcess(URL url, String baseURI,
      ISimalRepository repo) throws SimalRepositoryException {
    Set<File> annotatedFiles = new HashSet<File>();
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      Document originalDoc = null;
      Document doc = null;

      DocumentBuilder db = dbf.newDocumentBuilder();
      originalDoc = db.parse(url.openStream());

      // Strip any extra XML, such as Atom feed data or web services response data
      NodeList projects = originalDoc
          .getElementsByTagNameNS(DOAP_NS, "Project");
      if (projects.getLength() == 0) {
        Element docElement = originalDoc.getDocumentElement();
        if (docElement.getLocalName().equals("RDF")) {
          doc = originalDoc;
        } else {
          doc = db.newDocument();
          Node root = doc.createElementNS(RDF_NS, "RDF");
          root.appendChild(doc.importNode(docElement, true));
          doc.appendChild(root);
        }

        File annotatedFile = writeAnnotatedFile(url, doc);
        annotatedFiles.add(annotatedFile);
        lastFile = annotatedFile;
      } else {
        for (int i = 0; i < projects.getLength(); i = i + 1) {
          Node project = projects.item(i);

          File annotatedFile = preProcess(project, url, baseURI, repo);
          annotatedFiles.add(annotatedFile);
          lastFile = annotatedFile;
        }
      }
    } catch (SAXException e) {
      throw new SimalRepositoryException("Unable to prepare data from "
          + url.toExternalForm() + " for adding to the repository", e);
    } catch (FileNotFoundException e) {
      throw new SimalRepositoryException("Unable to prepare data from "
          + url.toExternalForm() + " for adding to the repository", e);
    } catch (IOException e) {
      throw new SimalRepositoryException("Unable to prepare data from "
          + url.toExternalForm() + " for adding to the repository", e);
    } catch (ParserConfigurationException e) {
      throw new SimalRepositoryException("Unable to prepare data from "
          + url.toExternalForm() + " for adding to the repository", e);
    }
    return annotatedFiles;
  }

  /**
   * Prepare a doap:Project node for addition to the Simal repository. Each
   * individual doap:Project element found within the file is separated out.
   * Each file is then processed to do useful things like remove blank nodes,
   * merge duplicates, filtering content etc. The result is a set of files ready
   * for importation into a Simal Repository.
   * 
   * @param project
   *          the doap:proejct node
   * @param sourceURL
   *          the URL from which this node was retrieved
   * @param baseURI
   * @param repo
   *          the repository that we are preparing the data for
   * @return
   * @throws SimalRepositoryException
   */
  public static File preProcess(Node project, URL sourceURL, String baseURI,
      ISimalRepository repo) throws SimalRepositoryException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    DocumentBuilder db;
    try {
      db = dbf.newDocumentBuilder();
      Document doc = db.newDocument();

      if (project == null || project.getNodeType() == Node.DOCUMENT_NODE) {
        throw new SimalRepositoryException(
            "The supplied node cannot be null and it cannot be the document element (is must be rdf:RDF or doap:Project)");
      }
      if (project.getLocalName().equals("RDF")) {
        project = project.getFirstChild();
        if (project == null) {
          throw new SimalRepositoryException(
              "The supplied node is an rdf:RDF node, but it does not contain a doap:Project node.");
        }
      }
      Node root = doc.createElementNS(RDF_NS, "RDF");
      root.appendChild(doc.importNode(project, true));
      doc.appendChild(root);

      // perform various checks on the document
      deDupeProjects(doc, repo);
      checkProjectIDs(doc, repo);

      checkPersonSHA1(doc);
      deDupePeople(doc, repo);
      checkPersonIDs(doc, repo);

      createSimalURIs(doc, repo);

      removeBNodes(doc);

      addProjectSeeAlso(doc, sourceURL);
      checkCategoryIDs(doc, repo);
      checkResources(doc);

      addProjectToPeople(doc);

      checkCDataSections(doc);

      checkHomePageNodes(doc);

      File annotatedFile = writeAnnotatedFile(sourceURL, doc);
      logger.debug("Written annotated file to "
          + annotatedFile.toURI().toURL().toString());
      return annotatedFile;
    } catch (FileNotFoundException e) {
      throw new SimalRepositoryException("Unable to prepare data from "
          + sourceURL.toExternalForm() + " for adding to the repository", e);
    } catch (IOException e) {
      throw new SimalRepositoryException("Unable to prepare data from "
          + sourceURL.toExternalForm() + " for adding to the repository", e);
    } catch (ParserConfigurationException e) {
      throw new SimalRepositoryException("Unable to prepare data from "
          + sourceURL.toExternalForm() + " for adding to the repository", e);
    }
  }

  /**
   * This method looks at all the homepage nodes in the document and attempts to
   * provide a useful label for each one.
   * 
   * @param doc
   */
  private static void checkHomePageNodes(Document doc) {
    NodeList nodes = doc.getElementsByTagNameNS(DOAP_NS, "homepage");
    for (int i = 0; i < nodes.getLength(); i++) {
      Element homepage = (Element) nodes.item(i);
      String uri = homepage.getAttributeNS(RDF_NS, "resource");
      String label = homepage.getAttributeNS(RDFS_NS, "label");
      if (label.length() == 0) {
        if (uri.startsWith("http://www.jisc.ac.uk/whatwedo")) {
          label = "JISC Project Page";
        } else if (uri.startsWith("http://code.google.com")) {
          label = "Google code site";
        } else if (uri.startsWith("http://www.sf.net")
            || uri.startsWith("http://www.sourceforge.net")
            || uri.startsWith("http://sourceforge.net")) {
          label = "Sourceforge site";
        } else if (uri.startsWith("http://www.ohloh.net")) {
          label = "Ohloh stats";
        } else {
          label = "Webpage";
        }
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
  private static void checkCDataSections(Document doc) {
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
   * Ensure that every resource that we wish to "own" the data for has a Simal
   * defined URI and that the original about URI is in a rdfs:seeAlso element.
   * 
   * @param doc
   * @param repo
   * @throws DOMException
   * @throws SimalRepositoryException
   */
  private static void createSimalURIs(Document doc, ISimalRepository repo)
      throws SimalRepositoryException, DOMException {
    String uri = null;

    // project URIs
    NodeList projects = doc.getElementsByTagNameNS(DOAP_NS, "Project");
    for (int i = 0; i < projects.getLength(); i++) {
      Element project = (Element) projects.item(i);
      String originalURI = project.getAttributeNS(RDF_NS, "about");
      if (!originalURI.startsWith(PROJECT_NAMESPACE_URI)) {
        addSeeAlso(project, originalURI);
      }
      NodeList simalIDs = project.getElementsByTagNameNS(SimalOntology.NS,
          SIMAL_PROJECT_ID);
      for (int idIdx = 0; idIdx < simalIDs.getLength(); idIdx++) {
        Node idNode = simalIDs.item(idIdx);
        if (idNode.getParentNode().equals(project)) {
          String id = repo.getEntityID(idNode.getTextContent());
          uri = getDefaultProjectURI(id);
          project.setAttributeNS(RDF_NS, "about", uri);
        }
      }
      logger.info("Project with URI '{}' given a local URI of {}",
          new String[] { originalURI, uri });
    }

    // people URIs
    NodeList people = doc.getElementsByTagNameNS(FOAF_NS, "Person");
    for (int i = 0; i < people.getLength(); i++) {
      Element person = (Element) people.item(i);
      String originalURI = person.getAttributeNS(RDF_NS, "about");
      if (!originalURI.startsWith(PERSON_NAMESPACE_URI)) {
        addSeeAlso(person, originalURI);
      }
      NodeList idNL = person.getElementsByTagNameNS(SimalOntology.NS, SIMAL_PERSON_ID);
      for (int idIdx = 0; idIdx < idNL.getLength(); idIdx++) {
        Node idNode = idNL.item(idIdx);
        if (idNode.getParentNode().equals(person)) {
          String id = repo.getEntityID(idNode.getTextContent());
          uri = getDefaultPersonURI(id);
          person.setAttributeNS(RDF_NS, "about", uri);
        }
      }
      logger.info("Person with URI '{}' given a local URI of {}", new String[] {
          originalURI, uri });
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
    Element seeAlso = node.getOwnerDocument().createElementNS(RDFS_NS,
        "seeAlso");
    seeAlso.setAttributeNS(RDF_NS, "resource", uri);
    node.appendChild(seeAlso);
    logger.debug("Added rdfs:seeAlso=\"{}\"", uri);
  }

  /**
   * Write the annotated copy of the source file to disk.
   * 
   * @param sourceURL
   * @param doc
   * @return
   * @throws FileNotFoundException
   * @throws IOException
   */
  private static File writeAnnotatedFile(URL sourceURL, Document doc)
      throws FileNotFoundException, IOException {
    File annotatedFile;
    try {
      annotatedFile = RDFUtils.getAnnotatedFile(sourceURL);
    } catch (SimalRepositoryException e) {
      throw new IOException("Unable to annotate source file: " + e.getMessage());
    }

    OutputFormat format = new OutputFormat(doc);
    format.setIndenting(false);
    XMLSerializer serializer = new XMLSerializer(new FileOutputStream(
        annotatedFile), format);
    serializer.setNamespaces(true);
    serializer.serialize(doc);
    return annotatedFile;
  }

  /**
   * Adds the appropriatecurrentProject tuple to each person
   * 
   * @param doc
   * @param repo
   */
  private static void addProjectToPeople(Document doc) {
    NodeList people = doc.getElementsByTagNameNS(FOAF_NS, "Person");
    for (int iper = 0; iper < people.getLength(); iper = iper + 1) {
      Node person = people.item(iper);
      Node grandparent = person.getParentNode().getParentNode();
      if (grandparent.getNamespaceURI().equals(DOAP_NS)
          && grandparent.getLocalName().equals("Project")) {
        String uri = grandparent.getAttributes()
            .getNamedItemNS(RDF_NS, "about").getNodeValue();
        Element projNode = doc.createElementNS(FOAF_NS, "currentProject");
        projNode.setAttributeNS(RDF_NS, "resource", uri);
        person.appendChild(projNode);
      }
    }
  }

  /**
   * Check that resources are correctly defined.
   * 
   * @param doc
   * @param repo
   */
  private static void checkResources(Document doc) {
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
  private static void validateResourceDefinition(NodeList nodes) {
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
  private static void checkPersonSHA1(Document doc)
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
   * Add a seeAlso element to the project that refers to the original source.
   * 
   * @param doc
   * @param sourceURL
   * @param repo
   */
  private static void addProjectSeeAlso(Document doc, URL sourceURL) {
    NodeList projects = doc.getElementsByTagNameNS(DOAP_NS, "Project");
    for (int i = 0; i < projects.getLength(); i++) {
      Node project = projects.item(i);
      addSeeAlso(project, sourceURL.toExternalForm());
    }
  }

  /**
   * Check that all categories have an ID associated with them.
   * 
   * @param doc
   * @param repo
   * @throws ISimalRepositoryException
   */
  private static void checkCategoryIDs(Document doc, ISimalRepository repo)
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
          simalCategory = repo.getCategory(att.getNodeValue().trim());
          if (simalCategory != null) {
            id = simalCategory.getSimalID();
          } else {
            id = repo.getNewPersonID();
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
   * Check that all people elements have an ID for this instance of Simal
   * associated with them. If there are IDs for other instances of Simal then ad
   * rdf:seeAlso nodes.
   * 
   * @param doc
   * @param repo
   * @throws ISimalRepositoryException
   */
  private static void checkPersonIDs(Document doc, ISimalRepository repo)
      throws SimalRepositoryException {
    NodeList people = doc.getElementsByTagNameNS(FOAF_NS, "Person");
    Element person;
    NodeList simalIDNL;
    String simalPersonID = null;
    Node simalIDNode;
    for (int i = 0; i < people.getLength(); i = i + 1) {
      person = (Element) people.item(i);

      // if this is a resource reference and the resource doesn't
      // yet exist in the repository then create the resource and
      // give it an iD
      String resourceURI = person.getAttributeNS(RDF_NS, "resource").trim();
      if (resourceURI.length() > 0) {
        IPerson existingPerson = SimalRepository.getInstance()
            .findPersonBySeeAlso(resourceURI);
        if (existingPerson == null) {
          simalPersonID = SimalRepository.getInstance().getNewPersonID();
          simalIDNode = doc.createElementNS(SimalOntology.NS, SIMAL_PERSON_ID);
          Attr attr = doc.createAttributeNS(ISimalRepository.RDF_NAMESPACE_URI,
              "datatype");
          attr.setNodeValue(ISimalRepository.XSD_NAMESPACE_URI + "string");
          ((Element) simalIDNode).setAttributeNodeNS(attr);

          Node text = doc.createTextNode(simalPersonID);
          simalIDNode.appendChild(text);
          person.appendChild(simalIDNode);

          person.removeAttributeNS(RDF_NS, "resource");
          addSeeAlso(person, resourceURI);
        }
      }

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

      // check there is a valid simal ID for this person on this instance of
      // Simal
      simalIDNL = person.getElementsByTagNameNS(SimalOntology.NS, SIMAL_PERSON_ID);
      if (simalIDNL.getLength() == 0) {
        IPerson simalPerson;
        try {
          Attr about = person.getAttributeNodeNS(RDF_NS, "about");
          if (about != null && !about.getTextContent().equals("")) {
            String uri = about.getTextContent();
            simalPerson = repo.getPerson(uri);
            if (simalPerson != null) {
              simalPersonID = simalPerson.getSimalID();
            } else {
              simalPersonID = repo.getNewPersonID();
            }
          } else {
            // we've already checked for duplicates and, where found, we
            // have given the node an rdf:about, so if there still isn't
            // one this is a new person and therefore needs a Simal ID
            simalPersonID = repo.getNewPersonID();
            if (about != null && !about.getTextContent().equals("")) {
              String[] params = { simalPersonID, about.getTextContent() };
              logger.info("Assigning Simal ID of {} to {}", params);
            }
          }
        } catch (Exception e) {
          throw new SimalRepositoryException("Unable to get person", e);
        }
        simalIDNode = doc.createElementNS(SimalOntology.NS, SIMAL_PERSON_ID);
        Attr attr = doc.createAttributeNS(ISimalRepository.RDF_NAMESPACE_URI,
            "datatype");
        attr.setNodeValue(ISimalRepository.XSD_NAMESPACE_URI + "string");
        ((Element) simalIDNode).setAttributeNodeNS(attr);

        Node text;
        if (repo.isUniqueSimalID(simalPersonID)) {
          text = doc.createTextNode(simalPersonID);
        } else {
          text = doc.createTextNode(repo.getUniqueSimalID(simalPersonID));
        }
        simalIDNode.appendChild(text);
        person.appendChild(simalIDNode);
      }
    }
  }

  /**
   * Check that the project element has an ID associated with this instance of
   * Simal associated with it. If there are IDs for other instances of Simal
   * then ad rdf:seeAlso nodes.
   * 
   * @param doc
   * @param repo
   * @throws ISimalRepositoryException
   */
  private static void checkProjectIDs(Document doc, ISimalRepository repo)
      throws SimalRepositoryException {
    NodeList projects = doc.getElementsByTagNameNS(DOAP_NS, "Project");
    Element project;
    NodeList simalIDNL;
    String projectID = null;
    Node simalIDNode;

    for (int i = 0; i < projects.getLength(); i = i + 1) {
      project = (Element) projects.item(i);

      // if this is a resource reference and the resource doesn't
      // yet exist in the repository then create the resource and
      // give it an iD
      String resourceURI = project.getAttributeNS(RDF_NS, "resource").trim();
      if (resourceURI.length() > 0) {
        IProject existingProject = SimalRepository.getInstance()
            .findProjectBySeeAlso(resourceURI);
        if (existingProject == null) {
          projectID = SimalRepository.getInstance().getNewProjectID();
          simalIDNode = doc.createElementNS(SimalOntology.NS, SIMAL_PROJECT_ID);
          Attr attr = doc.createAttributeNS(ISimalRepository.RDF_NAMESPACE_URI,
              "datatype");
          attr.setNodeValue(ISimalRepository.XSD_NAMESPACE_URI + "string");
          ((Element) simalIDNode).setAttributeNodeNS(attr);

          Node text = doc.createTextNode(projectID);
          simalIDNode.appendChild(text);
          project.appendChild(simalIDNode);

          project.removeAttributeNS(RDF_NS, "resource");
          addSeeAlso(project, resourceURI);
        }
      }

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

      // check there is a valid simal ID for this project on this instance of
      // Simal
      simalIDNL = project.getElementsByTagNameNS(SimalOntology.NS, SIMAL_PROJECT_ID);
      if (simalIDNL.getLength() == 0) {
        IProject simalProject;
        try {
          Attr about = project.getAttributeNodeNS(RDF_NS, "about");
          if (about != null && !about.getTextContent().equals("")) {
            String uri = about.getTextContent();
            simalProject = repo.getProject(uri);
            if (simalProject != null) {
              projectID = simalProject.getSimalID();
            } else {
              projectID = repo.getNewProjectID();
            }
          } else {
            // we've already checked for duplicates and, where found, we
            // have given the node an rdf:about, so if there still isn't
            // one this is a new person and therefore needs a Simal ID
            projectID = repo.getNewProjectID();
            if (about != null && !about.getTextContent().equals("")) {
              String[] params = { projectID, about.getTextContent() };
              logger.info("Assigning Simal ID of {} to {}", params);
            }
          }
        } catch (Exception e) {
          throw new SimalRepositoryException("Unable to get project", e);
        }
        simalIDNode = doc.createElementNS(SimalOntology.NS, SIMAL_PROJECT_ID);
        Attr attr = doc.createAttributeNS(ISimalRepository.RDF_NAMESPACE_URI,
            "datatype");
        attr.setNodeValue(ISimalRepository.XSD_NAMESPACE_URI + "string");
        ((Element) simalIDNode).setAttributeNodeNS(attr);

        Node text;
        if (repo.isUniqueSimalID(projectID)) {
          text = doc.createTextNode(projectID);
        } else {
          text = doc.createTextNode(repo.getUniqueSimalID(projectID));
        }
        simalIDNode.appendChild(text);
        project.appendChild(simalIDNode);
      }
    }
  }

  /**
   * Look for duplicate people and, where they are found, replace the QName with
   * that already present in the repository. This has the effect of merging
   * people records.
   * 
   * @param doc
   *          an XML document representing the RDF data
   * @param repo
   * @return
   * @throws ISimalRepositoryException
   * @throws DOMException
   */
  private static void deDupePeople(Document doc, ISimalRepository repo)
      throws DOMException, SimalRepositoryException {
    // handle duplicate people identified by their mbox_sha1sum
    NodeList sha1sums = doc.getElementsByTagNameNS(FOAF_NS, "mbox_sha1sum");
    for (int i = 0; i < sha1sums.getLength(); i = i + 1) {
      Element sha1sumNode = (Element) sha1sums.item(i);
      String sha1Sum = sha1sumNode.getFirstChild().getNodeValue().trim();
      IPerson person = repo.findPersonBySha1Sum(sha1Sum);
      if (person != null) {
        logger.info(
            "Merging duplicate person based on email SHA1 of {} into {}",
            new String[] { person.toString(), person.getURI() });
        Element personNode = (Element) sha1sumNode.getParentNode();
        personNode.setAttributeNS(RDF_NS, "about", person.getURI());
      }

      // Check there are no duplicates in the same file
      for (int idx = 0; idx < sha1sums.getLength(); idx = idx + 1) {
        String thisSum = sha1sums.item(idx).getFirstChild().getNodeValue()
            .trim();
        if (i != idx && sha1sumNode.getTextContent().equals(thisSum)) {
          logger
              .info(
                  "Merging duplicate person found in source file based on email SHA1 of {}",
                  thisSum);
          Element personNode = (Element) sha1sums.item(idx).getParentNode();
          personNode.setAttributeNS(RDF_NS, "about", sha1sumNode
              .getAttributeNS(RDF_NS, "about"));
        }
      }
    }

    NodeList people = doc.getElementsByTagNameNS(FOAF_NS, "Person");
    for (int personIdx = 0; personIdx < people.getLength(); personIdx++) {
      Element personNode = (Element) people.item(personIdx);

      // handle duplicate people identified by their rdf:about
      String aboutURI = personNode.getAttributeNS(RDF_NS, "about");
      if (!aboutURI.equals("")) {
        IPerson person = repo.findPersonBySeeAlso(aboutURI);
        if (person != null) {
          logger.info(
              "Merging duplicate person based on rdf:about of {} into {}",
              new String[] { aboutURI, person.getURI() });
          personNode.setAttributeNS(RDF_NS, "about", person.getURI());
        }
      }

      // handle duplicate people identified by their rdfs:seeAlso
      NodeList seeAlsos = personNode.getElementsByTagNameNS(RDFS_NS, "seeAlso");
      Element seeAlso;
      for (int seeAlsoIdx = 0; seeAlsoIdx < seeAlsos.getLength(); seeAlsoIdx = seeAlsoIdx + 1) {
        seeAlso = (Element) seeAlsos.item(seeAlsoIdx);
        String uri = seeAlso.getAttributeNS(RDF_NS, "resource").trim();
        IPerson person = repo.findPersonBySeeAlso(uri);
        if (person != null) {
          String[] params = { uri, person.getURI() };
          logger.info(
              "Merging duplicate person based on rdfs:seeAlso of {} into {}",
              params);
          personNode.setAttributeNS(RDF_NS, "about", person.getURI());
        }

        // Handle duplicates of the same person in the same RDF file
        NodeList allSeeAlsos = doc.getElementsByTagNameNS(RDFS_NS, "seeAlso");
        for (int idx = 0; idx < allSeeAlsos.getLength(); idx = idx + 1) {
          Element thisSeeAlso = (Element) allSeeAlsos.item(idx);
          String thisURI = thisSeeAlso.getAttributeNS(RDF_NS, "resource")
              .trim();
          if (!thisSeeAlso.equals(seeAlso) && uri.equals(thisURI)) {
            logger
                .info(
                    "Merging duplicate person in source file based on rdfs:seeAlso of {}",
                    thisURI);
            Node otherPerson = thisSeeAlso.getParentNode();
            NodeList children = otherPerson.getChildNodes();
            for (int childIdx = 0; childIdx < children.getLength(); childIdx++) {
              Node child = children.item(childIdx);
              otherPerson.removeChild(child);
              personNode.appendChild(child);
            }
            otherPerson.getParentNode().getParentNode().removeChild(
                otherPerson.getParentNode());
          }
        }
      }
    }
  }

  /**
   * Look for duplicate projects and, where they are found, replace the QName
   * with that already present in the repository. This has the effect of merging
   * project records.
   * 
   * @param doc
   *          an XML document representing the RDF data
   * @param repo
   * @return
   * @throws ISimalRepositoryException
   * @throws DOMException
   */
  private static void deDupeProjects(Document doc, ISimalRepository repo)
      throws DOMException, SimalRepositoryException {
    logger.debug("deDupeProjects found in RDF file");
    // handle duplicate projects identified by their homepage
    NodeList homepages = doc.getElementsByTagNameNS(DOAP_NS, "homepage");
    Element homepage;
    for (int i = 0; i < homepages.getLength(); i = i + 1) {
      homepage = (Element) homepages.item(i);
      IProject project = repo.findProjectByHomepage(homepage.getAttributeNS(
          RDF_NS, "resource").trim());
      if (project != null) {
        logger.info("Merging duplicate project (based on homepage): "
            + project.toString() + " into " + project.getURI());
        Element projectNode = (Element) homepage.getParentNode();
        projectNode.setAttributeNS(RDF_NS, "about", project.getURI());
      }
    }

    // handle duplicate projects identified by their rdf:seeAlso
    NodeList seeAlsos = doc.getElementsByTagNameNS(RDFS_NS, "seeAlso");
    Element seeAlso;
    for (int i = 0; i < seeAlsos.getLength(); i = i + 1) {
      seeAlso = (Element) seeAlsos.item(i);
      String uri = seeAlso.getAttributeNS(RDF_NS, "resource").trim();
      IProject project = repo.findProjectBySeeAlso(uri);
      if (project != null) {
        logger.info("Merging duplicate project (based on seeAlso): "
            + project.toString() + " into " + project.getURI());
        Element projectNode = (Element) seeAlso.getParentNode();
        projectNode.setAttributeNS(RDF_NS, "about", project.getURI());
      }
    }

    // handle duplicates with supplied URI
    NodeList projects = doc.getElementsByTagNameNS(DOAP_NS, "Project");
    for (int i = 0; i < projects.getLength(); i = i + 1) {
      Element projectElement = (Element) projects.item(i);
      String uri = projectElement.getAttributeNS(RDF_NS, "about");
      IProject project = repo.findProjectBySeeAlso(uri);
      if (project != null) {
        logger.info("Merging duplicate project (based on rdf:about): "
            + project.toString() + " into " + project.getURI());
        projectElement.setAttributeNS(RDF_NS, "about", project.getURI());
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
          "Unable to create the filesrote for annotated files");
    }
    fileStoreDir.mkdirs();
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

  /**
   * Get the File that contains the most recently processed data. Note that this
   * is primarily to allow testing, it cannot be relied upon in product because
   * when a source file has multiple projects within it that file will be split
   * into multiple local files. Thus this method will only return the File for
   * the last Project in the original source file.
   * 
   * @return
   */
  public static File getLastProcessedFile() {
    return lastFile;
  }
}