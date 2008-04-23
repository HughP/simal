package uk.ac.osswatch.simal.wicket.doap;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.Link;

import uk.ac.osswatch.simal.model.IProject;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.ExhibitProjectBrowserPage;
import uk.ac.osswatch.simal.wicket.UserApplication;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.data.SortableDoapResourceDataProvider;
import uk.ac.osswatch.simal.wicket.panel.ReleasesPanel;
import uk.ac.osswatch.simal.wicket.panel.SourceRepositoriesPanel;

public class ProjectDetailPage extends BasePage {
  private static final long serialVersionUID = 8719708525508677833L;

  public ProjectDetailPage() {
    IProject project;
    try {
      project = UserApplication.getRepository().getProject(
          UserApplication.DEFAULT_PROJECT_QNAME);
      populatePage(project);
    } catch (SimalRepositoryException e) {
      UserReportableException error = new UserReportableException(
          "Unable to get project from the repository",
          ProjectDetailPage.class, e);
      setResponsePage(new ErrorReportPage(error));
    }
  }

  public ProjectDetailPage(IProject project) {
    populatePage(project);
  }

  private void populatePage(final IProject project) {
    final Link deleteProjectActionLink = new Link("deleteProjectActionLink") {
        public void onClick() {
            try {
              project.delete();
            } catch (SimalRepositoryException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
        }
    };
    add(deleteProjectActionLink);
    
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
    add(getRepeatingLinks("homepages", "homepage",
        new SortableDoapResourceDataProvider(project.getHomepages()), false));

    // Community tools
    add(getRepeatingLinks("issueTrackers", "issueTracker", "Issue Tracker",
        new SortableDoapResourceDataProvider(project.getIssueTrackers()), false));
    add(getRepeatingLinks("mailingLists", "mailingList",
        new SortableDoapResourceDataProvider(project.getMailingLists()), false));
    add(getRepeatingLinks("wikis", "wiki", "Wiki",
        new SortableDoapResourceDataProvider(project.getWikis()), false));
    add(new SourceRepositoriesPanel("sourceRepositories", project
        .getRepositories()));
    add(getRepeatingLinks("screenshots", "screenshot", "Screenshot",
        new SortableDoapResourceDataProvider(project.getScreenshots()), false));

    // facets
    add(getRepeatingLinks("categories", "category",
        new SortableDoapResourceDataProvider(project.getCategories()), true));
    add(getRepeatingLabels("OSes", "OS", project.getOSes()));
    add(getRepeatingLabels("programmingLanguages", "programmingLanguage",
        project.getProgrammingLanguages()));

    // contributors
    add(getRepeatingPersonPanel("maintainers", "maintainer", project
        .getMaintainers()));
    add(getRepeatingPersonPanel("developers", "developer", project
        .getDevelopers()));
    add(getRepeatingPersonPanel("testers", "tester", project.getTesters()));
    add(getRepeatingPersonPanel("helpers", "helper", project.getHelpers()));
    add(getRepeatingPersonPanel("documenters", "documenter", project
        .getDocumenters()));
    add(getRepeatingPersonPanel("translators", "translator", project
        .getTranslators()));

    // downlaod
    add(getRepeatingLinks("downloadPages", "downloadPage", "Downloads",
        new SortableDoapResourceDataProvider(project.getDownloadPages()), false));
    add(getRepeatingLinks("downloadMirrors", "downloadMirror",
        "Download Mirror", new SortableDoapResourceDataProvider(project
            .getDownloadMirrors()), false));

    add(new Label("created", project.getCreated()));
  }

  /**
   * Get a link to a ProjectDetailPage for a project.
   * 
   * @param project
   *          the project we want a detail page link for
   * @return
   */
  @SuppressWarnings("serial")
  public static IPageLink getLink(final IProject project) {
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
