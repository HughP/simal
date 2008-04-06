package uk.ac.osswatch.simal.unitTest;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.osswatch.simal.SimalProperties;
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
        .getProperty(SimalProperties.PROPERTY_RDF_DATA_DIR));
  }

  @Test
  public void testLocal() throws FileNotFoundException, IOException, SimalRepositoryException {
    // first make sure a local.simal.properties file exists by changing a
    // value and saving the properties file
    props.setProperty(SimalProperties.PROPERTY_UNIT_TEST, "local");
    props.save();
    // Now re-initialise the properties and test
    props = new SimalProperties();
    assertEquals("This test will fail locally unless you have set up a local.simal.properties file", "local", props.getProperty(SimalProperties.PROPERTY_UNIT_TEST));
  }

}
