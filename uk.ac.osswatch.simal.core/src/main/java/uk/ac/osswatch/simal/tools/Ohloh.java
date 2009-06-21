package uk.ac.osswatch.simal.tools;

/*
 * 
 * Copyright 2007 University of Oxford
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryFactory;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.rdf.jena.SimalRepository;

/**
 * Pulls data from Ohloh and converts it to data of use to Simal.
 * 
 */
public class Ohloh {
  private static final String OHLOH_BASE_URI = "http://www.ohloh.net";

  /**
   * Adds a project from OhLoh to the repository.
   * 
   * @param projectID
   * @return
   * @throws SimalException
   */
  public void addProjectToSimal(String projectID) throws SimalException {
    ISimalRepository repo = SimalRepositoryFactory.getInstance(SimalRepositoryFactory.TYPE_JENA);

    try {
      DOMResult domProjectResult = getProjectDataAsDOAP(projectID);
      DOMResult domContributorResult = getContributorDataAsFOAF(projectID);
      Element resultRoot = mergeProjectAndContributorData(domProjectResult,
          domContributorResult).getDocumentElement();

      // Now add the data to the repository
      StringBuilder source = new StringBuilder(OHLOH_BASE_URI);
      source.append("/");
      source.append(projectID);
      URL sourceURL = new URL(source.toString());
      repo.addProject(resultRoot.getOwnerDocument(), sourceURL, OHLOH_BASE_URI);
    } catch (TransformerConfigurationException e) {
      throw new SimalRepositoryException("Unable to create XSL Transformer", e);
    } catch (TransformerException e) {
      throw new SimalRepositoryException("Unable to transform Ohloh data", e);
    } catch (MalformedURLException e) {
      throw new SimalRepositoryException("Malformed URL for an Ohloh resource",
          e);
    } catch (IOException e) {
      throw new SimalRepositoryException("Unable to read osloh to doap XSL", e);
    }
  }

  private DOMResult getProjectDataAsDOAP(String projectID)
      throws TransformerConfigurationException, IOException,
      TransformerException, SimalException {
    Document ohlohProject = getProjectData(projectID);
    TransformerFactory tFactory = TransformerFactory.newInstance();
    URL xsl = Ohloh.class.getResource("/stylesheet/ohlohProject-to-doap.xsl");
    Transformer transformer = tFactory.newTransformer(new StreamSource(xsl
        .openStream()));
    DOMResult domProjectResult = new DOMResult();
    transformer.transform(new DOMSource(ohlohProject), domProjectResult);
    return domProjectResult;
  }

  private DOMResult getContributorDataAsFOAF(String projectID)
      throws TransformerConfigurationException, IOException,
      TransformerException, SimalException {
    Document ohlohContributors = getContributorData(projectID);
    NodeList contributors = ohlohContributors
        .getElementsByTagName("contributor_fact");
    for (int i = 0; i < contributors.getLength(); i++) {
      Element contributor = (Element) contributors.item(i);
      Node accountIDNode = contributor.getElementsByTagName("account_id").item(
          0);
      if (accountIDNode != null) {
        String accountID = accountIDNode.getTextContent();
        if (accountID.length() > 0) {
          Element ohlohAccount = getAccountData(accountID).getDocumentElement();
          contributor.appendChild(ohlohContributors.importNode(ohlohAccount,
              true));
        }
      }
    }

    TransformerFactory tFactory = TransformerFactory.newInstance();
    URL xsl = Ohloh.class
        .getResource("/stylesheet/ohlohContributor-to-foaf.xsl");
    Transformer transformer = tFactory.newTransformer(new StreamSource(xsl
        .openStream()));
    DOMResult domContributorResult = new DOMResult();
    transformer.transform(new DOMSource(ohlohContributors),
        domContributorResult);

    return domContributorResult;
  }

  private Document mergeProjectAndContributorData(DOMResult domProjectResult,
      DOMResult domContributorResult) {
    Document result = (Document) domProjectResult.getNode();
    Element resultRoot = result.getDocumentElement();
    NodeList contributors = ((Document) domContributorResult.getNode())
        .getDocumentElement()
        .getElementsByTagNameNS(RDFUtils.FOAF_NS, "Person");
    for (int i = 0; i < contributors.getLength(); i++) {
      Node contributor = contributors.item(i);
      Node developer = result.createElementNS("http://usefulinc.com/ns/doap#",
          "developer");
      developer.appendChild(result.importNode(contributor, true));
      resultRoot.getFirstChild().appendChild(developer);
    }
    return result;
  }

  /**
   * Get the Ohloh project data as an Ohloh XML response document.
   * 
   * @see https://www.ohloh.net/api/getting_started
   * 
   * @param projectID
   * @return
   * @throws SimalException
   */
  protected Document getProjectData(String projectID) throws SimalException {
    String apiKey = getApiKey();
    StringBuilder sb = new StringBuilder(OHLOH_BASE_URI);
    sb.append("/projects/");
    sb.append(projectID);
    sb.append(".xml");
    sb.append("?api_key=");
    sb.append(apiKey);
    String urlString = sb.toString();
    Document doc = getOhlohResponse(urlString);
    return doc;
  }

  protected Document getOhlohResponse(String urlString) throws SimalException {
    URLConnection con;
    try {
      URL url = new URL(urlString);
      con = url.openConnection();
      if (!con.getHeaderField("Status").startsWith("200")) {
        throw new SimalException("Unable to open connection to " + url
            + " status: " + con.getHeaderField("Status"));
      }
    } catch (MalformedURLException e) {
      throw new SimalException(
          "The Ohloh URL is malformed, how can that happen since it is hard coded?",
          e);
    } catch (IOException e) {
      throw new SimalException("Unable to open connection to Ohloh", e);
    }
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    Document doc = null;

    DocumentBuilder db;
    try {
      db = dbf.newDocumentBuilder();
      doc = db.parse(con.getInputStream());
    } catch (ParserConfigurationException e) {
      throw new SimalException("Unable to configure XML parser", e);
    } catch (SAXException e) {
      throw new SimalException("Unable to parse XML document", e);
    } catch (IOException e) {
      throw new SimalException("Unable to read XML response", e);
    }
    return doc;
  }

  /**
   * Get the API key for this application. This should be set in
   * local.simal.properties with the parameter name ohloh.api.key
   * 
   * @return
   * @throws SimalException
   */
  protected String getApiKey() throws SimalException {
    String apiKey = SimalProperties
        .getProperty(SimalProperties.PROPERTY_OHLOH_API_KEY);
    if (apiKey == null || apiKey.length() == 0
        || apiKey.contains("has not been set")) {
      throw new SimalException(
          "To import from Ohloh it is necessary to provide an Ohloh API key. Please set ohloh.api.key in local.simal.properties");
    }
    return apiKey;
  }

  /**
   * Get a list of contributors to a given project as an Ohloh XML response
   * document.
   * 
   * @param projectID
   * @return
   * @throws SimalException
   */
  public Document getContributorData(String projectID) throws SimalException {
    String apiKey = getApiKey();
    StringBuilder sb = new StringBuilder(OHLOH_BASE_URI);
    sb.append("/projects/");
    sb.append(projectID);
    sb.append("/contributors.xml");
    sb.append("?api_key=");
    sb.append(apiKey);
    String urlString = sb.toString();
    Document doc = getOhlohResponse(urlString);
    return doc;
  }

  /**
   * Get the Ohloh data for a specific account as an Ohloh XML response
   * document.
   * 
   * @param accountID
   * @return
   * @throws SimalException
   */
  public Document getAccountData(String accountID) throws SimalException {
    String apiKey = getApiKey();
    StringBuilder sb = new StringBuilder(OHLOH_BASE_URI);
    sb.append("/accounts/");
    sb.append(accountID);
    sb.append(".xml");
    sb.append("?api_key=");
    sb.append(apiKey);
    String urlString = sb.toString();
    Document doc = getOhlohResponse(urlString);
    return doc;
  }

  public String getProjectPage(String projectID) {
    StringBuilder source = new StringBuilder(OHLOH_BASE_URI);
    source.append("/projects/");
    source.append(projectID);
    return source.toString();
  }

}
