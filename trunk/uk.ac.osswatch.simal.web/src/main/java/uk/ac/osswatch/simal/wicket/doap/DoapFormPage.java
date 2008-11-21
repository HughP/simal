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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.lang.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserApplication;
import uk.ac.osswatch.simal.wicket.UserHomePage;
import uk.ac.osswatch.simal.wicket.UserReportableException;

/**
 * A form for manipulating and creating DOAP files.
 */
public class DoapFormPage extends BasePage {
  private static final long serialVersionUID = -7082891387390604176L;
  private static final Logger logger = LoggerFactory
      .getLogger(DoapFormPage.class);
  private static DoapFormInputModel inputModel = new DoapFormInputModel();
  private Folder uploadFolder;

  public DoapFormPage() {
    final FeedbackPanel feedback = new FeedbackPanel("feedback");
    add(feedback);

    add(new AddByURLForm("addByURLForm"));

    final FileUploadForm ajaxSimpleUploadForm = new FileUploadForm("uploadForm");
    ajaxSimpleUploadForm.add(new UploadProgressBar("uploadProgress",
        ajaxSimpleUploadForm));
    add(ajaxSimpleUploadForm);

    add(new AddByRawRDFForm("rawRDFForm"));

    add(new DoapForm("doapForm"));
  }

  private static class AddByRawRDFForm extends Form<DoapFormInputModel> {
    private static final long serialVersionUID = 5436861979864365527L;
    private TextArea<String> rdfField;

    public AddByRawRDFForm(String id) {
      super(id, new CompoundPropertyModel<DoapFormInputModel>(inputModel));
      add(rdfField = new TextArea<String>("rawRDF"));
      String[] defaultValue = { "" };
      rdfField.setModelValue(defaultValue);
    }

    @Override
    protected void onSubmit() {
      super.onSubmit();
      String rdf = StringEscapeUtils.unescapeXml(rdfField.getValue());
      try {
        ISimalRepository repo = UserApplication.getRepository();
        repo.add(rdf);
        setResponsePage(new UserHomePage());
      } catch (SimalRepositoryException e) {
        setResponsePage(new ErrorReportPage(new UserReportableException(
            "Unable to add doap using RDF supplied", DoapFormPage.class, e)));
      }

    }

  }

  private class FileUploadForm extends Form<FileUploadField> {
    private static final long serialVersionUID = -6275625011225339551L;
    private FileUploadField fileUploadField;

    /**
     * Simple constructor.
     * 
     * @param name
     *          Component name
     */
    public FileUploadForm(String name) {
      super(name);
      setMultiPart(true);
      add(fileUploadField = new FileUploadField("fileInput"));
      setMaxSize(Bytes.kilobytes(100));
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
            DoapFormPage.this.info("saved file: " + upload.getClientFileName());
          } catch (IOException e) {
            throw new IllegalStateException("Unable to write file");
          }

          try {
            UserApplication.getRepository().addProject(newFile.toURI().toURL(),
                null);
            setResponsePage(new UserHomePage());
          } catch (SimalRepositoryException e) {
            setResponsePage(new ErrorReportPage(new UserReportableException(
                "Unable to add doap from url", DoapFormPage.class, e)));
          } catch (MalformedURLException e) {
            // should never be thrown as file is created locally
            setResponsePage(new ErrorReportPage(new UserReportableException(
                "Unable to add uploaded file", DoapFormPage.class, e)));
          }
        }
      }
    }
  }

  private static class DoapForm extends Form<DoapFormInputModel> {
    private static final long serialVersionUID = 4350446873545711199L;

    @SuppressWarnings("serial")
    public DoapForm(String name) {
      super(name, new CompoundPropertyModel<DoapFormInputModel>(inputModel));

      RequiredTextField<String> stringTextField = new RequiredTextField<String>(
          "name");
      stringTextField.setLabel(new Model<String>());
      add(stringTextField);

      stringTextField = new RequiredTextField<String>("shortDesc");
      stringTextField.setLabel(new Model<String>());
      add(stringTextField);

      add(new TextArea<String>("description"));
    }

    @Override
    protected void onSubmit() {
      super.onSubmit();

      if (!this.hasError()) {
        String uri = RDFUtils.PROJECT_NAMESPACE_URI + inputModel.getName();
        try {
          ISimalRepository repo = UserApplication.getRepository();

          IProject project = repo.createProject(uri);
          project.addName(inputModel.getName());
          project.setShortDesc(inputModel.getShortDesc());
          project.setDescription(inputModel.getDescription());
          setResponsePage(new UserHomePage());
        } catch (DuplicateURIException e) {
          error("Name must be unique within the registry");
        } catch (SimalRepositoryException e) {
          setResponsePage(new ErrorReportPage(new UserReportableException(
              "Unable to add doap from form", DoapFormPage.class, e)));
        }
      }
    }
  }

  private static class AddByURLForm extends Form<DoapFormInputModel> {
    private static final long serialVersionUID = 4350446873545711199L;

    public AddByURLForm(String name) {
      super(name, new CompoundPropertyModel<DoapFormInputModel>(inputModel));
      add(new TextField<URL>("sourceURL", URL.class));
    }

    @Override
    protected void onSubmit() {
      super.onSubmit();

      if (!this.hasError()) {
        try {
          UserApplication.getRepository().addProject(inputModel.getSourceURL(),
              inputModel.getSourceURL().getHost());
          setResponsePage(new UserHomePage());
        } catch (SimalRepositoryException e) {
          setResponsePage(new ErrorReportPage(new UserReportableException(
              "Unable to add doap from url", DoapFormPage.class, e)));
        }
      }
    }
  }

  private Folder getUploadFolder() {
    if (uploadFolder == null) {
      uploadFolder = new Folder(System.getProperty("java.io.tmpdir"),
          "wicket-uploads");
      uploadFolder.mkdirs();
    }
    return uploadFolder;
  }
}
