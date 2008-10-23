package uk.ac.osswatch.simal.wicket;

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

import java.net.URL;

import org.apache.wicket.IConverterLocator;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.target.coding.QueryStringUrlCodingStrategy;
import org.apache.wicket.util.convert.ConverterLocator;

import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryFactory;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.wicket.data.URLConverter;
import uk.ac.osswatch.simal.wicket.doap.CategoryBrowserPage;
import uk.ac.osswatch.simal.wicket.doap.ExhibitProjectBrowserPage;
import uk.ac.osswatch.simal.wicket.doap.ProjectDetailPage;
import uk.ac.osswatch.simal.wicket.foaf.PersonBrowserPage;
import uk.ac.osswatch.simal.wicket.foaf.PersonDetailPage;

/**
 * The UserApp is the main user facing appliation. This application allows users
 * to view Simal registries.
 */
public class UserApplication extends WebApplication {

  private static ISimalRepository repository;

  private static boolean isTest;

  public UserApplication() {
  }

  @Override
  public void init() {
    // Project pages
    mountBookmarkablePage("/project/detail", ProjectDetailPage.class);
    mount(new QueryStringUrlCodingStrategy("/project/detailencoded",
        ProjectDetailPage.class));

    mountBookmarkablePage("/projectBrowser", ExhibitProjectBrowserPage.class);

    // Person Pages
    mountBookmarkablePage("/person/detail", PersonDetailPage.class);
    mount(new QueryStringUrlCodingStrategy("/person/detailencoded",
        PersonDetailPage.class));

    mountBookmarkablePage("/personBrowser", PersonBrowserPage.class);

    // Category Pages
    mountBookmarkablePage("/categoryBrowser", CategoryBrowserPage.class);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Class getHomePage() {
    return UserHomePage.class;
  }

  /**
   * Get the repository for this application. If the repository has not been
   * initialised yet then create and initialise it first.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public static ISimalRepository getRepository()
      throws SimalRepositoryException {
    if (repository == null) {
      repository = SimalRepositoryFactory.getInstance();
      repository.setIsTest(isTest);
      if (!repository.isInitialised()) {
        repository.initialise();
      }
    }
    return repository;
  }

  /**
   * Destroy the repository object used by this application.
   * 
   * @throws SimalRepositoryException
   */
  public static void destroyRepository() throws SimalRepositoryException {
    repository.destroy();
    repository = null;
  }

  /**
   * If IsTest is set to true then a test (in memory) repository will be used,
   * otherwise a real repository is used.
   * 
   * @param value
   */
  public static void setIsTest(boolean value) {
    isTest = value;
  }

  protected IConverterLocator newConverterLocator() {
    ConverterLocator converterLocator = new ConverterLocator();
    converterLocator.set(URL.class, new URLConverter());
    return converterLocator;
  }

}