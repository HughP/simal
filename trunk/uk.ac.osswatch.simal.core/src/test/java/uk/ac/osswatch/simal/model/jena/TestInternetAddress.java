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

package uk.ac.osswatch.simal.model.jena;

import static junit.framework.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IInternetAddress;
import uk.ac.osswatch.simal.model.IPerson;
import uk.ac.osswatch.simal.model.ModelSupport;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * Test the class uk.ac.osswatch.simal.model.jena.InternetAddress, especially
 * the method getObfuscatedAddress()
 */
public class TestInternetAddress {

  protected static ISimalRepository repository;

  private static final String TEST_OSS_WATCH_DOAP = "testData/ossWatchDOAP.xml";

  private static final String TEST_DEVELOPER_SEE_ALSO = "http://people.apache.org/~rgardler/foaf.rdf.xml";

  private static final Map<String, String> EXPECTED_OBFUSCATED_EMAILS = new HashMap<String, String>();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    repository = SimalRepositoryFactory
        .getInstance(SimalRepositoryFactory.JENA);
    if (!repository.isInitialised()) {
      repository.initialise(null);
    }
    
    repository.addProject(ISimalRepository.class.getClassLoader().getResource(
        TEST_OSS_WATCH_DOAP), ModelSupport.TEST_FILE_BASE_URL);
    EXPECTED_OBFUSCATED_EMAILS.put("mailto:rgardler@apache.org",
        "mailto:rgardler [at] apache...org");
    EXPECTED_OBFUSCATED_EMAILS.put("mailto:ross.gardler@oucs.ox.ac.uk",
        "mailto:ross.gardler [at] oucs...ox...ac...uk");
  }

  @Test
  public void testInternetAddress() {
    try {
      IPerson developer = SimalRepositoryFactory.getPersonService()
          .findBySeeAlso(TEST_DEVELOPER_SEE_ALSO);
      if (developer != null) {
        Set<IInternetAddress> allEmailAddresses = developer.getEmail();
        for (IInternetAddress testAddress : allEmailAddresses) {
          assertEquals("Obfuscated addresses don't match.",
              EXPECTED_OBFUSCATED_EMAILS.get(testAddress.getAddress()),
              testAddress.getObfuscatedAddress());
        }
      } else {
        fail("Could not find right developer using seeAlso "
            + TEST_DEVELOPER_SEE_ALSO);
      }
    } catch (SimalRepositoryException e) {
      fail("Could not retreive test developer: " + e.getMessage());
    }
  }
}
