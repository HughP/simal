package uk.ac.osswatch.simal.rdf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimalRepositoryFactory {
  private static final Logger logger = LoggerFactory
  .getLogger(SimalRepositoryFactory.class);
  
  public static final int TYPE_SESAME = 1;
  public static final int TYPE_JENA = 2;
  public static int TYPE_DEFAULT = TYPE_SESAME;

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
