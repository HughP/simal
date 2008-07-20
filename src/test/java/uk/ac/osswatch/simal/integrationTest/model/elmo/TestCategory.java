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
package uk.ac.osswatch.simal.integrationTest.model.elmo;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestCategory extends BaseRepositoryTest {
  @Test
  public void testGetCategoryLabel() throws SimalRepositoryException, URISyntaxException {
    URI uri = new URI("http://simal.oss-watch.ac.uk/category/socialNews");
    IDoapCategory category = repository.findCategory(uri);
    String label = category.getLabel();
    assertEquals("Category Label is incorrect", "Social News", label);

    uri = new URI("http://example.org/does/not/exist");
    category = repository.findCategory(uri);
    label = category.getLabel();
    assertNotSame("Somehow we have a valid label for a resource that does not exist",
        uri, category.getURI());
  }
}
