package uk.ac.osswatch.simal.myExperiment;

import java.io.CharArrayReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;

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

import uk.ac.osswatch.simal.AbstractHandler;
import uk.ac.osswatch.simal.RESTCommand;
import uk.ac.osswatch.simal.SimalAPIException;

/**
 * MyExperiment API functionality for working with Person objects.
 * 
 */
public class PersonAPI extends AbstractHandler {
  String myExperimentURI;

  /**
   * Create a PersonAPI that will operate on a given MyExperiment instance.
   * Handlers should not be instantiated directly, use
   * HandlerFactory.createHandler(...) instead.
   * 
   * @param uri
   *          the URi of the MyExperiment instance to query
   * @throws SimalAPIException
   */
  protected PersonAPI(String uri) {
    super();
    this.myExperimentURI = uri;
  }

  /**
   * Execute a command.
   * 
   * @param cmd
   * @throws SimalAPIException
   */
  public String execute(RESTCommand command) throws SimalAPIException {
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
    String reqURI = myExperimentURI + "/user.xml?id=" + command.getPersonID()
        + "&elements=" + elements;

    HttpClient client = new HttpClient();
    HttpMethod method = new GetMethod(reqURI);
    try {
      client.executeMethod(method);
    } catch (HttpException e) {
      throw new SimalAPIException(
          "Unable to retrieve data from the MyExperiment instance "
              + myExperimentURI, e);
    } catch (IOException e) {
      throw new SimalAPIException(
          "Unable to retrieve data from the MyExperiment instance "
              + myExperimentURI, e);
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
      StreamSource xslt = new StreamSource(new File(PersonAPI.class.getResource("myExperiment-to-shindig.xsl").toURI())); 
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
    } catch (URISyntaxException e) {
      throw new SimalAPIException(
          "Unable to find XSLT file to convert from myExperiment data to Shindig data", e);
    }

    return out.toString();
  }

}
