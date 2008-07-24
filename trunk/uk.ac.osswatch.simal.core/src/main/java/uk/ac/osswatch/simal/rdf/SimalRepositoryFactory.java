package uk.ac.osswatch.simal.rdf;
/*
 * 
Copyright 2007 University of Oxford * 
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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimalRepositoryFactory {
  private static final Logger logger = LoggerFactory
  .getLogger(SimalRepositoryFactory.class);
  
  public static final int TYPE_SESAME = 1;
  public static final int TYPE_JENA = 2;
  public static int TYPE_DEFAULT = TYPE_JENA;

  /**
   * Get the SimalRepository object using the default repository type.
   * Note that only one of these can exist in a
   * single virtual machine.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public static ISimalRepository getInstance() throws SimalRepositoryException {
    logger.debug("Creating repository of default type");
    return getInstance(TYPE_DEFAULT );
  }
  
  /**
   * Get the SimalRepository object. Note that only one of each type can exist in a
   * single virtual machine.
   * 
   * @param type - type of repository see TYPE_* constants
   * @return
   * @throws SimalRepositoryException
   */
  public static ISimalRepository getInstance(int type) throws SimalRepositoryException {
    if (type == TYPE_JENA) {
      logger.debug("Creating Jena repository");
      return uk.ac.osswatch.simal.rdf.jena.SimalRepository.getInstance();
    } else {
      throw new SimalRepositoryException("Unable to create repository instance of type " + type);
    }
  }

}
