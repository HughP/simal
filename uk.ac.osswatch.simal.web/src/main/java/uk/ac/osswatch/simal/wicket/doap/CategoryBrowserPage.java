package uk.ac.osswatch.simal.wicket.doap;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.panel.CategoryListPanel;

/**
 * This page provides facilities to browse stored 
 * in the simal repository.
 */
public class CategoryBrowserPage extends BasePage {
  private static final long serialVersionUID = -3418218005629173956L;

  public CategoryBrowserPage() {
    try {
      add(new CategoryListPanel("categoryList"));
    } catch (SimalRepositoryException e) {
      UserReportableException error = new UserReportableException(
          "Unable to get categories from the repository",
          CategoryBrowserPage.class, e);
      setResponsePage(new ErrorReportPage(error));
    }
  }
}

