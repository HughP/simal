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


import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rest.RESTCommand;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserApplication;
import uk.ac.osswatch.simal.wicket.UserHomePage;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.data.SortableDoapResourceDataProvider;
import uk.ac.osswatch.simal.wicket.foaf.FoafFormInputModel;
import uk.ac.osswatch.simal.wicket.panel.CategoryListPanel;
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
          project = UserApplication.getRepository().findProjectById(id);
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
    }   
    
    add(new Label("projectName", project.getName()));
    add(new Label("shortDesc", project.getShortDesc()));

    // details
    add(new Label("description", project.getDescription()));
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
    add(getRepeatingPersonPanel("maintainers", "maintainer", project
        .getMaintainers()));
    add(new AddPersonContainer("addMaintainer"));
    
    add(getRepeatingPersonPanel("developers", "developer", project
        .getDevelopers()));
    add(getRepeatingPersonPanel("testers", "tester", project.getTesters()));
    add(getRepeatingPersonPanel("helpers", "helper", project.getHelpers()));
    add(getRepeatingPersonPanel("documenters", "documenter", project
        .getDocumenters()));
    add(getRepeatingPersonPanel("translators", "translator", project
        .getTranslators()));

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
  
  /**
   * Container for showing either the new person link, 
   * or the person form.
   */
  public class AddPersonContainer extends WebMarkupContainer {
    private static final long serialVersionUID = 8348295085251890400L;
    /** Visibility toggle so that either the link or the form is visible. */
    private boolean formVisible = false;
    private FoafFormInputModel inputModel = new FoafFormInputModel();
    TextField<String> nameField;
    TextField<String> emailField;
    FeedbackPanel feedback;
    
    public AddPersonContainer(String id) {
      super(id);
      setOutputMarkupId(true);
      add(new NewPersonLink("newLink"));
      add(new AddPersonForm("personForm"));
    }

    /**
     * Called when the new person link is clicked.
     * Shows the form, and hides the link.
     * 
     * @param target
     *            the request target.
     */
    void onShowPersonForm(AjaxRequestTarget target) {
        formVisible = true;
        target.addComponent(this);
    }

    /**
     * Called when the cancel link is clicked.
     * Hides the form, and shows the link.
     * 
     * @param target
     *            the request target.
     */
    void onHidePersonForm(AjaxRequestTarget target) {
        formVisible = false;
        target.addComponent(this);
    }
    
    /** 
     * Link for adding a person described in the form to
     * the repository.
     *
     */
    private final class AddPersonButton extends AjaxFallbackButton {
        /** Constructor. */
        private AddPersonButton(String id, Form form) {
            super(id, form);
        }

        /**
         * onclick handler.
         * 
         * @param target
         *            the request target.
         */
        @Override
        public void onSubmit(AjaxRequestTarget target, Form form) {
            IPerson person;
            inputModel.setName(nameField.getValue());
            inputModel.setEmail(emailField.getValue());
            try {
              person = inputModel.getPerson();
              project.addMaintainer(person);
            } catch (SimalRepositoryException e) {
              UserReportableException error = new UserReportableException(
                  "Unable to generate a person from the given form data",
                  ProjectDetailPage.class, e);
              setResponsePage(new ErrorReportPage(error));
            }
            onHidePersonForm(target);
        }

        protected void onError(AjaxRequestTarget target, Form form) {
            target.addComponent(feedback);
        }
    }
    
    /** Link for cancelling a new person action. */
    @SuppressWarnings("unchecked")
    private final class CancelLink extends AjaxFallbackLink {
      private static final long serialVersionUID = 8333095362462779919L;

      public CancelLink(String id) {
        super(id);
      }

      /**
       * When the link is clicked the form is shown and the link is hidden.
       * 
       * @param target
       *            the request target.
       */
      @Override
      public void onClick(AjaxRequestTarget target) {
          onHidePersonForm(target);
      }

    }
    
    /** Link for displaying the AddPersonForm. */
    @SuppressWarnings("unchecked")
    private final class NewPersonLink extends AjaxFallbackLink {
      private static final long serialVersionUID = 8333095362462779919L;

      public NewPersonLink(String id) {
        super(id);
      }

      /**
       * When the link is clicked the form is shown and the link is hidden.
       * 
       * @param target
       *            the request target.
       */
      @Override
      public void onClick(AjaxRequestTarget target) {
          onShowPersonForm(target);
      }
      
      @Override
      public boolean isVisible() {
          return !formVisible;
      }

    }
    
    /**
     * Displays a form for creating a person record.
     * The visibility of this component is mutually exclusive
     * with the visibility of the new person link.
     */
    private final class AddPersonForm extends Form<FoafFormInputModel> {
      private static final long serialVersionUID = 2931852197898067993L;
      
      public AddPersonForm(String id) {
        super(id, new CompoundPropertyModel<FoafFormInputModel>(inputModel));
        setOutputMarkupId(true);
        
        feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);
        
        add(nameField = new RequiredTextField<String>("name"));
        nameField.add(StringValidator.minimumLength(4));
        
        add(emailField = new RequiredTextField<String>("email"));
        emailField.add(EmailAddressValidator.getInstance());
        
        AjaxFormValidatingBehavior.addToAllFormComponents(this, "onkeyup", Duration.ONE_SECOND);
        
        add(new AddPersonButton("addPersonButton", this));
        add(new CancelLink("cancelLink"));
      }
      
      @Override
      public boolean isVisible() {
          return formVisible;
      }
    }
    
    /**
     * Cancel addition of a person.
     * @param target
     */
    void onCancel(AjaxRequestTarget target) {
      formVisible = false;
      target.addComponent(this);
    }
  }
}
