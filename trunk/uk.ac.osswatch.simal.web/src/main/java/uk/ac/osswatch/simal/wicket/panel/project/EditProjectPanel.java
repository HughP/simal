package uk.ac.osswatch.simal.wicket.panel.project;

/*
 * Copyright 2010 University of Oxford
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.IDocument;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.IResource;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.doap.ExhibitProjectBrowserPage;
import uk.ac.osswatch.simal.wicket.doap.ProjectDetailPage;
import uk.ac.osswatch.simal.wicket.foaf.AddPersonPanel;
import uk.ac.osswatch.simal.wicket.panel.CategoryListPanel;
import uk.ac.osswatch.simal.wicket.panel.PersonListPanel;
import uk.ac.osswatch.simal.wicket.panel.ReleasesPanel;
import uk.ac.osswatch.simal.wicket.panel.SourceRepositoriesPanel;

/**
 * Container for adding a new category. This is an AJAX enabled container that
 * either shows the form for selecting a category or a command link to display
 * the form.
 */
public class EditProjectPanel extends Panel {

  private static final long serialVersionUID = -7126291323723696950L;

  public static final Logger LOGGER = LoggerFactory
      .getLogger(EditProjectPanel.class);

  public static final String NEW_ITEM = "<<new>>";

  private IProject project;
  private ReadOnlyStyleBehavior rosb;

  private boolean loggedIn;
  private boolean readOnly;
  
  /**
   * Create a new container that will initially display the command link to show
   * the form.
   * 
   * @param wicketid
   *          the id of the HTML component to host the container
   * @param updatePanel
   *          the panel that should be updated when the category has been added
   *          (must have setOutputMarkupId(true)
   */
  public EditProjectPanel(String id, IProject project, boolean loggedIn) {
    super(id);
    this.project = project;
    this.loggedIn = loggedIn;
    this.readOnly = true;
    this.rosb = new ReadOnlyStyleBehavior();

    add(new EditProjectForm("editProjectForm",
        new CompoundPropertyModel<IProject>(project)));
  }

  /**
   * Add category to the project and to the existing list.
   * 
   * @see uk.ac.osswatch.simal.wicket.panel.AbstractAddPanel#processAddSubmit()
   */
  protected void processAddSubmit() {

  }

  private class EditProjectForm extends Form<IProject> {
    private static final long serialVersionUID = 5903165424353929310L;

    private Set<AbstractEditableResourcesPanel<? extends IResource>> editablePanels = new HashSet<AbstractEditableResourcesPanel<? extends IResource>>();

    private TextArea<String> description;
    private AjaxFallbackButton submitButton;

//    private Set<String> oses;
//    private Set<String> langs;
//    private Set<IDocument> homepages;
//    private Set<IDoapMailingList> mailingLists;
//    private Set<IDocument> issueTrackers;
//    private Set<IDocument> wikis;
//    private Set<IDocument> downloads;
//    private Set<IDocument> downloadMirrors;
//    private Set<IDocument> screenshots;

    public EditProjectForm(String id, IModel<IProject> model) {
      super(id, model);
      addFormFields(model);
      setOutputMarkupId(true);
    }

    private void toggleEditMode() {
      readOnly = !readOnly;
      if (readOnly) {
        submitButton.getModel().setObject("Edit");
      } else {
        submitButton.getModel().setObject("Save");
      }
      for (AbstractEditableResourcesPanel<? extends IResource> panel : editablePanels) {
        panel.setEditingOn(!readOnly);
      }
    }

    private void addEditablePanel(AbstractEditableResourcesPanel<? extends IResource> panel) {
      editablePanels.add(panel);
      add(panel);
    }

    /**
     * 
     * 
     * @see uk.ac.osswatch.simal.wicket.panel.AbstractAddPanel#addFormFields(uk.ac.
     *      osswatch.simal.wicket.panel.AbstractAddPanel.AddDoapResourceForm)
     */
    private void addFormFields(IModel<IProject> model) {
      addLeftColumn();
      addRightColumn();
      addPersonsColumn();
    }
    
    private void addLeftColumn() {
      add(new Label("projectName", project.getName()));

      Label shortDesc = new Label("shortDesc", project.getShortDesc());
      shortDesc.setEscapeModelStrings(false);
      shortDesc.add(rosb);
      shortDesc.setOutputMarkupId(true);
      add(shortDesc);

      //this.homepages = project.getHomepages();
      DocumentSetPanel homepageList = new DocumentSetPanel(
          "homepageList", "Web Pages", project.getHomepages(), loggedIn, project) {
        private static final long serialVersionUID = -6849401011037784163L;

        public void addToModel(IDocument document)
            throws SimalException {
          getProject().addHomepage(document);
        }

        public void removeFromModel(IDocument document)
            throws SimalRepositoryException {
          getProject().removeHomepage(document);
        }
      };
      addEditablePanel(homepageList);

      // Community tools
      //this.issueTrackers = project.getIssueTrackers();
      DocumentSetPanel issueTrackerList = new DocumentSetPanel(
          "issueTrackerList", "Issue Trackers", project.getIssueTrackers(), loggedIn, project) {
        private static final long serialVersionUID = -1120710361889351081L;

        public void addToModel(IDocument document) throws SimalException {
          getProject().addIssueTracker(document);
        }

        public void removeFromModel(IDocument document)
            throws SimalRepositoryException {
          getProject().removeIssueTracker(document);
        }
      };
      addEditablePanel(issueTrackerList);


      // FIXME Add mailing list panel project.getMailingLists()
//      DocumentSetPanel mailingListsPanel = new DocumentSetPanel(
//      "mailingLists", "Mailing lists", project.getWikis(), loggedIn, project) {
//        
//        private static final long serialVersionUID = 7634775797786719275L;
//
//        public void addToModel(IDocument document) throws SimalException {
//          if (document != null && document instanceof Document)  {
//            Object resource = ((Document)document).getRepositoryResource();
//            if (resource instanceof Resource) {
//              IDoapMailingList newList = new MailingList((Resource)resource);
//              getProject().addMailingList(newList);
//            }
//          }
//        }
//
//        public void removeFromModel(IDocument document)
//            throws SimalRepositoryException {
//          getProject().removeWiki(document);
//        }
//      };
//       add(mailingListsPanel);
      
      // this.wikis = project.getWikis();
      DocumentSetPanel wikiListPanel = new DocumentSetPanel("wikiLists",
          "Wikis", project.getWikis(), loggedIn, project) {
        private static final long serialVersionUID = 4574870021749081067L;

        public void addToModel(IDocument document) throws SimalException {
          getProject().addWiki(document);
        }

        public void removeFromModel(IDocument document)
            throws SimalRepositoryException {
          getProject().removeWiki(document);
        }
      };
      addEditablePanel(wikiListPanel);

      // download
      //this.downloads = project.getDownloadPages();
      DocumentSetPanel downloadsListPanel = new DocumentSetPanel("downloadPagesList",
          "Downloads", project.getDownloadPages(), loggedIn, project) {
        private static final long serialVersionUID = -7922957837006958358L;

        public void addToModel(IDocument document) throws SimalException {
          getProject().addDownloadPage(document);
        }

        public void removeFromModel(IDocument document)
            throws SimalRepositoryException {
          getProject().removeDownloadPage(document);
        }
      };
      addEditablePanel(downloadsListPanel);
      
      //this.downloadMirrors = project.getDownloadMirrors();
      DocumentSetPanel downloadMirrorsListPanel = new DocumentSetPanel("downloadMirrorsList",
          "Download Mirrors", project.getDownloadMirrors(), loggedIn, project) {
        private static final long serialVersionUID = -6222494226582096467L;

        public void addToModel(IDocument document) throws SimalException {
          getProject().addDownloadMirror(document);
        }

        public void removeFromModel(IDocument document)
            throws SimalRepositoryException {
          getProject().removeDownloadMirror(document);
        }
      };
      addEditablePanel(downloadMirrorsListPanel);
      
      try {
        SourceRepositoriesPanel sourceRepositoriesPanel = new SourceRepositoriesPanel("sourceRepositories", 
            "Source Repositories", project.getRepositories(), rosb, loggedIn, project); 
        addEditablePanel(sourceRepositoriesPanel);
      } catch (SimalRepositoryException e) {
        UserReportableException error = new UserReportableException(
            "Unable to get project releases from the repository",
            ExhibitProjectBrowserPage.class, e);
        setResponsePage(new ErrorReportPage(error));
      }
      
      
//      this.screenshots = project.getScreenshots();
      DocumentSetPanel screenshotsListPanel = new DocumentSetPanel("screenshotsList",
          "Screenshots", project.getScreenshots(), loggedIn, project) {
        private static final long serialVersionUID = -7837486995158569663L;

        public void addToModel(IDocument document) throws SimalException {
          getProject().addScreenshot(document);
        }

        public void removeFromModel(IDocument document)
            throws SimalRepositoryException {
          getProject().removeScreenshot(document);
        }
      };
      addEditablePanel(screenshotsListPanel);

    }

    private void addRightColumn() {
      description = new TextArea<String>("description");
      add(description);
      description.setOutputMarkupId(true);
      description.add(rosb);

      String[] defaultValue = { project.getDescription() };
      description.setModelValue(defaultValue);

      submitButton = new AjaxFallbackButton("editProjectActionLink",
          new Model<String>("Edit"), this) {
        private static final long serialVersionUID = 384116828159132608L;

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
          toggleEditMode();
          target.addComponent(form);
        }

        @Override
        public boolean isVisible() {
          return loggedIn;
        }

      };
      add(submitButton);

      Button cancelButton = new AjaxFallbackButton("cancelProjectActionLink",
          new Model<String>("Cancel"), this) {
        private static final long serialVersionUID = -7941561946368787393L;

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
          try {
            PageParameters projectPageParams = new PageParameters();
            projectPageParams.add("simalID", project.getSimalID());
            setResponsePage(ProjectDetailPage.class, projectPageParams);
          } catch (SimalRepositoryException e) {
            setResponsePage(new ProjectDetailPage(project));
          }
        }

        @Override
        public boolean isVisible() {
          return (loggedIn && !readOnly);
        }

      };
      cancelButton.setDefaultFormProcessing(false);
      add(cancelButton);
      add(new ReleasesPanel("releasepanel", project.getReleases(), rosb));

      CategoryListPanel categoryList = new CategoryListPanel("categoryList",
          "Categories", project.getCategories(), loggedIn, project);
      addEditablePanel(categoryList);

      //this.oses = project.getOSes();
      addRepeatingInputs("OSes", project.getOSes());

      //this.langs = project.getProgrammingLanguages();
      addRepeatingInputs("programmingLanguages", project.getProgrammingLanguages());
      
    }
    
    private void addPersonsColumn() {
      PersonListPanel maintainerList = new PersonListPanel("maintainers",
          "Maintainers", project.getMaintainers(), 4, project,
          AddPersonPanel.MAINTAINER, loggedIn);
      addEditablePanel(maintainerList);

      PersonListPanel developerList = new PersonListPanel("developers",
          "Developers", project.getDevelopers(), 7, project,
          AddPersonPanel.DEVELOPER, loggedIn);
      addEditablePanel(developerList);

      PersonListPanel testerList = new PersonListPanel("testers", "Testers",
          project.getTesters(), 4, project, AddPersonPanel.TESTER,
          loggedIn);
      addEditablePanel(testerList);

      PersonListPanel helperList = new PersonListPanel("helpers", "Helpers",
          project.getHelpers(), 4, project, AddPersonPanel.HELPER,
          loggedIn);
      addEditablePanel(helperList);

      PersonListPanel documentorList = new PersonListPanel("documenters",
          "Documentors", project.getDocumenters(), 4, project,
          AddPersonPanel.DOCUMENTOR, loggedIn);
      addEditablePanel(documentorList);

      PersonListPanel translatorList = new PersonListPanel("translators",
          "Translators", project.getTranslators(), 4, project,
          AddPersonPanel.TRANSLATOR, loggedIn);
      addEditablePanel(translatorList);
    }

    /**
     * Get a simple repeating view. Each resource in the supplied set will be
     * represented in the list using the supplied string as a label.
     * 
     * @param labelWicketID
     *          the wicket:id of the label within each list item
     * @param resources
     *          the resources to be added to the list
     * @return
     */
    private void addRepeatingInputs(String labelWicketID, Set<String> labels) {
      Iterator<String> itr = labels.iterator();
      List<GenericSetWrapper<String>> data = new ArrayList<GenericSetWrapper<String>>();

      while (itr.hasNext()) {
        GenericSetWrapper<String> gsw = new GenericSetWrapper<String>(labels,
            itr.next());
        data.add(gsw);
      }

      // Add empty one for new
      data.add(new GenericSetWrapper<String>(labels, NEW_ITEM));

      ListView<GenericSetWrapper<String>> listView = new ListView<GenericSetWrapper<String>>(
          labelWicketID, data) {
        private static final long serialVersionUID = 154815894763179933L;

        protected void populateItem(ListItem<GenericSetWrapper<String>> item) {
          GenericSetWrapper<String> wrapper = (GenericSetWrapper<String>) item
              .getModelObject();
          TextField<String> setItemValue = new TextField<String>(
              "setItemValue", new PropertyModel<String>(wrapper, "value"));
          setItemValue.add(new ReadOnlyStyleBehavior());
          item.add(setItemValue);
          item.add(generateDeleteItemButton(!NEW_ITEM
              .equals(wrapper.getValue()), item));
        }
      };
      listView.setReuseItems(true);
      add(listView);
    }

    private AjaxFallbackButton generateDeleteItemButton(
        boolean visibilityAllowed, ListItem<GenericSetWrapper<String>> item) {
      AjaxFallbackButton deleteItemButton = new AjaxFallbackDeleteItemButton(
          "deleteItem", new Model<String>("X"), this, item);

      deleteItemButton.setVisibilityAllowed(visibilityAllowed);
      deleteItemButton.add(new ReadOnlyStyleBehavior());
      return deleteItemButton;

    }

  }

  private static class AjaxFallbackDeleteItemButton extends AjaxFallbackButton {

    private static final long serialVersionUID = -6395239712922873605L;

    private ListItem<GenericSetWrapper<String>> item;

    /**
     * @param id
     * @param model
     * @param form
     */
    public AjaxFallbackDeleteItemButton(String id, IModel<String> model,
        Form<?> form, ListItem<GenericSetWrapper<String>> item) {
      super(id, model, form);
      this.item = item;
    }

    @Override
    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
      item.getModel().getObject().setValue(null);
      item.setVisible(false);
      setVisible(false);
      target.addComponent(form);
    }
  }

  public class ReadOnlyStyleBehavior extends AbstractBehavior {

    private static final long serialVersionUID = 9109967060661070046L;

    public void onComponentTag(final Component component, final ComponentTag tag) {

      if (readOnly) {
        tag.getAttributes().put("readonly", "readonly");
        tag.getAttributes().put("class", "readonly");
        if (StringEscapeUtils.escapeXml(NEW_ITEM).equals(
            tag.getAttribute("value"))) {
          tag.getAttributes().put("style", "display:none");
        }
      } else {
        tag.getAttributes().remove("readonly");
        tag.getAttributes().put("class", "editable");
        if (StringEscapeUtils.escapeXml(NEW_ITEM).equals(
            tag.getAttribute("value"))) {
          tag.getAttributes().put("style", "display:block");
        }
      }

    }
  }
}