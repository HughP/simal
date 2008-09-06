package uk.ac.osswatch.simal.wicket.doap;
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


import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rest.RESTCommand;
import uk.ac.osswatch.simal.rest.SimalAPIException;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserApplication;
import uk.ac.osswatch.simal.wicket.UserHomePage;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.data.SortableDoapResourceDataProvider;
import uk.ac.osswatch.simal.wicket.foaf.AddPersonPanel;
import uk.ac.osswatch.simal.wicket.panel.CategoryListPanel;
import uk.ac.osswatch.simal.wicket.panel.PersonListPanel;
import uk.ac.osswatch.simal.wicket.panel.ReleasesPanel;
import uk.ac.osswatch.simal.wicket.panel.SourceRepositoriesPanel;

public class ProjectDetailPage extends BasePage {
  private static final long serialVersionUID = 8719708525508677833L;
  private static final Logger logger = LoggerFactory.getLogger(ProjectDetailPage.class);
  private IProject project;
  
  public ProjectDetailPage(PageParameters parameters) {
    String id = null;
    if (parameters.containsKey("simalID")) {
        id = parameters.getString("simalID");
        
        try {
          String uniqueSimalID = UserApplication.getRepository().getUniqueSimalID(id);
          project = UserApplication.getRepository().findProjectById(uniqueSimalID);
          populatePage(project);
        } catch (SimalRepositoryException e) {
          UserReportableException error = new UserReportableException(
              "Unable to get project from the repository",
              ProjectDetailPage.class, e);
          setResponsePage(new ErrorReportPage(error));
        }
    } else {
      UserReportableException error = new UserReportableException(
          "Unable to get simalID parameter from URL",
          ProjectDetailPage.class);
      setResponsePage(new ErrorReportPage(error));
    }
  }
  
  public ProjectDetailPage(IProject project) {
    populatePage(project);
  }

  private void populatePage(final IProject project) {
    this.project = project;
    
    final Link deleteProjectActionLink = new Link("deleteProjectActionLink") {
      private static final long serialVersionUID = 2387446194207003694L;

        public void onClick() {
            try {
              project.delete();
              getRequestCycle().setResponsePage(new UserHomePage());
            } catch (SimalRepositoryException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
        }
    };
    add(deleteProjectActionLink);
    
    try {
      RESTCommand cmd = RESTCommand.createGetProject(project.getSimalID(), RESTCommand.TYPE_SIMAL, RESTCommand.FORMAT_XML);
      add(new ExternalLink("doapLink", cmd.getURL()));
      String rdfLink = "<link href=\"" + cmd.getURL() + "\" rel=\"meta\" title=\"DOAP\" type=\"application/rdf+xml\" />";
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
    
    add(new Label("projectName", project.getName()));
    
    Label shortDesc = new Label("shortDesc", project.getShortDesc());
    shortDesc.setEscapeModelStrings(false);
    add(shortDesc);

    // details
    Label desc = new Label("description", project.getDescription());
    desc.setEscapeModelStrings(false);
    add(desc);
    try {
      add(new ReleasesPanel("releases", project.getReleases()));
    } catch (SimalRepositoryException e) {
      UserReportableException error = new UserReportableException(
          "Unable to get project releases from the repository",
          ExhibitProjectBrowserPage.class, e);
      setResponsePage(new ErrorReportPage(error));
    }
    add(getRepeatingLinks("homepages", "homepage",
        new SortableDoapResourceDataProvider(project.getHomepages()), false));

    // Community tools
    add(getRepeatingLinks("issueTrackers", "issueTracker", "Issue Tracker",
        new SortableDoapResourceDataProvider(project.getIssueTrackers()), false));
    add(getRepeatingLinks("mailingLists", "mailingList",
        new SortableDoapResourceDataProvider(project.getMailingLists()), false));
    add(getRepeatingLinks("wikis", "wiki", "Wiki",
        new SortableDoapResourceDataProvider(project.getWikis()), false));
    add(new SourceRepositoriesPanel("sourceRepositories", project
        .getRepositories()));
    add(getRepeatingLinks("screenshots", "screenshot", "Screenshot",
        new SortableDoapResourceDataProvider(project.getScreenshots()), false));

    // facets
    add(new CategoryListPanel("categoryList", project.getCategories()));
    add(getRepeatingLabels("OSes", "OS", project.getOSes()));
    add(getRepeatingLabels("programmingLanguages", "programmingLanguage",
        project.getProgrammingLanguages()));

    // contributors
    PersonListPanel maintainerList = new PersonListPanel("maintainers", "Maintainers", project
        .getMaintainers());
    maintainerList.setOutputMarkupId(true);
    add(maintainerList);
    add(new AddPersonPanel("addMaintainerPanel", getProject(), AddPersonPanel.MAINTAINER, maintainerList));
    
    PersonListPanel developerList = new PersonListPanel("developers", "Developers", project
        .getDevelopers());
    developerList.setOutputMarkupId(true);
    add(developerList);
    add(new AddPersonPanel("addDeveloperPanel", getProject(), AddPersonPanel.DEVELOPER, developerList));
    

    PersonListPanel testerList = new PersonListPanel("testers", "Testers", project
        .getTesters());
    testerList.setOutputMarkupId(true);
    add(testerList);
    add(new AddPersonPanel("addTesterPanel", getProject(), AddPersonPanel.TESTER, testerList));
        
    PersonListPanel helperList = new PersonListPanel("helpers", "Helpers", project
        .getHelpers());
    helperList.setOutputMarkupId(true);
    add(helperList);
    add(new AddPersonPanel("addHelperPanel", getProject(), AddPersonPanel.HELPER, helperList));
    

    PersonListPanel documentorList = new PersonListPanel("documenters", "Documentors", project
        .getDocumenters());
    documentorList.setOutputMarkupId(true);
    add(documentorList);
    add(new AddPersonPanel("addDocumentorPanel", getProject(), AddPersonPanel.DOCUMENTOR, documentorList));
    
    PersonListPanel translatorList = new PersonListPanel("translators", "Translators", project
        .getTranslators());
    translatorList.setOutputMarkupId(true);
    add(translatorList);
    add(new AddPersonPanel("addTranslatorPanel", getProject(), AddPersonPanel.TRANSLATOR, translatorList));

    // downlaod
    add(getRepeatingLinks("downloadPages", "downloadPage", "Downloads",
        new SortableDoapResourceDataProvider(project.getDownloadPages()), false));
    add(getRepeatingLinks("downloadMirrors", "downloadMirror",
        "Download Mirror", new SortableDoapResourceDataProvider(project
            .getDownloadMirrors()), false));
    
    // sources
    add(getRepeatingDataSourcePanel("sources", "seeAlso", project.getSources()));

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

}
