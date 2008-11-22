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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.osswatch.simal.integrationTest.rdf.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IFeed;

public class TestFeed extends BaseRepositoryTest{
  
  static Set<IFeed> feeds;
  static IFeed firstFeed;
  
  @BeforeClass
  public static void getFeeds() {
    feeds = project1.getFeeds();
    assertNotNull("Got no feeds in the project", feeds);
    assertTrue("Got no feeds in the project", 0 != feeds.size());
    
    firstFeed = feeds.iterator().next();
  }

  @Test
  public void getTitle() {
    String title = firstFeed.getTitle();
    assertEquals("Title of the first feed is incorrect", "Test Feed", title);
  }
  
  @Test
  public void getFeedURL() {
    String feedURL = firstFeed.getFeedURL();
    assertNotNull("Feed URL is undefined", feedURL);
    assertEquals("The feed URL is incorrect", "http://foo.org/feed.rss", feedURL);
  }
  
}
