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
import java.util.Collections;
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
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
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
import uk.ac.osswatch.simal.model.IDoapLicence;
import uk.ac.osswatch.simal.model.IDocument;
import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.model.utils.DoapResourceByNameComparator;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.authentication.SimalSession;
import uk.ac.osswatch.simal.wicket.doap.ExhibitProjectBrowserPage;
import uk.ac.osswatch.simal.wicket.doap.ProjectDetailPage;
import uk.ac.osswatch.simal.wicket.foaf.AddPersonPanel;
import uk.ac.osswatch.simal.wicket.panel.CategoryListPanel;
import uk.ac.osswatch.simal.wicket.panel.PersonListPanel;
import uk.ac.osswatch.simal.wicket.panel.ReleasesPanel;
import uk.ac.osswatch.simal.wicket.panel.SelectCategoryInputModel;
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
  public EditProjectPanel(String id, IProject project) {
    super(id);
    this.project = project;
    this.readOnly = true;
    this.rosb = new ReadOnlyStyleBehavior();

    add(new EditProjectForm("editProjectForm",
        new CompoundPropertyModel<IProject>(project)));
    addPersonsColumn();
  }
  

  private void addPersonsColumn() {
    PersonListPanel maintainerList = new PersonListPanel("maintainers",
        "Maintainers", project.getMaintainers(), 4, project,
        AddPersonPanel.MAINTAINER);
    add(maintainerList);

    PersonListPanel developerList = new PersonListPanel("developers",
        "Developers", project.getDevelopers(), 7, project,
        AddPersonPanel.DEVELOPER);
    add(developerList);

    PersonListPanel testerList = new PersonListPanel("testers", "Testers",
        project.getTesters(), 4, project, AddPersonPanel.TESTER);
    add(testerList);

    PersonListPanel helperList = new PersonListPanel("helpers", "Helpers",
        project.getHelpers(), 4, project, AddPersonPanel.HELPER);
    add(helperList);

    PersonListPanel documentorList = new PersonListPanel("documenters",
        "Documentors", project.getDocumenters(), 4, project,
        AddPersonPanel.DOCUMENTOR);
    add(documentorList);

    PersonListPanel translatorList = new PersonListPanel("translators",
        "Translators", project.getTranslators(), 4, project,
        AddPersonPanel.TRANSLATOR);
    add(translatorList);
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
    }
    
    private void addLeftColumn() {
      add(new Label("projectName", project.getName()));

      TextField<String> shortDesc = new TextField<String>(
          "shortDesc", new PropertyModel<String>(project, "shortDesc"));
      shortDesc.setEscapeModelStrings(false);
      shortDesc.add(rosb);
      shortDesc.setOutputMarkupId(true);
      add(shortDesc);

      //this.homepages = project.getHomepages();
      DocumentSetPanel homepageList = new DocumentSetPanel(
          "homepageList", "Web Pages", project.getHomepages(), project) {
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
      add(homepageList);

      // Community tools
      //this.issueTrackers = project.getIssueTrackers();
      DocumentSetPanel issueTrackerList = new DocumentSetPanel(
          "issueTrackerList", "Issue Trackers", project.getIssueTrackers(), project) {
        private static final long serialVersionUID = -1120710361889351081L;

        public void addToModel(IDocument document) throws SimalException {
          getProject().addIssueTracker(document);
        }

        public void removeFromModel(IDocument document)
            throws SimalRepositoryException {
          getProject().removeIssueTracker(document);
        }
      };
      add(issueTrackerList);


      // FIXME Add mailing list panel project.getMailingLists()
//      DocumentSetPanel mailingListsPanel = new DocumentSetPanel(
//      "mailingLists", "Mailing lists", project.getWikis(), project) {
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
          "Wikis", project.getWikis(), project) {
        private static final long serialVersionUID = 4574870021749081067L;

        public void addToModel(IDocument document) throws SimalException {
          getProject().addWiki(document);
        }

        public void removeFromModel(IDocument document)
            throws SimalRepositoryException {
          getProject().removeWiki(document);
        }
      };
      add(wikiListPanel);

      // download
      //this.downloads = project.getDownloadPages();
      DocumentSetPanel downloadsListPanel = new DocumentSetPanel("downloadPagesList",
          "Downloads", project.getDownloadPages(), project) {
        private static final long serialVersionUID = -7922957837006958358L;

        public void addToModel(IDocument document) throws SimalException {
          getProject().addDownloadPage(document);
        }

        public void removeFromModel(IDocument document)
            throws SimalRepositoryException {
          getProject().removeDownloadPage(document);
        }
      };
      add(downloadsListPanel);
      
      //this.downloadMirrors = project.getDownloadMirrors();
      DocumentSetPanel downloadMirrorsListPanel = new DocumentSetPanel("downloadMirrorsList",
          "Download Mirrors", project.getDownloadMirrors(), project) {
        private static final long serialVersionUID = -6222494226582096467L;

        public void addToModel(IDocument document) throws SimalException {
          getProject().addDownloadMirror(document);
        }

        public void removeFromModel(IDocument document)
            throws SimalRepositoryException {
          getProject().removeDownloadMirror(document);
        }
      };
      add(downloadMirrorsListPanel);
      
      try {
        SourceRepositoriesPanel sourceRepositoriesPanel = new SourceRepositoriesPanel("sourceRepositories", 
            "Source Repositories", project.getRepositories(), rosb, project); 
        add(sourceRepositoriesPanel);
      } catch (SimalRepositoryException e) {
        UserReportableException error = new UserReportableException(
            "Unable to get project releases from the repository",
            ExhibitProjectBrowserPage.class, e);
        setResponsePage(new ErrorReportPage(error));
      }
      
      
//      this.screenshots = project.getScreenshots();
      DocumentSetPanel screenshotsListPanel = new DocumentSetPanel("screenshotsList",
          "Screenshots", project.getScreenshots(), project) {
        private static final long serialVersionUID = -7837486995158569663L;

        public void addToModel(IDocument document) throws SimalException {
          getProject().addScreenshot(document);
        }

        public void removeFromModel(IDocument document)
            throws SimalRepositoryException {
          getProject().removeScreenshot(document);
        }
      };
      add(screenshotsListPanel);

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
          return SimalSession.get().isAuthenticated();
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
          return (!readOnly && SimalSession.get().isAuthenticated());
        }

      };
      cancelButton.setDefaultFormProcessing(false);
      add(cancelButton);
      add(new ReleasesPanel("releasepanel", project.getReleases(), rosb));

      CategoryListPanel categoryList = new CategoryListPanel("categoryList",
          "Categories", project.getCategories(), project);
      add(categoryList);

      //this.oses = project.getOSes();
      addRepeatingInputs("OSes", project.getOSes());

      //this.langs = project.getProgrammingLanguages();
      addRepeatingInputs("programmingLanguages", project.getProgrammingLanguages());

      try {
        addLicences(project.getLicences());
      } catch (SimalRepositoryException e) {
        LOGGER.warn("Could not get licences from repository. ",e);
      }
    }
        private void addLicences(Set<IDoapLicence> licences) throws SimalRepositoryException {
      
      List<SelectCategoryInputModel<IDoapLicence>> data = new ArrayList<SelectCategoryInputModel<IDoapLicence>>();

      final List<IDoapLicence> allDoapLicences = new ArrayList<IDoapLicence>(SimalRepositoryFactory
            .getInstance().getAllLicences());
      Collections.sort(allDoapLicences, new DoapResourceByNameComparator());
      
      for(IDoapLicence licence : licences) {
        SelectCategoryInputModel<IDoapLicence> inputModel = new SelectCategoryInputModel<IDoapLicence>();
        inputModel.setComboChoice(licence);
        data.add(inputModel);
      }

      // Wrap all licence fields in a list:
      ListView<SelectCategoryInputModel<IDoapLicence>> listView = new ListView<SelectCategoryInputModel<IDoapLicence>>(
          "licences", data) {

        private static final long serialVersionUID = 8106669931357952797L;

        protected void populateItem(ListItem<SelectCategoryInputModel<IDoapLicence>> item) {
          SelectCategoryInputModel<IDoapLicence> inputModel = (SelectCategoryInputModel<IDoapLicence>) item
              .getModelObject();
          DropDownChoice<IDoapLicence> licencesField;
          licencesField = new DropDownChoice<IDoapLicence>("licence",
              new PropertyModel<IDoapLicence>(inputModel, "comboChoice"),
              allDoapLicences, new ChoiceRenderer<IDoapLicence>("name", "uri")) {

            private static final long serialVersionUID = -3911050158593221477L;

            protected boolean wantOnSelectionChangedNotifications() {
              return false;
            }
          };
          
          item.add(licencesField);
        }
      };
      listView.setReuseItems(true);
      add(listView);
      
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

      List<String> editableList = new ArrayList<String>(labels);
      editableList.add(NEW_ITEM);
      GenericSetWrapper<String> editableListWrapper = new GenericSetWrapper<String>(new ArrayList<String>(editableList));
      

      ListView<String> listView = new ListView<String>(
          labelWicketID, new PropertyModel<List<String>>(editableListWrapper, "editableList")) {
        private static final long serialVersionUID = 154815894763179933L;

        protected void populateItem(ListItem<String> item) {
          String wrapper = (String) item.getModelObject();
          TextField<String> setItemValue = new TextField<String>(
              "setItemValue", item.getModel());
          setItemValue.add(new ReadOnlyStyleBehavior());
          item.add(setItemValue);
          item.add(generateDeleteItemButton(!NEW_ITEM
              .equals(wrapper), item));
        }
      };
      listView.setReuseItems(true);
      add(listView);
    }


    private AjaxFallbackButton generateDeleteItemButton(
        boolean visibilityAllowed, ListItem<String> item) {
      AjaxFallbackButton deleteItemButton = new AjaxFallbackDeleteItemButton(
          "deleteItem", new Model<String>("X"), this, item);

      deleteItemButton.setVisibilityAllowed(visibilityAllowed);
      deleteItemButton.add(new ReadOnlyStyleBehavior());
      return deleteItemButton;

    }

  }

  private static class AjaxFallbackDeleteItemButton extends AjaxFallbackButton {

    private static final long serialVersionUID = -6395239712922873605L;

    private ListItem<String> item;

    /**
     * @param id
     * @param model
     * @param form
     */
    public AjaxFallbackDeleteItemButton(String id, IModel<String> model,
        Form<?> form, ListItem<String> item) {
      super(id, model, form);
      this.item = item;
    }

    @Override
    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
      // FIXME item.getModel().getObject().setValue(null);
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