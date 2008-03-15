package uk.ac.osswatch.simal.rest;

import org.junit.BeforeClass;

import uk.ac.osswatch.simal.rdf.SimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public abstract class AbstractAPITest {

  protected static SimalRepository repo;

  @BeforeClass
  public static void setUpRepo() throws SimalRepositoryException {
    repo = new SimalRepository();
    repo.setIsTest(true);
    repo.initialise();
  }
}
