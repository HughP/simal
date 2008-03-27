package uk.ac.osswatch.simal.openSocial;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.shindig.gadgets.GadgetToken;
import org.apache.shindig.social.opensocial.model.IdSpec;
import org.json.JSONException;

import uk.ac.osswatch.simal.rest.RESTCommand;
import uk.ac.osswatch.simal.rest.SimalAPIException;

public class SimalPeopleService extends
    org.apache.shindig.social.samplecontainer.BasicPeopleService {

  @Override
  public List<String> getIds(IdSpec idSpec, GadgetToken token)
      throws JSONException {
    Map<String, List<String>> friendIds;
    String personID = null;
    switch (idSpec.getType()) {
    case OWNER:
      personID = token.getOwnerId();
      break;
    case VIEWER:
      personID = token.getViewerId();
      break;
    case OWNER_FRIENDS:
      personID = token.getOwnerId();
      break;
    case VIEWER_FRIENDS:
      personID = token.getViewerId();
      break;
    case USER_IDS:
      personID = token.getViewerId();
      break;
    }
    
    RESTCommand cmd = RESTCommand.createGetColleagues(personID, token.getAppId(), RESTCommand.FORMAT_XML);
    
    try {
      friendIds = RESTFetcher.get(cmd).getFriendIds();
    } catch (SimalAPIException e) {
      throw new JSONException(e);
    }

    List<String> ids = new ArrayList<String>();
    switch (idSpec.getType()) {
    case OWNER:
      ids.add(token.getOwnerId());
      break;
    case VIEWER:
      ids.add(token.getViewerId());
      break;
    case OWNER_FRIENDS:
      ids.addAll(friendIds.get(token.getOwnerId()));
      break;
    case VIEWER_FRIENDS:
      ids.addAll(friendIds.get(token.getViewerId()));
      break;
    case USER_IDS:
      ids.addAll(idSpec.fetchUserIds());
      break;
    }
    return ids;
  }

}
