/*
 * Copyright 2010 University of Oxford 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

package uk.ac.osswatch.simal.model.utils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

import uk.ac.osswatch.simal.model.MockDoapResource;

/**
 * Test whether the DoapResourceByNameComparator compares correctly.
 */
public class IDoapResourceByNameComparatorTest {

  private static final DoapResourceByNameComparator comp = new DoapResourceByNameComparator();

  @Test
  public void testIDoapResourcesByName() {
    
    MockDoapResource emptyName = new MockDoapResource();
    MockDoapResource t1 = new MockDoapResource();
    MockDoapResource t2 = new MockDoapResource();
    t1.setName("aaa");
    t2.setName("zzz");
    
    assertEquals(0, comp.compare(null, null));
    assertEquals(0, comp.compare(t1, t1));
    assertEquals(0, comp.compare(t2, t2));
    assertEquals(0, comp.compare(emptyName, emptyName));

    assertTrue(comp.compare(t1, t2) < 0);
    assertTrue(comp.compare(t2, t1) > 0);
    assertTrue(comp.compare(emptyName, t1) < 0);
    assertTrue(comp.compare(emptyName, t2) < 0);
    
    assertTrue(comp.compare(null, t2) < 0);
    assertTrue(comp.compare(t2, null) > 0);
    assertTrue(comp.compare(null, emptyName) < 0);
    assertTrue(comp.compare(emptyName, null) > 0);
  }
  
  
}
