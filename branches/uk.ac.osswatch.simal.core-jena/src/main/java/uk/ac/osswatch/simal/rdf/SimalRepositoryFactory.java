package uk.ac.osswatch.simal.rdf;

import uk.ac.osswatch.simal.rdf.sesame.SimalRepository;

public class SimalRepositoryFactory {

  private static final int TYPE_SESAME = 1;
  private static int TYPE_DEFAULT = TYPE_SESAME;

  /**
   * Get the SimalRepository object using the default repository type.
   * Note that only one of these can exist in a
   * single virtual machine.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public static ISimalRepository getInstance() throws SimalRepositoryException {
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
    if (type == TYPE_SESAME) {
    return SimalRepository.getInstance();
    } else {
      throw new SimalRepositoryException("Unable to create repository instance of type " + type);
    }
  }

}
