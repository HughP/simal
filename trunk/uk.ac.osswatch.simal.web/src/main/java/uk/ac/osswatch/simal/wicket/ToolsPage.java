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
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.model.ModelSupport;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.tools.Ohloh;
import uk.ac.osswatch.simal.tools.PTSWImport;
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
      add(new Label("numOfPeople", Integer.toString(UserApplication
          .getRepository().getAllPeople().size())));
      add(new Label("numOfCategories", Integer.toString(UserApplication
          .getRepository().getAllCategories().size())));
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
    ModelSupport.addTestData(repo);
  }

  private void importPTSW() throws UserReportableException {
    ISimalRepository repo;
    int preProjectCount;
    try {
      repo = UserApplication.getRepository();
      preProjectCount = repo.getAllProjects().size();
    } catch (SimalRepositoryException e) {
      throw new UserReportableException("Unable to get the count of projects",
          ToolsPage.class, e);
    }
    PTSWImport importer = new PTSWImport();
    Document pings;
    try {
      pings = importer.getLatestPingsAsRDF();
    } catch (SimalException e) {
      throw new UserReportableException(
          "Unable to retrieve the latest pings from PTSW", ToolsPage.class, e);
    }
    OutputFormat format = new OutputFormat(pings);
    StringWriter writer = new StringWriter();
    XMLSerializer serial = new XMLSerializer(writer, format);
    try {
      serial.serialize(pings);
    } catch (IOException e) {
      throw new UserReportableException("Unable to serialize PTSW response",
          ToolsPage.class);
    }
    logger.info("Updated DOAP documents:\n");
    logger.info(writer.toString());

    File tmpFile = new File(System.getProperty("java.io.tmpdir")
        + File.separator + "PTSWExport.xml");
    FileWriter fw = null;
    try {
      fw = new FileWriter(tmpFile);
      fw.write(writer.toString());
    } catch (IOException e) {
      throw new UserReportableException(
          "Unable to write PTSW export file to temporary space",
          ToolsPage.class);
    } finally {
      if (fw != null) {
        try {
          fw.close();
        } catch (IOException e) {
          logger.warn("Unable to close file", e);
        }
      }
    }

    try {
      repo.addProject(tmpFile.toURI().toURL(), tmpFile.toURI().toURL()
          .toExternalForm());
      int postProjectCount = repo.getAllProjects().size();
      logger.info("Imported " + (postProjectCount - preProjectCount)
          + " project records from PTSW");
    } catch (Exception e) {
      throw new UserReportableException(
          "Unable to add projects from PTSW Export", ToolsPage.class, e);
    }
  }

  private static class ImportFromOhlohForm extends Form<OhlohFormInputModel> {
    private static final long serialVersionUID = 4350446873545711199L;

    public ImportFromOhlohForm(String name) {
      super(name, new CompoundPropertyModel<OhlohFormInputModel>(inputModel));
      add(new TextField<String>("projectID"));
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
}
