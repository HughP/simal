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
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * A set of RDF utils for working with RDF data.
 * 
 */
public class RDFUtils {
  private static final String SIMAL_PERSON_ID = "personId";
  private static final String SIMAL_PROJECT_ID = "projectId";

  private static final Logger logger = LoggerFactory.getLogger(RDFUtils.class);

  public static final String DOAP_NS = "http://usefulinc.com/ns/doap#";
  public static final String FOAF_NS = "http://xmlns.com/foaf/0.1/";
  public static final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
  public static final String RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#";
  public static final String SIMAL_NS = "http://simal.oss-watch.ac.uk/ns/0.2/simal#";

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
  private static void removeBNodes(Document doc, SimalRepository repo)
      throws URISyntaxException {

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
            uri = "http://simal.oss-watch.ac.uk/"
                + getProjectName((Element) el.getParentNode()) + "#"
                + el.getLocalName();
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
          String uri = SimalRepository.DEFAULT_PROJECT_NAMESPACE_URI;
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
          String uri = SimalRepository.DEFAULT_PERSON_NAMESPACE_URI;
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
          String uri = SimalRepository.DEFAULT_PROJECT_NAMESPACE_URI;
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

  /**
   * Prepare the file at the supplied URL for addition to the Simal repository.
   * 
   * @param url
   * @param baseURI
   * @param repo
   *          the repository that we are preaparing the data for
   * @return
   * @throws URISyntaxException
   * @throws IOException
   * @throws SimalRepositoryException
   */
  public static File preProcess(URL url, String baseURI, SimalRepository repo)
      throws SimalRepositoryException {
    File annotatedFile = null;
    try {
      annotatedFile = RDFUtils.getAnnotatedDoapFile(url);
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      Document doc = null;

      DocumentBuilder db = dbf.newDocumentBuilder();
      doc = db.parse(url.openStream());

      RDFUtils.removeBNodes(doc, repo);
      RDFUtils.deDupePeople(doc, repo);
      RDFUtils.checkProjectID(doc, repo);
      RDFUtils.checkProjectSeeAlso(doc, url, repo);
      RDFUtils.checkPersonIDs(doc, repo);
      RDFUtils.checkPersonSHA1(doc, repo);
      RDFUtils.checkPersonNames(doc, repo);

      OutputFormat format = new OutputFormat(doc);
      format.setIndenting(false);
      XMLSerializer serializer = new XMLSerializer(new FileOutputStream(
          annotatedFile), format);
      serializer.setNamespaces(true);
      serializer.serialize(doc);
    } catch (Exception e) {
      throw new SimalRepositoryException(
          "Unable to prepare data for adding to the repository", e);
    } finally {
      annotatedFile.delete();
    }
    return annotatedFile;
  }

  /**
   * For every person convert foafL:name into foaf:givename and foaf:family_name
   * elements.
   * 
   * @param doc
   * @param repo
   */
  private static void checkPersonNames(Document doc, SimalRepository repo) {
    NodeList people = doc.getElementsByTagNameNS(FOAF_NS, "Person");
    Element person;
    for (int i = 0; i < people.getLength(); i = i + 1) {
      person = (Element) people.item(i);
      NodeList names = person.getElementsByTagNameNS(FOAF_NS, "name");
      for (int ni = 0; ni < names.getLength(); ni = ni + 1) {
        Element nameElement = (Element) names.item(ni);
        if (nameElement != null) {
          if (nameElement.getParentNode() == person) {
            String name = nameElement.getFirstChild().getNodeValue().trim();
            StringTokenizer tokeniser = new StringTokenizer(name, " ");

            for (int nn = 0; nn < tokeniser.countTokens(); nn = nn + 1) {
              String token = tokeniser.nextToken();
              Element givenname = doc.createElementNS(FOAF_NS, "givenname");
              Text text = doc.createTextNode(token);
              givenname.appendChild(text);
              person.appendChild(givenname);
            }
            String token = tokeniser.nextToken();
            Element familyName = doc.createElementNS(FOAF_NS, "family_name");
            Text text = doc.createTextNode(token);
            familyName.appendChild(text);
            person.appendChild(familyName);
          }
        }
      }
    }
  }

  /**
   * For every mailbox associated with a person create an sha1 checksum.
   * 
   * @param doc
   * @param repo
   * @throws SimalRepositoryException
   */
  private static void checkPersonSHA1(Document doc, SimalRepository repo)
      throws SimalRepositoryException {
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
      SimalRepository repo) {
    NodeList projects = doc.getElementsByTagNameNS(DOAP_NS, "Project");
    if (projects.getLength() > 0) {
      Node project = projects.item(0);
      Element seeAlso = doc.createElementNS(RDFS_NS, "seeAlso");
      seeAlso.setAttributeNS(RDF_NS, "resource", sourceURL.toExternalForm());
      project.appendChild(seeAlso);
    }
  }

  /**
   * Check that all people elements have an ID associated with them.
   * 
   * @param doc
   * @param repo
   * @throws SimalRepositoryException
   */
  private static void checkPersonIDs(Document doc, SimalRepository repo)
      throws SimalRepositoryException {
    NodeList people = doc.getElementsByTagNameNS(FOAF_NS, "Person");
    Element person;
    NodeList simalIDNL;
    String id = null;
    Node simalIDNode;
    for (int i = 0; i < people.getLength(); i = i + 1) {
      person = (Element) people.item(i);
      simalIDNL = person.getElementsByTagNameNS(SIMAL_NS, SIMAL_PERSON_ID);
      if (simalIDNL.getLength() == 0) {
        IPerson simalPerson = repo.getPerson(new QName(person
            .getAttributeNodeNS(RDF_NS, "about").getNodeValue()));
        if (simalPerson != null) {
          id = simalPerson.getSimalId();
        } else {
          id = repo.getNewPersonID();
        }
      }
      simalIDNode = doc.createElementNS(SIMAL_NS, SIMAL_PERSON_ID);
      Node text = doc.createTextNode(id);
      simalIDNode.appendChild(text);
      person.appendChild(simalIDNode);
    }
  }

  /**
   * Check that the project element has an ID associated with them.
   * 
   * @param doc
   * @param repo
   * @throws SimalRepositoryException
   */
  private static void checkProjectID(Document doc, SimalRepository repo)
      throws SimalRepositoryException {
    NodeList projects = doc.getElementsByTagNameNS(DOAP_NS, "Project");
    Element project;
    NodeList simalIDNL;
    String id = null;
    Node simalIDNode;
    for (int i = 0; i < projects.getLength(); i = i + 1) {
      project = (Element) projects.item(i);
      simalIDNL = project.getElementsByTagNameNS(SIMAL_NS, SIMAL_PROJECT_ID);
      if (simalIDNL.getLength() == 0) {
        IProject simalProject = repo.getProject(new QName(project
            .getAttributeNodeNS(RDF_NS, "about").getNodeValue()));
        if (simalProject != null) {
          id = simalProject.getSimalID();
        } else {
          id = repo.getNewProjectID();
        }
      }
      simalIDNode = doc.createElementNS(SIMAL_NS, SIMAL_PROJECT_ID);
      Node text = doc.createTextNode(id);
      simalIDNode.appendChild(text);
      project.appendChild(simalIDNode);
    }
  }

  /**
   * Look for duplicate people and replace the QName with that already present
   * in the repository.
   * 
   * @param doc
   *          an XML document representing the RDF data
   * @param repo
   * @return
   * @throws SimalRepositoryException
   * @throws DOMException
   */
  private static void deDupePeople(Document doc, SimalRepository repo)
      throws DOMException, SimalRepositoryException {
    // handle duplicate people identified by their mbox_sha1sum
    NodeList sha1sums = doc.getElementsByTagNameNS(FOAF_NS, "mbox_sha1sum");
    Node sha1sum;
    for (int i = 0; i < sha1sums.getLength(); i = i + 1) {
      sha1sum = sha1sums.item(i);
      IPerson person = repo.findPersonBySha1Sum(sha1sum.getFirstChild()
          .getNodeValue().trim());
      if (person != null) {
        logger.debug("Merging duplicate person (based on email SHA1): "
            + person.toString());
        Element personNode = (Element) sha1sum.getParentNode();
        personNode.setAttributeNS(RDF_NS, "about", person.getQName()
            .getNamespaceURI()
            + person.getQName().getLocalPart());
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
        logger.debug("Merging duplicate person (based on seeAlso): "
            + person.toString());
        Element personNode = (Element) seeAlso.getParentNode();
        personNode.setAttributeNS(RDF_NS, "about", person.getQName()
            .getNamespaceURI()
            + person.getQName().getLocalPart());
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
    File fileStoreDir = new File(SimalRepository
        .getProperty(SimalRepository.PROPERTY_SIMAL_DOAP_FILE_STORE)
        + File.separator + "simal-uploads");
    fileStoreDir.mkdirs();
    String path = fileStoreDir.getAbsolutePath();
    File file = new File(path + File.separator + filename);
    return file;
  }

  /**
   * Get the File for the local, annotated version of the file located at the
   * the given URL.
   * 
   * @return
   */
  public static File getAnnotatedDoapFile(URL url) {
    String filename;
    String path = url.getPath();
    int startName = path.lastIndexOf("/");
    if (startName > 0) {
      filename = path.substring(startName + 1);
    } else {
      filename = path;
    }
    return getAnnotatedDoapFile(filename);
  }
}
