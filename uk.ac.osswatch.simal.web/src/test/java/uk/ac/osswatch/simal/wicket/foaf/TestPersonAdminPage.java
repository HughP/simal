package uk.ac.osswatch.simal.wicket.foaf;

import org.junit.Test;

import uk.ac.osswatch.simal.wicket.TestBase;

public class TestPersonAdminPage extends TestBase{

  @Test
  public void testPageRender() {
    tester.startPage(PersonAdminPage.class);
    tester.assertRenderedPage(PersonAdminPage.class);
    tester.assertVisible("personList");
  }
}