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
package uk.ac.osswatch.simal.tools;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;

public class PTSWImport {
  
  /**
   * Get latest pings as RDF/XML by retrieving the document
   * http://pingthesemanticweb.com/export/, extracting the
   * list of pings and then retrieving the relevant DOAP
   * files.<br/>
   * 
   * Note that you must have created an account on the
   * Ping The Semantic Web site for the IP number that is
   * making the request.
   * @throws SimalException 
   * 
   * @SeeAlso #getListOfPings, #getPingsAsRDF 
   * 
   */
  public Document getLatestPingsAsRDF() throws SimalException {
    URL url;
    try {
      url = new URL("http://pingthesemanticweb.com/export/");
    } catch (MalformedURLException e) {
      throw new SimalException("The PTSW URL is malformed, how can that happen since it is hard coded?", e);
    }
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    Document doc = null;
    
    DocumentBuilder db;
    try {
      db = dbf.newDocumentBuilder();
      doc = db.parse(url.openStream());
    } catch (Exception e) {
      throw new SimalException("Unable to retrive the PTSW export file", e);
    }
    
    return getPingsAsRDF(doc);
  }

  /**
   * Get a list of pings contained within the supplied XML
   * Export from Ping The Semantic Web.
   */ 
  public Set<URL> getListOfPings(Document export) {
    HashSet<URL> urls = new HashSet<URL>();
    Element root = export.getDocumentElement();
    NodeList pings = root.getElementsByTagName("rdfdocument");
    for (int i = 0; i < pings.getLength(); i ++) {
      Element ping = (Element)pings.item(i);
      try {
        String strURL = ping.getAttributeNode("url").getValue();
        if (strURL.startsWith("http://doapspace.org")) {
          strURL = strURL + ".rdf";
        }
        urls.add(new URL(strURL));
      } catch (MalformedURLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return urls;
  }
  
  /**
   * Get an RDF/XML document containing all the DOAP files referenced
   * in a PTSW export file.
   * @throws ParserConfigurationException 
   */
  public Document getPingsAsRDF(Document export) throws SimalException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    DocumentBuilder db;
    try {
      db = dbf.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      throw new SimalException("Unable to create new RDF/XML document", e);
    }
    Document resultDoc = db.newDocument();
    Element root = resultDoc.createElementNS(RDFUtils.RDF_NS, "RDF");
    Iterator<URL> pings = getListOfPings(export).iterator();
    Document projectDoc;
    Element projectRoot;
    while(pings.hasNext()) {
      try {
        URL ping = pings.next();
        projectDoc = db.parse(ping.openStream());
        projectRoot = projectDoc.getDocumentElement();
        if (!projectRoot.getLocalName().equals("Project")) {
          NamedNodeMap atts = projectRoot.getAttributes();
          Element docRoot = projectRoot;
          projectRoot = (Element)projectRoot.getElementsByTagNameNS(RDFUtils.DOAP_NS, "Project").item(0);
          for (int i = 0; i < atts.getLength(); i++) {
            Attr att = (Attr)atts.item(i);
            i = i - 1;
            if (att.getName().startsWith("xmlns")) {
              docRoot.removeAttributeNode(att);
              projectRoot.setAttributeNode(att);
            }
          }
        }
        Node importedProjectNode = resultDoc.importNode(projectRoot, true);
        Element seeAlso = resultDoc.createElementNS(RDFUtils.RDF_NS, "rdf:seeAlso");
        seeAlso.setAttributeNS(RDFUtils.RDF_NS, "rdf:resource", ping.toExternalForm());
        importedProjectNode.appendChild(seeAlso);
        root.appendChild(importedProjectNode);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    resultDoc.appendChild(root);
    return resultDoc;
  }
}
