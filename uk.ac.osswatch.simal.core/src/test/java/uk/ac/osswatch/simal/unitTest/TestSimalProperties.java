package uk.ac.osswatch.simal.unitTest;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Test;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestSimalProperties {
  
  @Test
  public void testLocalFile() throws URISyntaxException, SimalRepositoryException{
    String version = "Simal-UnitTests";
    File propsFile =  new File(TestSimalProperties.class.getClassLoader().getResource(
        "testData/local.unitTest.properties").toURI());
    SimalProperties.setLocalPropertiesFile(propsFile);
    assertEquals("We don't seem to be using the custom properties file", version, SimalProperties.getProperty(SimalProperties.PROPERTY_SIMAL_VERSION));
  }

}
