/*
 * Copyright 2007 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.osswatch.simal.integrationTest.model.jena;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestDoapResource extends BaseRepositoryTest {

  public TestDoapResource() throws SimalRepositoryException {
    super();
  }

  @Test
  public void testGetName() throws SimalRepositoryException {
    assertEquals(TEST_SIMAL_PROJECT_NAME, project1.getName());
  }

  @Test
  public void testGetShortDesc() {
    assertEquals(TEST_SIMAL_PROJECT_SHORT_DESC, project1.getShortDesc());
  }

  @Test
  public void testSetShortDesc() {
    String origDesc = project1.getShortDesc();
    String newDesc = "New short description";
    project1.setShortDesc(newDesc);
    assertEquals(newDesc, project1.getShortDesc());
    project1.setShortDesc(origDesc);
    assertEquals(origDesc, project1.getShortDesc());
  }

  @Test
  public void testGetCreated() {
    assertEquals(TEST_SIMAL_PROJECT_CREATED, project1.getCreated().toString());
  }

  @Test
  public void testSetCreated() throws SimalRepositoryException {
    String newCreated = "2020-20-01";
    project1.setCreated(newCreated);
    assertEquals(newCreated, project1.getCreated());
  }

  @Test
  public void testGetDescription() {
    assertEquals(TEST_SIMAL_PROJECT_DESCRIPTION, project1.getDescription());
  }

  @Test
  public void testSetDescription() {
    String newDesc = "modified description";
    project1.setDescription(newDesc);
    assertEquals(newDesc, project1.getDescription());
  }

  @Test
  public void testGetLicences() {
    String licences = project1.getLicences().toString();
    assertTrue(licences.contains(TEST_SIMAL_PROJECT_LICENCE_ONE));
    assertTrue(licences.contains(TEST_SIMAL_PROJECT_LICENCE_TWO));
  }

}
