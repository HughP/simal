package uk.ac.osswatch.simal.wicket;

/*
 * Copyright 2008 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");   *
 * you may not use this file except in compliance with the License.  *
 * You may obtain a copy of the License at                           *
 *                                                                   *
 *   http://www.apache.org/licenses/LICENSE-2.0                      *
 *                                                                   *
 * Unless required by applicable law or agreed to in writing,        *
 * software distributed under the License is distributed on an       *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY            *
 * KIND, either express or implied.  See the License for the         *
 * specific language governing permissions and limitations           *
 * under the License.                                                *
 */

import org.junit.Test;

/**
 * Simple test using the WicketTester
 */
public class TestUserHomePage extends TestBase {

  @Test
  public void testRenderPage() {
    tester.startPage(UserHomePage.class);
    tester.assertRenderedPage(UserHomePage.class);
    tester.assertVisible("projectList");
    tester.assertVisible("featuredProject");
    tester.assertVisible("footer");
  }
}