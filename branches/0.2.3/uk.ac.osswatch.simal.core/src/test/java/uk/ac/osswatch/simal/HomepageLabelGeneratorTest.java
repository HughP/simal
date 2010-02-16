package uk.ac.osswatch.simal;

/*
 * Copyright 2009 University of Oxford 
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

/**
 * @author svanderwaal
 * 
 */
public class HomepageLabelGeneratorTest {

  @Test
  public void testConfiguredHomepageLabels() {
    String jiscLabel = "JISC Project Page";
    matchingTest("http://www.jisc.ac.uk/whatwedo", jiscLabel);
    matchingTest("https://www.jisc.ac.uk/whatwedo", jiscLabel);
    matchingTest("www.jisc.ac.uk/whatwedo/andmuchmore", jiscLabel);
    matchingTest("http://www.jisc.ac.uk/whatwedo/andmuchmore", jiscLabel);

    notMatchingTest("wqerwqrwwrewww.jisc.ac.uk/whatwedo", jiscLabel);
    notMatchingTest("kkjisc.ac.uk/whatwedo", jiscLabel);
    notMatchingTest("www.jisc.ac.uk:8080/whatwedo/andmuchmore", jiscLabel);
  }

  @Test
  public void testConfiguredSFHomepageLabels() {
    String sfLabel = "Sourceforge site";
    matchingTest("http://www.sf.net/myproject", sfLabel);
    matchingTest("http://sourceforge.net/myproject", sfLabel);
    matchingTest("myproject.sourceforge.net", sfLabel);
    matchingTest("https://myproject.sourceforge.net", sfLabel);
  }

  @Test
  public void testAllKnownHomePageLabels() {
    matchingTest("www.jisc.ac.uk/whatwedo", "JISC Project Page");
    matchingTest("code.google.com", "Google code site");
    matchingTest("www.sf.net", "Sourceforge site");
    matchingTest("www.sourceforge.net", "Sourceforge site");
    matchingTest("sourceforge.net", "Sourceforge site");
    matchingTest("www.ohloh.net", "Ohloh stats");
  }

  @Test
  public void testDefaultHomepageLabels() {
    String defaultLabel = HomepageLabelGenerator.DEFAULT_HOMEPAGE_LABEL;
    matchingTest("This can never be a uri!", defaultLabel);
    matchingTest("https://looks.like.one", defaultLabel);
    matchingTest("www.jisc.ac.uk:8080/whatwedo/andmuchmore", defaultLabel);

    notMatchingTest("http://www.jisc.ac.uk/whatwedo", defaultLabel);
    notMatchingTest("code.google.com", defaultLabel);
  }

  public static void matchingTest(String uri, String expectedHomepageLabel) {
    String resultString = HomepageLabelGenerator.getHomepageLabel(uri);
    assertEquals(expectedHomepageLabel, resultString);
  }

  public static void notMatchingTest(String uri, String expectedHomepageLabel) {
    String resultString = HomepageLabelGenerator.getHomepageLabel(uri);
    assertNotSame(expectedHomepageLabel, resultString);
  }

}
