package uk.ac.osswatch.simal.openSocial;

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
