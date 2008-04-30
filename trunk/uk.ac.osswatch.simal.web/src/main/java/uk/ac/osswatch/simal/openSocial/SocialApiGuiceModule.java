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


import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;

import java.util.ArrayList;
import java.util.List;

import org.apache.shindig.social.GadgetDataHandler;
import org.apache.shindig.social.opensocial.ActivitiesService;
import org.apache.shindig.social.opensocial.DataService;
import org.apache.shindig.social.opensocial.OpenSocialDataHandler;
import org.apache.shindig.social.opensocial.PeopleService;
import org.apache.shindig.social.samplecontainer.StateFileDataHandler;

/**
 * Provides social api component injection
 */
public class SocialApiGuiceModule extends AbstractModule {

  /** {@inheritDoc} */
  @Override
  protected void configure() {
    bind(PeopleService.class).to(SimalPeopleService.class);
    bind(DataService.class).to(SimalDataService.class);
    bind(ActivitiesService.class).to(SimalActivitiesService.class);

    bind(new TypeLiteral<List<GadgetDataHandler>>() {
    }).toProvider(GadgetDataHandlersProvider.class);
  }

  public static class GadgetDataHandlersProvider
      implements Provider<List<GadgetDataHandler>> {
    List<GadgetDataHandler> handlers;

    @Inject
    public GadgetDataHandlersProvider(OpenSocialDataHandler
        openSocialDataHandler, StateFileDataHandler stateFileHandler) {
      handlers = new ArrayList<GadgetDataHandler>();
      handlers.add(openSocialDataHandler);
      handlers.add(stateFileHandler);
    }

    public List<GadgetDataHandler> get() {
      return handlers;
    }
  }
}
