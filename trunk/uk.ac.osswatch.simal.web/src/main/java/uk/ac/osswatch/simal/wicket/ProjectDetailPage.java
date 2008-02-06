package uk.ac.osswatch.simal.wicket;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.IPageLink;

import uk.ac.osswatch.simal.model.elmo.Project;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class ProjectDetailPage extends BasePage {
	private static final long serialVersionUID = 8719708525508677833L;

	public ProjectDetailPage() {
		Project project;
		try {
			project = SimalRepository.getProject(UserApplication.DEFAULT_PROJECT_QNAME);
			populatePage(project);
		} catch (SimalRepositoryException e) {
			UserReportableException error = new UserReportableException("Unable to get project from the repository", ExhibitProjectBrowserPage.class, e);
			setResponsePage(new ErrorReportPage(error));
		}
	}

	public ProjectDetailPage(Project project) {
		populatePage(project);
	}

	private void populatePage(Project project) {
		add(new Label("projectName", project.getName()));
		add(new Label("shortDesc", project.getShortDesc()));
	}

	/**
	 * Get a link to a ProjectDetailPage for a project.
	 * 
	 * @param project
	 *            the project we want a detail page link for
	 * @return
	 */
	@SuppressWarnings("serial")
	public static IPageLink getLink(final Project project) {
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
}
