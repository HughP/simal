package uk.ac.osswatch.simal.openSocial;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.shindig.gadgets.BasicGadgetSigner;
import org.apache.shindig.gadgets.GadgetException;
import org.apache.shindig.gadgets.GadgetToken;
import org.apache.shindig.social.opensocial.model.IdSpec;
import org.json.JSONException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.osswatch.simal.rest.RESTCommand;

public class TestSimalPeopleService {
  static TestServerThread server;

  @BeforeClass
  public static void startServer() throws Exception {
    server = new TestServerThread();
    server.start();
  }
  
  @AfterClass
  public static void stopServer() throws Exception {
    server.destroy();
  }

  @Test
  public void testGetSimalAppViewerFriendIds() throws JSONException,
      GadgetException {
    String jsonIdSpec = "VIEWER_FRIENDS";
    IdSpec idSpec = IdSpec.fromJson(jsonIdSpec);
    BasicGadgetSigner signer = new BasicGadgetSigner();
    GadgetToken token = signer.createToken("1:15:"
        + RESTCommand.SOURCE_TYPE_SIMAL + ":simal.oss-watch.ac.uk");

    SimalPeopleService service = new SimalPeopleService();
    List<String> ids = service.getIds(idSpec, token);
    assertEquals("Got the wrong number of IDs", 8, ids.size());
  }

  @Test
  public void testGetSimalAppOwnerFriendIds() throws JSONException,
      GadgetException {
    String jsonIdSpec = "OWNER_FRIENDS";
    IdSpec idSpec = IdSpec.fromJson(jsonIdSpec);
    BasicGadgetSigner signer = new BasicGadgetSigner();
    GadgetToken token = signer.createToken("15:1:"
        + RESTCommand.SOURCE_TYPE_SIMAL + ":simal.oss-watch.ac.uk");

    SimalPeopleService service = new SimalPeopleService();
    List<String> ids = service.getIds(idSpec, token);
    assertEquals("Got the wrong number of IDs", 8, ids.size());
  }

  @Test
  public void testGetMyExperimentAppFriendIds() throws JSONException,
      GadgetException {
    String jsonIdSpec = "VIEWER_FRIENDS";
    IdSpec idSpec = IdSpec.fromJson(jsonIdSpec);
    BasicGadgetSigner signer = new BasicGadgetSigner();
    GadgetToken token = signer.createToken("15:15:"
        + RESTCommand.SOURCE_TYPE_MYEXPERIMENT + ":simal.oss-watch.ac.uk");

    SimalPeopleService service = new SimalPeopleService();
    List<String> ids = service.getIds(idSpec, token);
    assertEquals("Got the wrong number of IDs", 7, ids.size());
  }

}
