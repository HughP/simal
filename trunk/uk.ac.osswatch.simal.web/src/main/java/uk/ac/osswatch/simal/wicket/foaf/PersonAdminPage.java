package uk.ac.osswatch.simal.wicket.foaf;

import org.apache.wicket.markup.html.WebPage;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.wicket.BasePage;
import uk.ac.osswatch.simal.wicket.ErrorReportPage;
import uk.ac.osswatch.simal.wicket.UserReportableException;
import uk.ac.osswatch.simal.wicket.panel.PersonListPanel;

/**
 * This page provides facilities to manage people stored 
 * in the simal repository.
 */
public class PersonAdminPage extends BasePage {

  public PersonAdminPage() {
    try {
      add(new PersonListPanel("personList"));
    } catch (SimalRepositoryException e) {
      UserReportableException error = new UserReportableException(
          "Unable to get people from the repository",
          PersonAdminPage.class, e);
      setResponsePage(new ErrorReportPage(error));
    }
  }
}

