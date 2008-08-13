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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.SimalOntology;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * A set of RDF utils for working with RDF data.
 * 
 */
public class RDFUtils {
  private static final String SIMAL_CATEGORY_ID = "categoryId";
  private static final String SIMAL_PERSON_ID = "personId";
  private static final String SIMAL_PROJECT_ID = "projectId";

  private static final Logger logger = LoggerFactory.getLogger(RDFUtils.class);

  public static final String DOAP_NS = "http://usefulinc.com/ns/doap#";
  public static final String FOAF_NS = "http://xmlns.com/foaf/0.1/";
  public static final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
  public static final String RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#";
  public static final String SIMAL_NS = "http://simal.oss-watch.ac.uk/ns/0.2/simal#";
  private static File lastFile;

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
   * @throws DOMException 
   * @throws UnsupportedEncodingException 
   */
  private static void removeBNodes(Document doc, ISimalRepository repo)
      throws DOMException, UnsupportedEncodingException {

    NodeList nl = doc.getElementsByTagNameNS(DOAP_NS, "Project");
    removeBlankProjectNodes(nl);

    nl = doc.getElementsByTagNameNS(DOAP_NS, "Repository");
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

    nl = doc.getElementsByTagNameNS(FOAF_NS, "Person");
    removeBlankPersonNodes(nl);
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
  private static void removeBlankRepositoryNodes(NodeList nl) throws DOMException, UnsupportedEncodingException {
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
              uri = uri.substring(uri.indexOf("@") + 1);
              uri = "cvs:" + uri.substring(0, uri.indexOf(":")) + uri.substring(uri.indexOf(":") + 1);
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
   * @param el
   * @return
   */
  private static Element getProjectElement(Element el) {
    Node node = el.getParentNode();
    if (node.getLocalName().equals("Project") && node.getNamespaceURI().equals(DOAP_NS)) {
        return (Element)node;
    } else {
        return getProjectElement((Element)node);
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
   * @throws UnsupportedEncodingException 
   */
  private static void removeBlankProjectNodes(NodeList nl) throws DOMException, UnsupportedEncodingException {
    if (nl != null && nl.getLength() > 0) {
      for (int i = 0; i < nl.getLength(); i++) {
        Element el = (Element) nl.item(i);
        if (!el.hasAttributeNS(RDF_NS, "about")
            || el.getAttributeNodeNS(RDF_NS, "about").getValue().equals("")) {
          String uri = ISimalRepository.DEFAULT_PROJECT_NAMESPACE_URI;
          Node nameNode = el.getElementsByTagNameNS(DOAP_NS, "name").item(0);
          uri = uri + nameNode.getFirstChild().getNodeValue();
          uri = uri + "#Project";
          el.setAttributeNS(RDF_NS, "rdf:about", encode(uri));
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
   * @throws UnsupportedEncodingException 
   */
  private static void removeBlankPersonNodes(NodeList nl) throws DOMException, UnsupportedEncodingException {
    if (nl != null && nl.getLength() > 0) {
      for (int i = 0; i < nl.getLength(); i++) {
        Element el = (Element) nl.item(i);
        if (!el.hasAttributeNS(RDF_NS, "about")) {
          String name = null;
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
          String uri = ISimalRepository.DEFAULT_PERSON_NAMESPACE_URI;
          uri = uri + name;
          uri = uri + "#Person";
          el.setAttributeNS(RDF_NS, "rdf:about", encode(uri));
        }

      }
    }
  }

  /**
   * Encode the given string so that it can be used to create a valid URI.
   * FOr example, encode ' ' as %20
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
  private static void removeBlankVersionNodes(NodeList nl) throws DOMException, UnsupportedEncodingException {
    if (nl != null && nl.getLength() > 0) {
      for (int i = 0; i < nl.getLength(); i++) {
        Element el = (Element) nl.item(i);
        if (!el.hasAttributeNS(RDF_NS, "about")) {
          String uri = ISimalRepository.DEFAULT_PROJECT_NAMESPACE_URI;
          Node nameNode = el.getElementsByTagNameNS(DOAP_NS, "name").item(0);
          uri = uri + nameNode.getFirstChild().getNodeValue();
          Node revisionNode = el.getElementsByTagNameNS(DOAP_NS, "revision")
              .item(0);
          uri = uri + "/" + revisionNode.getFirstChild().getNodeValue();
          uri = uri + "#Version";
          el.setAttributeNS(RDF_NS, "rdf:about", encode(uri));
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

      // Strip any extra XML, such as Atom feed data
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
          doc = db.newDocument();
          Node root = doc.createElementNS(RDF_NS, "RDF");
          Node project = projects.item(i);
          root.appendChild(doc.importNode(project, true));
          doc.appendChild(root);

          // perform various checks on the document
          removeBNodes(doc, repo);
          deDupeProjects(doc, repo);
          checkProjectID(doc, repo);
          checkProjectSeeAlso(doc, url, repo);
          checkCategoryIDs(doc, repo);

          deDupePeople(doc, repo);
          checkPersonIDs(doc, repo);
          checkPersonSHA1(doc, repo);
          checkResources(doc, repo);
          
          addProjectToPeople(doc, repo);
          
          escapeContent(doc, repo);
          
          File annotatedFile = writeAnnotatedFile(url, doc);
          annotatedFiles.add(annotatedFile);
          lastFile = annotatedFile;
        }
      }
    } catch (Exception e) {
      throw new SimalRepositoryException("Unable to prepare data from "
          + url.toExternalForm() + " for adding to the repository", e);
    }
    return annotatedFiles;
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
    File annotatedFile = RDFUtils.getAnnotatedFile(sourceURL);

    OutputFormat format = new OutputFormat(doc);
    format.setIndenting(false);
    XMLSerializer serializer = new XMLSerializer(new FileOutputStream(
        annotatedFile), format);
    serializer.setNamespaces(true);
    serializer.serialize(doc);
    return annotatedFile;
  }

  /**
   * Ensures that content that may contain HTML, such as doap:description is
   * correctly escaped.
   * 
   * @param doc
   * @param repo
   * @throws ISimalRepositoryException
   */
  private static void escapeContent(Document doc, ISimalRepository repo)
      throws SimalRepositoryException {
    NodeList descriptions = doc.getElementsByTagNameNS(DOAP_NS, "description");
    Element description;
    for (int i = 0; i < descriptions.getLength(); i = i + 1) {
      description = (Element) descriptions.item(i);
      NodeList nodes = description.getChildNodes();
      for (int ni = 0; ni < nodes.getLength(); ni = ni + 1) {
        Node node = nodes.item(ni);
        String escapedDescription = "";
        if (node.getNodeType() == Node.TEXT_NODE) {
          escapedDescription = escapedDescription + node.getNodeValue();
        } else if (node.getNodeType() == Node.ELEMENT_NODE) {
          if (!node.hasChildNodes()) {
            escapedDescription = escapedDescription + "<" + node.getNodeName()
                + " />";
          } else {
            throw new SimalRepositoryException(
                "Unable to handle non-empty nodes in description");
          }
        }
        description.replaceChild(doc.createTextNode(escapedDescription), node);
      }
    }
  }

  /**
   * Adds a 
   * @param doc
   * @param repo
   */
  private static void addProjectToPeople(Document doc, ISimalRepository repo) {
    NodeList people = doc.getElementsByTagNameNS(FOAF_NS, "Person");
    for (int iper = 0; iper < people.getLength(); iper = iper + 1) {
      Node person = people.item(iper);
      NodeList projects = doc.getElementsByTagNameNS(DOAP_NS, "Project");
      for (int iproj = 0; iproj < projects.getLength(); iproj = iproj + 1) {
        String uri = projects.item(iproj).getAttributes().getNamedItemNS(RDF_NS, "about").getNodeValue();
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
  private static void checkResources(Document doc, ISimalRepository repo) {
    logger.debug("Check resources found in RDF file");
    validateResourceDefinition(doc.getElementsByTagNameNS(DOAP_NS, "bug-database"));
    validateResourceDefinition(doc.getElementsByTagNameNS(DOAP_NS, "download-page"));
    validateResourceDefinition(doc.getElementsByTagNameNS(FOAF_NS, "homepage"));
    validateResourceDefinition(doc.getElementsByTagNameNS(DOAP_NS, "license"));
    validateResourceDefinition(doc.getElementsByTagNameNS(DOAP_NS, "mailing-list"));
    validateResourceDefinition(doc.getElementsByTagNameNS(DOAP_NS, "wiki"));
  }

  /**
   * Checks to see if resources are correctly identified. If not then
   * drop the element.
   * 
   * @TODO This is a bit draconian, we should be looking to see if the
   * DOAP is incorrectly encoded (i.e. provides a URI as content) and try 
   * and correct things rather than just dumping them.
   * 
   * @param nodes
   */
    private static void validateResourceDefinition(NodeList nodes) {
        Element el;
        for (int i = 0; i < nodes.getLength(); i = i + 1) {
          el = (Element) nodes.item(i);
          Attr resource = el.getAttributeNodeNS(RDF_NS, "resource");
          if (resource == null || resource.getValue().equals("")) {
            el.getParentNode().removeChild(el);
          }
        }
    }

  /**
   * For every mailbox associated with a person create an sha1 checksum.
   * 
   * @param doc
   * @param repo
   * @throws ISimalRepositoryException
   */
  private static void checkPersonSHA1(Document doc, ISimalRepository repo)
      throws SimalRepositoryException {
    logger.debug("Check person SHA1 in RDF file");
    NodeList people = doc.getElementsByTagNameNS(FOAF_NS, "Person");
    Element person;
    for (int i = 0; i < people.getLength(); i = i + 1) {
      person = (Element) people.item(i);
      NodeList emailNL = person.getElementsByTagNameNS(FOAF_NS, "mbox");
      for (int i1 = 0; i1 < emailNL.getLength(); i1 = i1 + 1) {
        Element emailEl = (Element) emailNL.item(i1);
        String mbox = emailEl.getAttributeNS(RDF_NS, "resource");
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
    MessageDigest md = MessageDigest.getInstance("SHA");
    StringBuffer sb = new StringBuffer();

    md.update(data.getBytes());
    byte[] digest = md.digest();
    for (int i = 0; i < digest.length; i++) {
      String hex = Integer.toHexString(digest[i]);
      if (hex.length() == 1)
        hex = "0" + hex;
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
  private static void checkProjectSeeAlso(Document doc, URL sourceURL,
      ISimalRepository repo) {
    logger.debug("Check project see also in RDF file");
    NodeList projects = doc.getElementsByTagNameNS(DOAP_NS, "Project");
    if (projects.getLength() > 0) {
      Node project = projects.item(0);
      Element seeAlso = doc.createElementNS(RDFS_NS, "seeAlso");
      seeAlso.setAttributeNS(RDF_NS, "resource", sourceURL.toExternalForm());
      project.appendChild(seeAlso);
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
    Attr  simalIDAtt;
    String id = null;
    for (int i = 0; i < categories.getLength(); i = i + 1) {
      category = (Element) categories.item(i);
      simalIDAtt = category.getAttributeNodeNS(SimalOntology.NS, SIMAL_CATEGORY_ID);
      if (simalIDAtt == null) {
        IDoapCategory simalCategory;
        try {
          Attr att = category.getAttributeNodeNS(RDF_NS, "resource");
          simalCategory = repo.getCategory(att.getNodeValue());
          if (simalCategory != null) {
            id = simalCategory.getSimalID();
          } else {
            id = repo.getNewPersonID();
          }
          simalIDAtt = doc.createAttributeNS(SIMAL_NS, SIMAL_CATEGORY_ID);
          simalIDAtt.setValue(id);
          category.setAttributeNode(simalIDAtt);
        } catch (Exception e) {
          throw new SimalRepositoryException("Unable to get category", e);
        }
      }
    }
  }

  /**
   * Check that all people elements have an ID associated with them.
   * 
   * @param doc
   * @param repo
   * @throws ISimalRepositoryException
   */
  private static void checkPersonIDs(Document doc, ISimalRepository repo)
      throws SimalRepositoryException {
    logger.debug("Check person IDs in RDF file");
    NodeList people = doc.getElementsByTagNameNS(FOAF_NS, "Person");
    Element person;
    NodeList simalIDNL;
    String id = null;
    Node simalIDNode;
    for (int i = 0; i < people.getLength(); i = i + 1) {
      person = (Element) people.item(i);
      simalIDNL = person.getElementsByTagNameNS(SIMAL_NS, SIMAL_PERSON_ID);
      if (simalIDNL.getLength() == 0) {
        IPerson simalPerson;
        try {
          simalPerson = repo.getPerson(person.getAttributeNodeNS(RDF_NS, "about").getNodeValue());
        } catch (Exception e) {
          throw new SimalRepositoryException("Unable to get person", e);
        }
        if (simalPerson != null) {
          id = simalPerson.getSimalID();
        } else {
          id = repo.getNewPersonID();
        }
        simalIDNode = doc.createElementNS(SIMAL_NS, SIMAL_PERSON_ID);
        Node text = doc.createTextNode(id);
        simalIDNode.appendChild(text);
        person.appendChild(simalIDNode);
      }
    }
  }

  /**
   * Check that the project element has an ID associated with them.
   * 
   * @param doc
   * @param repo
   * @throws ISimalRepositoryException
   */
  private static void checkProjectID(Document doc, ISimalRepository repo)
      throws SimalRepositoryException {
    logger.debug("Check project IDs in RDF file");
    NodeList projects = doc.getElementsByTagNameNS(DOAP_NS, "Project");
    Element project;
    NodeList simalIDNL;
    String id = null;
    Node simalIDNode;
    for (int i = 0; i < projects.getLength(); i = i + 1) {
      project = (Element) projects.item(i);
      simalIDNL = project.getElementsByTagNameNS(SIMAL_NS, SIMAL_PROJECT_ID);
      if (simalIDNL.getLength() == 0) {
        IProject simalProject;
        try {
          String uri = project
          .getAttributeNodeNS(RDF_NS, "about").getNodeValue();
          if (repo.containsProject(uri)) {
            // FIXME: it would be more efficient to just get the statement we want
            simalProject = repo.getProject(uri);
            id = simalProject.getSimalID();
          } else {
            id = repo.getNewProjectID();
          }
        } catch (Exception e) {
          throw new SimalRepositoryException("Unable to create URI for project", e);
        }
        simalIDNode = doc.createElementNS(SIMAL_NS, SIMAL_PROJECT_ID);
        Node text = doc.createTextNode(id);
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
    logger.debug("deDupePeople found in RDF file");
    // handle duplicate people identified by their mbox_sha1sum
    NodeList sha1sums = doc.getElementsByTagNameNS(FOAF_NS, "mbox_sha1sum");
    Node sha1sum;
    for (int i = 0; i < sha1sums.getLength(); i = i + 1) {
      sha1sum = sha1sums.item(i);
      IPerson person = repo.findPersonBySha1Sum(sha1sum.getFirstChild()
          .getNodeValue().trim());
      if (person != null) {
        logger.info("Merging duplicate person (based on email SHA1): "
            + person.toString() + " into " + person.getURI());
        Element personNode = (Element) sha1sum.getParentNode();
        personNode.setAttributeNS(RDF_NS, "about", person.getURI().toString());
      }
    }

    // handle duplicate people identified by their rdf:seeAlso
    NodeList seeAlsos = doc.getElementsByTagNameNS(RDFS_NS, "seeAlso");
    Element seeAlso;
    for (int i = 0; i < seeAlsos.getLength(); i = i + 1) {
      seeAlso = (Element) seeAlsos.item(i);
      IPerson person = repo.findPersonBySeeAlso(seeAlso.getAttributeNS(RDF_NS,
          "resource"));
      if (person != null) {
        logger.info("Merging duplicate person (based on seeAlso): "
            + person.toString() + " into " + person.getURI());
        Element personNode = (Element) seeAlso.getParentNode();
        personNode.setAttributeNS(RDF_NS, "about", person.getURI().toString());
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
          RDF_NS, "resource"));
      if (project != null) {
        logger.info("Merging duplicate project (based on homepage): "
            + project.toString() + " into " + project.getURI());
        Element projectNode = (Element) homepage.getParentNode();
        projectNode.setAttributeNS(RDF_NS, "about", project.getURI().toString());
      }
    }

    // handle duplicate projects identified by their rdf:seeAlso
    NodeList seeAlsos = doc.getElementsByTagNameNS(RDFS_NS, "seeAlso");
    Element seeAlso;
    for (int i = 0; i < seeAlsos.getLength(); i = i + 1) {
      seeAlso = (Element) seeAlsos.item(i);
      IProject project = repo.findProjectBySeeAlso(seeAlso.getAttributeNS(
          RDF_NS, "resource"));
      if (project != null) {
        logger.info("Merging duplicate project (based on seeAlso): "
            + project.toString() + " into " + project.getURI());
        Element projectNode = (Element) seeAlso.getParentNode();
        projectNode.setAttributeNS(RDF_NS, "about", project.getURI().toString());
      }
    }
  }

  /**
   * Get the File for the local, annotated version of the file with the given
   * name.
   * 
   * @return
   */
  public static File getAnnotatedDoapFile(String filename) {
    File fileStoreDir = new File(SimalProperties
        .getProperty(SimalProperties.PROPERTY_SIMAL_DOAP_FILE_STORE)
        + File.separator + "simal-uploads");
    fileStoreDir.mkdirs();
    String path = fileStoreDir.getAbsolutePath();
    if (!(filename.endsWith(".rdf") || filename.endsWith(".xml"))) {
      filename = filename + ".rdf";
    }
    File file = new File(path + File.separator + System.currentTimeMillis()
        + "_" + filename);
    return file;
  }

  /**
   * Get the File for the local, annotated version of the file located at the
   * the given URL.
   * 
   * @return
   */
  public static File getAnnotatedFile(URL url) {
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