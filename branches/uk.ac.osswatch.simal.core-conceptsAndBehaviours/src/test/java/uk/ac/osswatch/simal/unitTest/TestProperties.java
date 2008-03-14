package uk.ac.osswatch.simal.unitTest;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.osswatch.simal.rdf.SimalProperties;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestProperties {

  private static SimalProperties props;

  @BeforeClass
  public static void setUpBeforeClass() throws SimalRepositoryException {
    props = new SimalProperties();
  }

  @Test
  public void testDefaults() {
    assertEquals("false", props.getProperty(SimalProperties.PROPERTY_TEST));
    assertEquals("simalRepository", props
        .getProperty(SimalProperties.PROPERTY_DATA_DIR));
  }

}
