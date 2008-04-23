package uk.ac.osswatch.simal.openSocial;

import org.apache.shindig.social.samplecontainer.XmlStateFileFetcher;

import com.google.inject.Inject;

public class SimalDataService extends
    org.apache.shindig.social.samplecontainer.BasicDataService {

  @Inject
  public SimalDataService(XmlStateFileFetcher fetcher) {
    super(fetcher);
  }

}

