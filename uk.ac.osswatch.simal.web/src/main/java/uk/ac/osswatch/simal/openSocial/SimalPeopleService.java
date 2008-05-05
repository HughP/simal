package uk.ac.osswatch.simal.openSocial;
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


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.shindig.gadgets.GadgetToken;
import org.apache.shindig.social.opensocial.model.IdSpec;
import org.apache.shindig.social.samplecontainer.XmlStateFileFetcher;
import org.json.JSONException;

import com.google.inject.Inject;

import uk.ac.osswatch.simal.rest.RESTCommand;
import uk.ac.osswatch.simal.rest.SimalAPIException;

public class SimalPeopleService extends
    org.apache.shindig.social.samplecontainer.BasicPeopleService {
  
  XmlStateFileFetcher fetcher;

  @Inject
  public SimalPeopleService(XmlStateFileFetcher fetcher) {
    super(fetcher);
    this.fetcher = fetcher;
  }

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
      friendIds = RESTFetcher.get(cmd, fetcher).getFriendIds();
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

