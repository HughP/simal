package uk.ac.osswatch.simal.openSocial;

import org.apache.shindig.social.samplecontainer.XmlStateFileFetcher;

import com.google.inject.Inject;

public class SimalActivitiesService extends
    org.apache.shindig.social.samplecontainer.BasicActivitiesService {
  
  @Inject
  public SimalActivitiesService(XmlStateFileFetcher fetcher) {
    super(fetcher);
  }

}

