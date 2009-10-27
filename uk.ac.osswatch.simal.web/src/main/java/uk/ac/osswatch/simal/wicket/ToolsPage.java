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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.file.Folder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.importData.PTSWImport;
import uk.ac.osswatch.simal.importData.Pims;
import uk.ac.osswatch.simal.model.ModelSupport;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.tools.Ohloh;
import uk.ac.osswatch.simal.wicket.tools.OhlohFormInputModel;

/**
 * The tools page provides access to a number of useful Admin tools.
 */
public class ToolsPage extends BasePage {
  private static final long serialVersionUID = -3723085497057001876L;
  private static final Logger logger = LoggerFactory.getLogger(ToolsPage.class);
  private static OhlohFormInputModel inputModel = new OhlohFormInputModel();

  public ToolsPage() {

    // Repository Stats
    try {
      add(new Label("numOfProjects", Integer.toString(UserApplication
          .getRepository().getAllProjects().size())));
      add(new Label("numOfPeople", Integer.toString(SimalRepositoryFactory.getPersonService().getAll().size())));
      add(new Label("numOfCategories", Integer.toString(SimalRepositoryFactory.getCategoryService().getAll().size())));
      add(new Label("numOfReviews", Integer.toString(SimalRepositoryFactory.getReviewService().getReviews().size())));
    } catch (SimalRepositoryException e) {
      UserReportableException error = new UserReportableException(
          "Unable to get repository statistics", ToolsPage.class, e);
      setResponsePage(new ErrorReportPage(error));
    }

    // Repository Config
    try {
      add(new Label("instanceID", SimalProperties
          .getProperty(SimalProperties.PROPERTY_SIMAL_INSTANCE_ID)));
      add(new Label("propertiesFile", SimalProperties.getLocalPropertiesFile()
          .toString()));
      add(new Label("repositoryDir", SimalProperties
          .getProperty(SimalProperties.PROPERTY_RDF_DATA_DIR)));
      if (UserApplication.getScheduledPtswStatus() ) {
        add(new Label("PTSWUpdaterStatus", "True"));
      } else  {
        add(new Label("PTSWUpdaterStatus", "False"));
      }


      add(new Link("toggleImportPTSWLink") {

        public void onClick() {
          UserApplication.setScheduledPtswStatus(!UserApplication.getScheduledPtswStatus());
          setResponsePage(new ToolsPage());
        }
      });
    } catch (SimalRepositoryException e) {
      UserReportableException error = new UserReportableException(
          "Unable to get repository configuration data", ToolsPage.class, e);
      setResponsePage(new ErrorReportPage(error));
    }

    add(new Link("removeAllData") {

      public void onClick() {
        try {
          removeAllData();
          setResponsePage(new ToolsPage());
        } catch (UserReportableException e) {
          setResponsePage(new ErrorReportPage(e));
        }
      }
    });

    add(new Link("importTestData") {

      public void onClick() {
        try {
          importTestData();
          setResponsePage(new ToolsPage());
        } catch (UserReportableException e) {
          setResponsePage(new ErrorReportPage(e));
        }
      }
    });

    add(new ImportFromOhlohForm("importFromOhlohForm"));
    
    final PimsUploadForm pimsProgrammesUploadForm = new PimsUploadForm("importProgrammesFromPimsForm", PimsUploadForm.PROGRAMMES);
    pimsProgrammesUploadForm.add(new UploadProgressBar("uploadProgress",
        pimsProgrammesUploadForm));
    add(pimsProgrammesUploadForm);
    
    final PimsUploadForm pimsProjectsUploadForm = new PimsUploadForm("importProjectsFromPimsForm", PimsUploadForm.PROJECTS);
    pimsProjectsUploadForm.add(new UploadProgressBar("uploadProgress",
        pimsProjectsUploadForm));
    add(pimsProjectsUploadForm);
    
    final PimsUploadForm pimsProjectContactsUploadForm = new PimsUploadForm("importProjectContactsFromPimsForm", PimsUploadForm.PROJECT_CONTACTS);
    pimsProjectContactsUploadForm.add(new UploadProgressBar("uploadProgress",
        pimsProjectContactsUploadForm));
    add(pimsProjectContactsUploadForm);
    
    add(new Link("importPTSWLink") {
      private static final long serialVersionUID = -6938957715376331902L;

      public void onClick() {
        try {
          importPTSW();
          setResponsePage(new ToolsPage());
        } catch (UserReportableException e) {
          setResponsePage(new ErrorReportPage(e));
        }
      }
    });
  }

  /**
   * Remove all data from the repository.
   * 
   * @throws UserReportableException
   */
  private void removeAllData() throws UserReportableException {
    ISimalRepository repo;
    try {
      repo = UserApplication.getRepository();
    } catch (SimalRepositoryException e) {
      throw new UserReportableException("Unable to get the count of projects",
          ToolsPage.class, e);
    }
    repo.removeAllData();
  }

  private void importTestData() throws UserReportableException {
    ISimalRepository repo;
    try {
      repo = UserApplication.getRepository();
    } catch (SimalRepositoryException e) {
      throw new UserReportableException("Unable to import test data",
          ToolsPage.class, e);
    }
    try {
		ModelSupport.addTestData(repo);
	} catch (Exception e) {
		throw new UserReportableException("Unable to add test data: " + e.getMessage(), ToolsPage.class);
	}
  }
  
  private void importPTSW() throws UserReportableException {
    PTSWImport importer = new PTSWImport();
    try {
		importer.importLatestDOAP();
	} catch (SimalRepositoryException e) {
	    throw new UserReportableException("Unable to import data into the repostory",
		        ToolsPage.class, e);
	} catch (SimalException e) {
	    throw new UserReportableException("Unable to import data from PTSW",
	        ToolsPage.class, e);
	} catch (IOException e) {
	    throw new UserReportableException("Unable to retrive data from PTSW",
	        ToolsPage.class, e);
	}
  }

  private static class ImportFromOhlohForm extends Form<OhlohFormInputModel> {
    private static final long serialVersionUID = 4350446873545711199L;

    public ImportFromOhlohForm(String name) {
      super(name, new CompoundPropertyModel<OhlohFormInputModel>(inputModel));
      TextField<String> idField = new TextField<String>("projectID");
	  add(idField);
      String[] defaultValue = { "" };
      idField.setModelValue(defaultValue);
    }

    @Override
    protected void onSubmit() {
      super.onSubmit();

      if (!this.hasError()) {
        try {
          Ohloh importer = new Ohloh();
          importer.addProjectToSimal(inputModel.getProjectID());
        } catch (SimalException e) {
          setResponsePage(new ErrorReportPage(new UserReportableException(
              "Unable to import from Ohloh", ToolsPage.class, e)));
        }
      }
    }
  }

  private static class PimsUploadForm extends Form<FileUploadField> {
	    private static final long serialVersionUID = 1L;
		public static final int PROGRAMMES = 10;
		public static final int PROJECTS = 20;
		public static final int PROJECT_CONTACTS = 30;
		private FileUploadField fileUploadField;
		private int type;

	    /**
	     * Simple constructor.
	     * 
	     * @param name
	     *          Component name
	     * @param i 
	     */
	    public PimsUploadForm(String name, int type) {
	      super(name);
	      setMultiPart(true);
	      add(fileUploadField = new FileUploadField("fileInput"));
	      this.type = type;
	    }

	    /**
	     * @see org.apache.wicket.markup.html.form.Form#onSubmit()
	     */
	    protected void onSubmit() {
	      super.onSubmit();

	      if (!this.hasError()) {
	        final FileUpload upload = fileUploadField.getFileUpload();
	        if (upload != null) {
	          File newFile = new File(getUploadFolder(), upload.getClientFileName());

	          if (newFile.exists()) {
	            if (!Files.remove(newFile)) {
	              throw new IllegalStateException("Unable to overwrite "
	                  + newFile.getAbsolutePath());
	            }
	          }

	          try {
	            boolean success = newFile.createNewFile();
	            if (!success) {
	              logger.warn("Trying ot create a file that already exists: "
	                  + newFile);
	            }
	            upload.writeTo(newFile);
	            logger.info("Uploaded PIMS export saved to " + upload.getClientFileName());
	          } catch (IOException e) {
	            throw new IllegalStateException("Unable to write file");
	          }

	          try {
        	    switch (type) {
          	      case PROGRAMMES:
					Pims.importProgrammes(newFile.toURI().toURL());
	                break;
        	      case PROJECTS:
	                Pims.importProjects(newFile.toURI().toURL());
	                break;
        	      case PROJECT_CONTACTS:
	                Pims.importProjectContacts(newFile.toURI().toURL());
	                break;
    		      default:
    			    setResponsePage(new ErrorReportPage(new UserReportableException(
    		                "Illegal type setting for PIMS uploader", ToolsPage.class)));
    		    }
	            setResponsePage(new ToolsPage());
			  } catch (FileNotFoundException e) {
		        setResponsePage(new ErrorReportPage(new UserReportableException(
			                "Cannot find PIMS data file to import", ToolsPage.class, e)));
			  } catch (MalformedURLException e) {
			        setResponsePage(new ErrorReportPage(new UserReportableException(
			                "Cannot find PIMS data file to import", ToolsPage.class, e)));
			  } catch (IOException e) {
			        setResponsePage(new ErrorReportPage(new UserReportableException(
			                "Cannot read PIMS data file to import", ToolsPage.class, e)));
			  } catch (DuplicateURIException e) {
			        setResponsePage(new ErrorReportPage(new UserReportableException(
			                "Cannot import PIMS data file", ToolsPage.class, e)));
			  } catch (SimalException e) {
			        setResponsePage(new ErrorReportPage(new UserReportableException(
			                "Cannot import PIMS data file", ToolsPage.class, e)));
			  }
	        } else {
	            setResponsePage(new ErrorReportPage(new UserReportableException(
	            		"Must select a file to upload", ToolsPage.class)));
	        }
		  }
	    }
	  }

  private static Folder uploadFolder;
  private static Folder getUploadFolder() {
    if (uploadFolder == null) {
      uploadFolder = new Folder(System.getProperty("java.io.tmpdir"),
          "wicket-uploads");
      uploadFolder.mkdirs();
    }
    return uploadFolder;
  }


}
