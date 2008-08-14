package uk.ac.osswatch.simal.myExperiment;
/*
 * Copyright 2008 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");   *
 * you may not use this file except in compliance with the License.  *
 * You may obtain a copy of the License at                           *
 *                                                                   *
 *   http://www.apache.org/licenses/LICENSE-2.0                      *
 *                                                                   *
 * Unless required by applicable law or agreed to in writing,        *
 * software distributed under the License is distributed on an       *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY            *
 * KIND, either express or implied.  See the License for the         *
 * specific language governing permissions and limitations           *
 * under the License.                                                *
 */


import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.ac.osswatch.simal.rest.AbstractHandler;
import uk.ac.osswatch.simal.rest.RESTCommand;
import uk.ac.osswatch.simal.rest.SimalAPIException;

/**
 * MyExperiment API functionality for working with Person objects.
 * 
 */
public class PersonAPI extends AbstractHandler {

  /**
   * Create a PersonAPI that will operate on a given MyExperiment instance.
   * Handlers should not be instantiated directly, use
   * HandlerFactory.createHandler(...) instead.
   * 
   * @param uri
   *          the URi of the MyExperiment instance to query
   * @throws SimalAPIException
   */
  protected PersonAPI(RESTCommand cmd) {
    super(cmd);
  }

  /**
   * Execute a command.
   * 
   * @param cmd
   * @throws SimalAPIException
   */
  public String execute() throws SimalAPIException {
    if (command.isGetColleagues()) {
      return getAllColleagues(command);
    } else {
      throw new SimalAPIException("Unkown command: " + command);
    }
  }

  /**
   * Get all the colleagues for this
   * 
   * @param cmd
   * @return
   * @throws SimalAPIException
   */
  public String getAllColleagues(RESTCommand command) throws SimalAPIException {
    String elements = "id,name,friends";
    String reqURI = getBaseURI() + "/user.xml?id=" + command.getPersonID()
        + "&elements=" + elements;

    HttpClient client = new HttpClient();
    HttpMethod method = new GetMethod(reqURI);
    try {
      client.executeMethod(method);
    } catch (HttpException e) {
      throw new SimalAPIException(
          "Unable to retrieve data from the MyExperiment instance "
              + getBaseURI(), e);
    } catch (IOException e) {
      throw new SimalAPIException(
          "Unable to retrieve data from the MyExperiment instance "
              + getBaseURI(), e);
    }

    StringWriter out;
    try {
      InputSource in = new InputSource(new StringReader(new String(method.getResponseBody())));
      DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory
          .newInstance();
      DocumentBuilder parser = docBuildFactory.newDocumentBuilder();
      Document document = parser.parse(in);

      TransformerFactory xformFactory =
                TransformerFactory.newInstance();
      StreamSource xslt = new StreamSource(PersonAPI.class.getResourceAsStream("myExperiment-to-shindig.xsl")); 
      Transformer transformer =
                   xformFactory.newTransformer(xslt);
      DOMSource source = new DOMSource(document);
      out = new StringWriter();
      StreamResult scrResult =
        new StreamResult(out);
      transformer.transform(source, scrResult); 
    } catch (IOException e) {
      throw new SimalAPIException(
          "Unable to read the response from the MyExperiment instance "
              + reqURI, e);
    } catch (ParserConfigurationException e) {
      throw new SimalAPIException(
          "Unable to create an XML parser", e);
    } catch (SAXException e) {
      throw new SimalAPIException(
          "Unable to parse the response from the MyExperiment instance "
              + reqURI, e);
    } catch (TransformerConfigurationException e) {
      throw new SimalAPIException(
          "Unable to create an XSLT transformer", e);
    } catch (TransformerException e) {
      throw new SimalAPIException(
          "Unable to transform the response from the MyExperiment instance "
              + reqURI, e);
    }

    return out.toString();
  }

}
