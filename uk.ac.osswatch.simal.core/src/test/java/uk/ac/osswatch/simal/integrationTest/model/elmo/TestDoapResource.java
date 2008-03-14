package uk.ac.osswatch.simal.integrationTest.model.elmo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.elmo.DoapResourceBehaviour;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestDoapResource extends BaseRepositoryTest {

  public TestDoapResource() throws SimalRepositoryException {
    super();
  }

  @Test
  public void testGetName() throws SimalRepositoryException {
    resetTestData();
    // FIXME: We shouldn't need to create the behaviour ourselves we
    // should be able to just designate it
    DoapResourceBehaviour resource = new DoapResourceBehaviour(project1);
    assertEquals(TEST_SIMAL_PROJECT_NAME, resource.getName());
  }

  @Test
  public void testGetShortDesc() {
    assertEquals(TEST_SIMAL_PROJECT_SHORT_DESC, project1.getDoapShortdesc());
  }

  @Test
  public void testSetShortDesc() {
    String newDesc = "New short description";
    project1.setDoapShortdesc(newDesc);
    assertEquals(newDesc, project1.getDoapShortdesc());
  }

  @Test
  public void testGetCreated() {
    assertEquals(TEST_SIMAL_PROJECT_CREATED, project1.getDoapCreated().toString());
  }

  @Test
  public void testSetCreated() throws SimalRepositoryException {
    String newCreated = "2020-20-01";
    Set<Object> set = new HashSet<Object>();
    set.add(newCreated);
    project1.setDoapCreated(set);
    assertEquals(newCreated, project1.getDoapCreated().toString());
  }

  @Test
  public void testGetDescription() {
    assertEquals(TEST_SIMAL_PROJECT_DESCRIPTION, project1.getDoapDescription());
  }

  @Test
  public void testSetDescription() {
    String newDesc = "modified description";
    project1.setDoapDescription(newDesc);
    assertEquals(newDesc, project1.getDoapDescription());
  }

  @Test
  public void testGetLicences() {
    assertTrue(project1.getDoapLicenses().toString().contains(
        TEST_SIMAL_PROJECT_LICENCE_ONE));
    assertTrue(project1.getDoapLicenses().toString().contains(
        TEST_SIMAL_PROJECT_LICENCE_TWO));
  }

}
