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

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * Testing with a bogus homepagelabels configuration to force the loading of the
 * home page labels to fail. Reflection needed to force re-initialisation of the
 * properties.
 * 
 * @author svanderwaal
 * 
 */
public class HomepageLabelGeneratorFailsTest {

  @Before 
  @After
  public void initClassesUnderTest() throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException
  {
    Class<HomepageLabelGenerator> targetClass = HomepageLabelGenerator.class;
    Field field = targetClass.getDeclaredField("homepageLabels");
    field.setAccessible(true);
    field.set(null, null);    

    Class<SimalProperties> targetSimalProperties = SimalProperties.class;
    Field defaultProps = targetSimalProperties.getDeclaredField("defaultProps");
    defaultProps.setAccessible(true);
    defaultProps.set(null, null);    
  }
  
  @Test
  public void testWrongPropertiesFile() throws SimalRepositoryException {
    SimalProperties.initProperties();
    SimalProperties.setProperty(SimalProperties.PROPERTY_SIMAL_HOMEPAGELABELS_PATH, "invalid.file");
    String defaultLabel = HomepageLabelGenerator.DEFAULT_HOMEPAGE_LABEL;

    HomepageLabelGeneratorTest.matchingTest("http://www.jisc.ac.uk/whatwedo", defaultLabel);
    HomepageLabelGeneratorTest.matchingTest("https://www.jisc.ac.uk/whatwedo", defaultLabel);
    HomepageLabelGeneratorTest.matchingTest("www.jisc.ac.uk/whatwedo/andmuchmore", defaultLabel);
    HomepageLabelGeneratorTest.matchingTest("http://www.jisc.ac.uk/whatwedo/andmuchmore", defaultLabel);

    HomepageLabelGeneratorTest.notMatchingTest("www.jisc.ac.uk/whatwedo", "JISC Project Page");
    HomepageLabelGeneratorTest.notMatchingTest("code.google.com", "Google code site");
    HomepageLabelGeneratorTest.notMatchingTest("www.sf.net", "Sourceforge site");
    HomepageLabelGeneratorTest.notMatchingTest("www.sourceforge.net", "Sourceforge site");
    HomepageLabelGeneratorTest.notMatchingTest("sourceforge.net", "Sourceforge site");
    HomepageLabelGeneratorTest.notMatchingTest("www.ohloh.net", "Ohloh stats");

  }

}
