package uk.ac.osswatch.simal.wicket.doap;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Locale;

import javax.xml.namespace.QName;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.DuplicateQNameException;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
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
	public static DoapFormInputModel inputModel = new DoapFormInputModel();

	public DoapFormPage() {
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		add(new AddByURLForm("addByURLForm"));
		add(new DoapForm("doapForm"));
	}

	private class DoapForm extends Form {
		private static final long serialVersionUID = 4350446873545711199L;

		@SuppressWarnings("serial")
		public DoapForm(String name) {
			super(name, new CompoundPropertyModel(inputModel));

			RequiredTextField stringTextField = new RequiredTextField("name");
			stringTextField.setLabel(new Model());
			add(stringTextField);

			stringTextField = new RequiredTextField("shortDesc");
			stringTextField.setLabel(new Model());
			add(stringTextField);

		}

		@Override
		protected void onSubmit() {
			super.onSubmit();

			if (!this.hasError()) {
				QName qname = new QName(SimalRepository.DEFAULT_PROJECT_NAMESPACE_URI + inputModel.getName());
				try {
					SimalRepository repo = UserApplication.getRepository();
					
					IProject project = repo.createProject(qname);
					project.addName(inputModel.getName());
					project.setShortDesc(inputModel.getShortDesc());
					setResponsePage(new UserHomePage());
				} catch (DuplicateQNameException e) {
					error("Name must be unique within the registry");
				} catch (SimalRepositoryException e) {
					setResponsePage(new ErrorReportPage(
							new UserReportableException(
									"Unable to add doap from form",
									DoapFormPage.class, e)));
				}
			}
		}
	}

	private class AddByURLForm extends Form {
		private static final long serialVersionUID = 4350446873545711199L;

		@SuppressWarnings("serial")
		public AddByURLForm(String name) {
			super(name, new CompoundPropertyModel(inputModel));

			add(new TextField("sourceURL", URL.class) {
				@SuppressWarnings("unchecked")
				public IConverter getConverter(final Class type) {
					return new IConverter() {
						public Object convertToObject(String value,
								Locale locale) {
							try {
								return new URL(value.toString());
							} catch (MalformedURLException e) {
								throw new ConversionException("'" + value
										+ "' is not a valid URL");
							}
						}

						public String convertToString(Object value,
								Locale locale) {
							return value != null ? value.toString() : null;
						}
					};
				}
			});
		}

		@Override
		protected void onSubmit() {
			super.onSubmit();

			if (!this.hasError()) {
				try {
					UserApplication.getRepository().addProject(
							inputModel.getSourceURL(),
							inputModel.getSourceURL().getHost());
					setResponsePage(new UserHomePage());
				} catch (SimalRepositoryException e) {
					setResponsePage(new ErrorReportPage(
							new UserReportableException(
									"Unable to add doap from url",
									DoapFormPage.class, e)));
				}
			}
		}
	}
}
