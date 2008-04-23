package uk.ac.osswatch.simal.openSocial;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.apache.shindig.social.samplecontainer.XmlStateFileFetcher;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rest.HandlerFactory;
import uk.ac.osswatch.simal.rest.IAPIHandler;
import uk.ac.osswatch.simal.rest.RESTCommand;
import uk.ac.osswatch.simal.rest.SimalAPIException;
import uk.ac.osswatch.simal.wicket.UserApplication;

/**
 * A class for retrieving social data from the Simal Rest API.
 * 
 */
public class RESTFetcher {

  private static RESTFetcher fetcher;
  private static XmlStateFileFetcher xmlFetcher;

  /**
   * This class should not be instantiated directly.
   * 
   * @throws UserReportableException
   * @see RESTFetcher#get()
   */
  private RESTFetcher() {
    //xmlFetcher = new XmlStateFileFetcher();
  };

  /**
   * Get a RESTFetcher that will respond to the supplied
   * command.
   * 
   * @return
   * @throws SimalAPIException 
   */
  public static RESTFetcher get(RESTCommand cmd) throws SimalAPIException {
    if (fetcher == null) {
      fetcher = new RESTFetcher();
    }
    IAPIHandler handler;
    try {
      handler = HandlerFactory.get(cmd, UserApplication.getRepository());
      xmlFetcher.resetStateFile(handler.getStateURI());
    } catch (URISyntaxException e) {
      throw new SimalAPIException("Unable to create state URI", e);
    } catch (SimalRepositoryException e) {
      throw new SimalAPIException("Unable to connect to the repository", e);
    }  
    return fetcher;
  }

  /**
   * Get a map of friend IDs.
   * 
   * @param token
   * @return
   */
  public Map<String, List<String>> getFriendIds() {
    return xmlFetcher.getFriendIds();
  }

}

