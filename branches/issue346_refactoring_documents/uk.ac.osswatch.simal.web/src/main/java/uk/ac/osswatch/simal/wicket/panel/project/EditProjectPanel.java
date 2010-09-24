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

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IDocument;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.doap.ExhibitProjectBrowserPage;
import uk.ac.osswatch.simal.wicket.doap.ProjectDetailPage;
import uk.ac.osswatch.simal.wicket.panel.AddCategoryPanel;
import uk.ac.osswatch.simal.wicket.panel.CategoryListPanel;
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
  private boolean isReadOnly;

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
    this.isReadOnly = true;
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
    private TextArea<String> description;
    private AjaxFallbackButton submitButton;

    private Set<String> oses;
    private Set<String> langs;
    private Set<IDocument> homepages;
    //private Set<IDoapMailingList> mailingLists;
    private Set<IDocument> issueTrackers;
    private Set<IDocument> wikis;
    private Set<IDocument> downloads;
    private Set<IDocument> downloadMirrors;
    private Set<IDocument> screenshots;

    public EditProjectForm(String id, IModel<IProject> model) {
      super(id, model);
      addFormFields(model);
      setOutputMarkupId(true);
    }

    private void toggleEditMode() {
      isReadOnly = !isReadOnly;
      if (isReadOnly) {
        submitButton.getModel().setObject("Edit");
      } else {
        submitButton.getModel().setObject("Save");
      }
    }

    /**
     * 
     * 
     * @see uk.ac.osswatch.simal.wicket.panel.AbstractAddPanel#addFormFields(uk.ac.
     *      osswatch.simal.wicket.panel.AbstractAddPanel.AddDoapResourceForm)
     */
    private void addFormFields(IModel<IProject> model) {
      addLeftColumn(model);
      description = new TextArea<String>("description");
      add(description);
      description.setOutputMarkupId(true);
      description.add(new ReadOnlyStyleBehavior());

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
          return (loggedIn && !isReadOnly);
        }

      };
      cancelButton.setDefaultFormProcessing(false);
      add(cancelButton);
      add(new ReleasesPanel("releasepanel", project.getReleases(), rosb));

      CategoryListPanel categoryList = new CategoryListPanel("categoryList",
          project.getCategories());
      categoryList.setOutputMarkupId(true);
      add(categoryList);
      add(new AddCategoryPanel("addCategoryPanel", project, categoryList));

      this.oses = project.getOSes();
      addRepeatingInputs("OSes", this.oses);

      this.langs = project.getProgrammingLanguages();
      addRepeatingInputs("programmingLanguages", this.langs);
    }

    /**
     * @param model
     */
    private void addLeftColumn(IModel<IProject> model) {
      add(new Label("projectName", project.getName()));

      Label shortDesc = new Label("shortDesc", project.getShortDesc());
      shortDesc.setEscapeModelStrings(false);
      add(shortDesc);

      this.homepages = project.getHomepages();
      GenericIResourceSetPanel homepageList = new GenericIResourceSetPanel(
          "homepageList", "Web Pages", homepages, loggedIn, project) {
        private static final long serialVersionUID = -6849401011037784163L;

        public void processAdd(IDoapResourceFormInputModel inputModel)
            throws SimalRepositoryException {
          IDocument homepage = SimalRepositoryFactory.getHomepageService()
              .getOrCreate(inputModel.getUrl());
          homepage.setLabel(inputModel.getName());

          add(homepage);
          getProject().addHomepage(homepage);
        }

        public void processDelete(IDocument iDoapResource)
            throws SimalRepositoryException {
          delete(iDoapResource);
          getProject().removeHomepage((IDocument) iDoapResource);
        }
      };
      add(homepageList);

      // Community tools
      this.issueTrackers = project.getIssueTrackers();
      GenericIResourceSetPanel issueTrackerList = new GenericIResourceSetPanel("issueTrackerList",
          "Issue Trackers", issueTrackers);
      add(issueTrackerList);


      // FIXME Add mailing list panel
      // this.mailingLists = project.getMailingLists();
      // GenericIResourceSetPanel mailingListsPanel = new
      // GenericIResourceSetPanel(
      // "mailingLists", "Mailing lists", mailingLists);
      //
      // add(mailingListsPanel);
      
      this.wikis = project.getWikis();
      GenericIResourceSetPanel wikiListPanel = new GenericIResourceSetPanel("wikiLists",
          "Wikis", wikis);
      add(wikiListPanel);

      // download
      this.downloads = project.getDownloadPages();
      GenericIResourceSetPanel downloadsListPanel = new GenericIResourceSetPanel("downloadPagesList",
          "Downloads", downloads);
      add(downloadsListPanel);
      
      this.downloadMirrors = project.getDownloadMirrors();
      GenericIResourceSetPanel downloadMirrorsListPanel = new GenericIResourceSetPanel("downloadMirrorsList",
          "Download Mirrors", downloadMirrors);
      add(downloadMirrorsListPanel);
      
      try {
        add(new SourceRepositoriesPanel("sourceRepositories", project
            .getRepositories(), rosb));
      } catch (SimalRepositoryException e) {
        UserReportableException error = new UserReportableException(
            "Unable to get project releases from the repository",
            ExhibitProjectBrowserPage.class, e);
        setResponsePage(new ErrorReportPage(error));
      }
      
      
      this.screenshots = project.getScreenshots();
      GenericIResourceSetPanel screenshotsListPanel = new GenericIResourceSetPanel("screenshotsList",
          "Screenshots", screenshots);
      add(screenshotsListPanel);

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

      if (isReadOnly) {
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