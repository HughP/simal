package uk.ac.osswatch.simal.wicket;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.IPageLink;

import uk.ac.osswatch.simal.model.elmo.Project;
import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.panel.ReleasesPanel;
import uk.ac.osswatch.simal.wicket.panel.SourceRepositoriesPanel;

public class ProjectDetailPage extends BasePage {
	private static final long serialVersionUID = 8719708525508677833L;

	public ProjectDetailPage() {
		Project project;
		try {
			project = SimalRepository
					.getProject(UserApplication.DEFAULT_PROJECT_QNAME);
			populatePage(project);
		} catch (SimalRepositoryException e) {
			UserReportableException error = new UserReportableException(
					"Unable to get project from the repository",
					ExhibitProjectBrowserPage.class, e);
			setResponsePage(new ErrorReportPage(error));
		}
	}

	public ProjectDetailPage(Project project) {
		populatePage(project);
	}

	private void populatePage(Project project) {
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
		add(getRepeatingLinks("homepages", "homepage", project.getHomepages()));

		// Community tools
		add(getRepeatingLinks("issueTrackers", "issueTracker", "Issue Tracker",
				project.getIssueTrackers()));
		add(getRepeatingLinks("mailingLists", "mailingList", project
				.getMailingLists()));
		add(getRepeatingLinks("wikis", "wiki", "Wiki", project.getWikis()));
		try {
			add(new SourceRepositoriesPanel("sourceRepositories", project
					.getRepositories()));
		} catch (SimalRepositoryException e) {
			UserReportableException error = new UserReportableException(
					"Unable to get project source repositories from the repository",
					ExhibitProjectBrowserPage.class, e);
			setResponsePage(new ErrorReportPage(error));
		}
		add(getRepeatingLinks("screenshots", "screenshot", "Screenshot",
				project.getScreenshots()));

		// facets
		add(getRepeatingLinks("categories", "category", project.getCategories()));
		add(getRepeatingLabels("OSes", "OS", project.getOSes()));
		add(getRepeatingLabels("programmingLanguages", "programmingLanguage",
				project.getProgrammingLangauges()));

		// contributors
		try {
			add(getRepeatingPersonPanel("maintainers", "maintainer", project
					.getMaintainers()));
		} catch (SimalRepositoryException e) {
			add(new Label("maintainers", "Error retrieving maintainer details"));
		}
		try {
			add(getRepeatingPersonPanel("developers", "developer", project
					.getDevelopers()));
		} catch (SimalRepositoryException e) {
			add(new Label("developers", "Error retrieving developer details"));
		}
		try {
			add(getRepeatingPersonPanel("testers", "tester", project
					.getTesters()));
		} catch (SimalRepositoryException e) {
			add(new Label("testers", "Error retrieving tester details"));
		}
		try {
			add(getRepeatingPersonPanel("helpers", "helper", project
					.getHelpers()));
		} catch (SimalRepositoryException e) {
			add(new Label("helpers", "Error retrieving helper details"));
		}
		try {
			add(getRepeatingPersonPanel("documenters", "documenter", project
					.getDocumenters()));
		} catch (SimalRepositoryException e) {
			add(new Label("documenters", "Error retrieving documenter details"));
		}
		try {
			add(getRepeatingPersonPanel("translators", "translator", project
					.getTranslators()));
		} catch (SimalRepositoryException e) {
			add(new Label("translators", "Error retrieving translator details"));
		}

		// downlaod
		add(getRepeatingLinks("downloadPages", "downloadPage", "Downloads",
				project.getDownloadPages()));
		add(getRepeatingLinks("downloadMirrors", "downloadMirror",
				"Download Mirror", project.getDownloadMirrors()));

		add(new Label("created", project.getCreated()));
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
