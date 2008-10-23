package uk.ac.osswatch.simal.unitTest.tools;

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
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.tools.Ohloh;

public class MockOhloh extends Ohloh {

  @Override
  protected Document getOhlohResponse(String urlString) throws SimalException {
    URL url;
    if (urlString.contains("ohlohtest")) {
      if (urlString.contains("contributors.xml")) {
        url = MockOhloh.class
            .getResource("/testData/ohlohContributorExport.xml");
      } else if (urlString.contains("/accounts/")) {
        url = MockOhloh.class.getResource("/testData/ohlohAccountExport.xml");
      } else {
        url = MockOhloh.class.getResource("/testData/ohlohProjectExport.xml");
      }
    } else {
      url = MockOhloh.class.getResource("/testData/ohlohError.xml");
    }
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    DocumentBuilder db;
    try {
      db = dbf.newDocumentBuilder();
      return db.parse(url.openStream());
    } catch (ParserConfigurationException e) {
      throw new SimalException("Unable to build XML parser", e);
    } catch (SAXException e) {
      throw new SimalException("Unable to parse Ohloh document", e);
    } catch (IOException e) {
      throw new SimalException("Unable to read Olhoh response", e);
    }
  }

  @Override
  protected String getApiKey() throws SimalException {
    return "MockAPIKey";
  }

}
