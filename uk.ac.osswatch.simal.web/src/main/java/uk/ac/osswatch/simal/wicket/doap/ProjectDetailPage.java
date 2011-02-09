package uk.ac.osswatch.simal.wicket.doap;

/*
 * Copyright 2008,2010 University of Oxford
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

import java.util.Iterator;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IFeed;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rest.RESTCommand;
import uk.ac.osswatch.simal.rest.SimalAPIException;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserApplication;
import uk.ac.osswatch.simal.wicket.UserHomePage;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.authentication.SimalSession;
import uk.ac.osswatch.simal.wicket.panel.ReviewListPanel;
import uk.ac.osswatch.simal.wicket.panel.project.EditProjectPanel;
import uk.ac.osswatch.simal.wicket.simal.AddReviewPanel;

public class ProjectDetailPage extends BasePage {
  private static final long serialVersionUID = 8719708525508677833L;
  private static final Logger logger = LoggerFactory
      .getLogger(ProjectDetailPage.class);
  private static final CompressedResourceReference FEED_API_CSS = new CompressedResourceReference(
      UserApplication.class, "style/googleFeedAPI.css");
  private IProject project;
  
  private boolean isLoggedIn = SimalSession.get().isAuthenticated();
  

  public ProjectDetailPage(PageParameters parameters) {
    String id = null;
    if (parameters.containsKey("simalID")) {
      id = parameters.getString("simalID");

      try {
        String uniqueSimalID = UserApplication.getRepository()
            .getUniqueSimalID(id);
        this.project = SimalRepositoryFactory.getProjectService()
            .getProjectById(uniqueSimalID);
        populatePage();
      } catch (SimalRepositoryException e) {
        UserReportableException error = new UserReportableException(
            "Unable to get project from the repository",
            ProjectDetailPage.class, e);
        setResponsePage(new ErrorReportPage(error));
      }
    } else {
      UserReportableException error = new UserReportableException(
          "Unable to get simalID parameter from URL", ProjectDetailPage.class);
      setResponsePage(new ErrorReportPage(error));
    }
  }

  public ProjectDetailPage(IProject project) {
    this.project = project;
    populatePage();
  }
  
  /**
   * Prepare for render by adding the Google Ajax Feed API
   * initialisation code.
   */
  @Override
  public void onBeforeRender() {
    super.onBeforeRender();
    
    try {
      add(JavascriptPackageResource.getHeaderContribution("http://www.google.com/jsapi?key=" + SimalProperties.getProperty(SimalProperties.PROPERTY_GOOGLE_AJAX_FEED_API_KEY)));
      add(JavascriptPackageResource.getHeaderContribution("http://www.google.com/uds/solutions/dynamicfeed/gfdynamicfeedcontrol.js"));
      add(CSSPackageResource.getHeaderContribution("http://www.google.com/uds/solutions/dynamicfeed/gfdynamicfeedcontrol.css"));
      add(CSSPackageResource.getHeaderContribution(FEED_API_CSS));
      
      StringBuffer config = new StringBuffer();
      
      config.append("<script type=\"text/javascript\">\n");
      config.append("function LoadDynamicFeedControl() {");
      config.append("var feeds = [");
      
      Iterator<IFeed> feedItr = project.getFeeds().iterator();
      IFeed feed = null;
      while (feedItr.hasNext()) {
        if (feed != null) {
          config.append(",");
        }
        feed = feedItr.next();
        config.append("{title: '" + feed.getTitle() + "',");
        config.append("url: '" + feed.getFeedURL() + "'");
        config.append("}");
      }
      config.append("];");
      
      config.append("var options = {");
      config.append("stacked : false,");
      config.append("horizontal : false,");
      config.append("title : \"\"");
      config.append("}\n\n");
      config.append("new GFdynamicFeedControl(feeds, 'feed-control', options);");
      config.append("}\n\n");
      config.append("google.load('feeds', '1');");
      config.append("google.setOnLoadCallback(LoadDynamicFeedControl);");
      config.append("</script>");

      add(new StringHeaderContributor(config.toString()));
    } catch (SimalRepositoryException e) {
      logger.warn("Unable to get Google API Key. Aborting addition of AJAX Feed widget.", e);
    }
  }

  private void populatePage() {
    if (project == null) {
    	UserReportableException error = new UserReportableException(
          "Null project supplied.", ProjectDetailPage.class);
        setResponsePage(new ErrorReportPage(error));
        return;
    }
    add(new DeleteLink("deleteProjectActionLink", project));

    try {
      RESTCommand cmd = RESTCommand.createGetProject(project.getSimalID(),
          RESTCommand.TYPE_SIMAL, RESTCommand.FORMAT_XML);
      add(new ExternalLink("doapLink", cmd.getURL()));
      String rdfLink = "<link href=\"" + cmd.getURL()
          + "\" rel=\"meta\" title=\"DOAP\" type=\"application/rdf+xml\" />";
      add(new StringHeaderContributor(rdfLink));
    } catch (SimalRepositoryException e) {
      UserReportableException error = new UserReportableException(
          "Unable to get new person ID from the repository",
          ExhibitProjectBrowserPage.class, e);
      setResponsePage(new ErrorReportPage(error));
    } catch (SimalAPIException e) {
      UserReportableException error = new UserReportableException(
          "Unable to get a RESTful URI for the project RDF/XML document",
          ProjectDetailPage.class, e);
      setResponsePage(new ErrorReportPage(error));
    }

    try {
      add(new Label("opennessRating", Integer.toString(project.getOpennessRating()) + "%"));
    } catch (SimalRepositoryException e) {
        add(new Label("opennessRating", "Not reviewed"));
    }
    add(new EditProjectPanel("editProjectPanel", project, isLoggedIn));
    
    // sources
    add(getRepeatingDataSourcePanel("sources", "seeAlso", project.getSources()));
    try {
		add(new Label("simalID", project.getSimalID()));
	} catch (SimalRepositoryException e) {
		add(new Label("simalID", e.getMessage()));
	}
    
    // reviews
    ReviewListPanel reviewList;
	try {
		reviewList = new ReviewListPanel("reviews",
		    SimalRepositoryFactory.getReviewService().getReviewsForProject(project));
	    reviewList.setOutputMarkupId(true);
	} catch (SimalRepositoryException e) {
		reviewList = null;
	}
    add(reviewList);
    
    // FIXME: reviewer should be set from a drop down
    IPerson reviewer;
	try {
		reviewer = SimalRepositoryFactory.getPersonService().get("http://people.apache.org/~rgardler/#me");
	} catch (SimalRepositoryException e) {
		logger.error("Unable to get Ross Gardler as a reviewer", e);
		reviewer = null;
	}
    add(new AddReviewPanel("addReviewPanel", project, reviewer, reviewList));

    add(new Label("created", project.getCreated()));
  }

  /**
   * Get a link to a ProjectDetailPage for a project.
   * 
   * @param project
   *          the project we want a detail page link for
   * @return
   */
  @SuppressWarnings("serial")
  public static IPageLink getLink(final IProject project) {
    IPageLink link = new IPageLink() {
      public Page getPage() {
        return new ProjectDetailPage(project);
      }

      public Class<ProjectDetailPage> getPageIdentity() {
        return ProjectDetailPage.class;
      }
    };
    return link;
  }

  public IProject getProject() {
    return project;
  }

  private static class DeleteLink extends Link<IProject> {
    IProject project;

    public DeleteLink(String id, IProject project) {
      super(id);
      this.project = project;
    }

    private static final long serialVersionUID = 1L;

    public void onClick() {
      try {
        project.delete();
        getRequestCycle().setResponsePage(new UserHomePage());
      } catch (SimalRepositoryException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

}
