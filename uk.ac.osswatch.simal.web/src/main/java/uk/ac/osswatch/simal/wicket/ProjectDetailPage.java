package uk.ac.osswatch.simal.wicket;


import org.apache.wicket.Component;
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
			add(new Label("releases", project.getReleases().toString()));
		} catch (SimalRepositoryException e) {
			add(new Label("releases", "Error retrieving release details"));
		}
		add(getRepeatingLinks("homepages", "homepage", project.getHomepages()));

		// Community tools
		add(getRepeatingLinks("issueTrackers", "issueTracker", "Issue Tracker", project.getIssueTrackers()));
		add(getRepeatingLinks("mailingLists", "mailingList", project
				.getMailingLists()));
		add(getRepeatingLinks("wikis", "wiki", "Wiki", project.getWikis()));
		try {
			add(new Label("repositories", project.getRepositories().toString()));
		} catch (SimalRepositoryException e) {
			add(new Label("repositories", "Error retrieving repository details"));
		}
		add(getRepeatingLinks("screenshots", "screenshot", "Screenshot", project.getScreenshots()));

		// facets
		add(getRepeatingLinks("categories", "category", project.getCategories()));
		add(getRepeatingLabels("OSes", "OS", project.getOSes()));
		add(getRepeatingLabels("programmingLanguages", "programmingLanguage", project.getProgrammingLangauges()));

		// contributors
		try {
			add(new Label("maintainers", project.getMaintainers().toString()));
		} catch (SimalRepositoryException e) {
			add(new Label("maintainers", "Error retrieving maintainer details"));
		}
		try {
			add(new Label("developers", project.getDevelopers().toString()));
		} catch (SimalRepositoryException e) {
			add(new Label("developers", "Error retrieving developer details"));
		}
		try {
			add(new Label("testers", project.getTesters().toString()));
		} catch (SimalRepositoryException e) {
			add(new Label("testers", "Error retrieving tester details"));
		}
		try {
			add(new Label("helpers", project.getHelpers().toString()));
		} catch (SimalRepositoryException e) {
			add(new Label("helpers", "Error retrieving helper details"));
		}
		try {
			add(new Label("documenters", project.getDocumenters().toString()));
		} catch (SimalRepositoryException e) {
			add(new Label("documenters", "Error retrieving documenter details"));
		}
		try {
			add(new Label("translators", project.getTranslators().toString()));
		} catch (SimalRepositoryException e) {
			add(new Label("translators", "Error retrieving translator details"));
		}

		// downlaod
		add(getRepeatingLinks("downloadPages", "downloadPage", "Downloads", project.getDownloadPages()));
		add(getRepeatingLinks("downloadMirrors", "downloadMirror", "Download Mirror", project.getDownloadMirrors()));

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
