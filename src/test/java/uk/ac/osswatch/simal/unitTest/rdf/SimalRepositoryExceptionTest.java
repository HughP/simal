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
package uk.ac.osswatch.simal.unitTest.rdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class SimalRepositoryExceptionTest {

  @Test
  public void testSimalRepositoryException() {
    SimalRepositoryException e = new SimalRepositoryException("Just testing",
        null);
    assertEquals("Just testing", e.getMessage());
    assertNull(e.getCause());

    e = new SimalRepositoryException("Just testing", new IOException(
        "Just testing an IO exception"));
    assertEquals("Just testing", e.getMessage());
    assertNotNull(e.getCause());
  }

}
