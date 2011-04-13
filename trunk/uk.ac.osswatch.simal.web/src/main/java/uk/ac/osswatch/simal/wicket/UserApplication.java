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

import java.io.IOError;
import java.net.URL;
import java.util.Timer;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadWebRequest;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.util.convert.ConverterLocator;
import org.joseki.DatasetDesc;
import org.joseki.Processor;
import org.joseki.RDFServer;
import org.joseki.Registry;
import org.joseki.Service;
import org.joseki.ServiceRegistry;
import org.joseki.processors.SPARQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.jena.simal.JenaDatabaseSupport;
import uk.ac.osswatch.simal.model.jena.simal.JenaDatabaseSupport.JenaDatabaseType;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.schedule.ImportPTSWTask;
import uk.ac.osswatch.simal.wicket.authentication.SimalAuthorizationStrategy;
import uk.ac.osswatch.simal.wicket.authentication.SimalSession;
import uk.ac.osswatch.simal.wicket.data.URLConverter;
import uk.ac.osswatch.simal.wicket.doap.CategoryBrowserPage;
import uk.ac.osswatch.simal.wicket.doap.CategoryDetailPage;
import uk.ac.osswatch.simal.wicket.doap.ExhibitProjectBrowserPage;
import uk.ac.osswatch.simal.wicket.doap.ProjectDetailPage;
import uk.ac.osswatch.simal.wicket.foaf.ExhibitPersonBrowserPage;
import uk.ac.osswatch.simal.wicket.foaf.PersonDetailPage;
import uk.ac.osswatch.simal.wicket.widgets.WidgetInstancePage;
import uk.ac.osswatch.simal.wicket.widgets.WookieServerConnection;

import com.hp.hpl.jena.assembler.JA;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.tdb.TDB;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * The UserApp is the main user facing appliation. This application allows users
 * to view Simal registries.
 */
public class UserApplication extends WebApplication {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(UserApplication.class);

	private static ISimalRepository repository;

	private static boolean isTest;

	private static boolean schedulePTSW;

	private static Timer ptswTimer;

	private static WookieServerConnection wookieServerConnection;

	public UserApplication() {
		setScheduledPtswStatus(false);
	}

	@Override
	public void init() {
        SimalAuthorizationStrategy authorizationStrategy = new SimalAuthorizationStrategy();
        getSecuritySettings().setAuthorizationStrategy(authorizationStrategy);
        getSecuritySettings().setUnauthorizedComponentInstantiationListener(authorizationStrategy);
	       
		// Project pages
		mountBookmarkablePage("/project", ProjectDetailPage.class);
		mountBookmarkablePage("/projectBrowser",
				ExhibitProjectBrowserPage.class);

		// Person Pages
		mountBookmarkablePage("/person", PersonDetailPage.class);
		mountBookmarkablePage("/personBrowser", ExhibitPersonBrowserPage.class);

		// Category Pages
		mountBookmarkablePage("/categoryBrowser", CategoryBrowserPage.class);
    mountBookmarkablePage("/category", CategoryDetailPage.class);

    mountBookmarkablePage("/widgets", WidgetInstancePage.class);
    
    try {
      initRepository(true);
      initSparqlQueryEngine();
    } catch (SimalRepositoryException e) {
      LOGGER.error("Could not initialize repository. Error: " + e.getMessage(), e);
      // TODO there must be a more elegant way in Wicket, but couldn't find it:
      throw new IOError(e);
    }
  }

	@Override
	public Class<UserHomePage> getHomePage() {
		return UserHomePage.class;
	}
	
	/**
     * @see org.apache.wicket.protocol.http.WebApplication#newSession(Request, Response)
     */
    @Override
	public Session newSession(Request request, Response response) {
    	   return new SimalSession(request);
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
		  initRepository(true);
		}
		return repository;
	}

	/**
	 * Initialise the Simal repository, if not yet initialised.
	 * @throws SimalRepositoryException
	 */
	public static void initRepository() throws SimalRepositoryException { 
	  initRepository(false);
	}
	
  /**
   * Initialise the Simal repository.
   * 
   * @param forceInit
   *          If true, force initialisation, if false don't initialise if
   *          already done so.
   * @throws SimalRepositoryException
   */
  private static void initRepository(boolean forceInit)
      throws SimalRepositoryException {
    if (repository == null || forceInit) {
      repository = SimalRepositoryFactory.getInstance();
      repository.setIsTest(isTest);
      if (!repository.isInitialised()) {
        repository.initialise();
      }
    }
  }

	/**
	 * Destroy the repository object used by this application.
	 * 
	 * @throws SimalRepositoryException
	 */
	public static void destroyRepository() throws SimalRepositoryException {
	  if (repository != null) {
	    repository.destroy();
	    repository = null;
	  }
	}

	/**
	 * If IsTest is set to true then a test (in memory) repository will be used,
	 * otherwise a real repository is used.
	 * 
	 * @param value
	 */
	public static void setIsTest(boolean value) {
		if (value) {
			setScheduledPtswStatus(false);
		}
		isTest = value;
	}

	protected IConverterLocator newConverterLocator() {
		ConverterLocator converterLocator = new ConverterLocator();
		converterLocator.set(URL.class, new URLConverter());
		return converterLocator;
	}

	@Override
	protected WebRequest newWebRequest(HttpServletRequest servletRequest) {
		return new UploadWebRequest(servletRequest);
	}

  protected void onDestroy()
  {
    LOGGER.info("Trying to gracefully shut down the repository.");
    try {
      UserApplication.destroyRepository();
    } catch (SimalRepositoryException e) {
      LOGGER.info("Did not manage to go down gracefully.",e);
    }
  }

	/**
	 * Indicates if this application instance should attempt to perform
	 * scheduled updates from Ping The Semantic Web.
	 * 
	 * @return
	 */
	public static boolean getScheduledPtswStatus() {
		return schedulePTSW;
	}

	/**
	 * Sets whether this application instance should attempt to perform
	 * scheduled updates from Ping The Semantic Web.
	 * 
	 * @param status
	 */
	public static void setScheduledPtswStatus(boolean status) {
		schedulePTSW = status;
		if (schedulePTSW) {
			ptswTimer = new Timer(true);
			ptswTimer.schedule(new ImportPTSWTask(), 1000 * 60 * 1,
					1000 * 60 * 60);
		} else if (ptswTimer != null) {
			ptswTimer.cancel();
			ptswTimer = null;
		}
	}

	/**
	 * Get a connection to the Wookie Server used for gadget rendering.
	 * @param forceNew to indicate if always a new connection should be made.
	 * @return
	 */
	public static WookieServerConnection getWookieServerConnection(boolean forceNew) {
		if (wookieServerConnection == null || forceNew) {
			wookieServerConnection = new WookieServerConnection();
		}
		return wookieServerConnection;
	}
	
  /**
   * Initialise Joseki configuration to allow SPARQL querying of the 
   * Jena back-end. 
   */
  private void initSparqlQueryEngine() {
    ServiceRegistry myReg = new ServiceRegistry();
    Processor proc = new SPARQL();
    String baseRootURI = "#dataset";
    String serviceURI = "joseki/sparql";

    String dbPath = JenaDatabaseSupport.getDatabasePath();

    if (dbPath != null
        && JenaDatabaseSupport.getType().equals(JenaDatabaseType.TDB)) {
      Model model = ModelFactory.createDefaultModel();
      Resource baseResource = model.createResource(baseRootURI);
      Resource datasetTDB = ResourceFactory.createResource(TDB.namespace
          + "DatasetTDB");
      model.add(baseResource, RDF.type, datasetTDB);
      model.add(baseResource,
          ResourceFactory.createProperty(TDB.namespace, "location"), dbPath);
      model.add(datasetTDB, RDFS.subClassOf,
          ResourceFactory.createResource(JA.uri + "RDFDataset"));

      DatasetDesc datasetDesc = new DatasetDesc(baseResource);
      datasetDesc.initialize();

      Service handler = new Service(proc, serviceURI, datasetDesc);
      myReg.add(serviceURI, handler);

      Registry.add(RDFServer.ServiceRegistryName, myReg);
    } else {
      LOGGER.warn("Could not initialise Joseki, dbPath == " + dbPath);
    }
  }
}
