package uk.ac.osswatch.simal.rest;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import uk.ac.osswatch.simal.rdf.SimalRepository;

/**
 * Tests API functions for working with complete DOAP files.
 * 
 */
public class TestProjectAPI extends AbstractAPITest{

  @Test
  public void addDOAP() throws SimalAPIException, URISyntaxException, IOException {
    RESTCommand command = RESTCommand.createCommand(RESTCommand.COMMAND_PROJECT_ADD);
    command.addParameter("rdf", "illegal RDF data");
    IAPIHandler handler = SimalHandlerFactory.createHandler(command, repo);
    try {
      handler.execute();
    } catch (SimalAPIException e) {
      // that's good, we don't expect to add invalid data
    }
    
    File testFile = new File(SimalRepository.class.getResource(SimalRepository.TEST_FILE_URI_WITH_QNAME).toURI());
    FileInputStream fis = new FileInputStream(testFile);
    int x= fis.available();
    byte b[]= new byte[x];
    fis.read(b);
    String data = new String(b);
    
    command.addParameter(RESTCommand.PARAM_RDF, data);
    handler = SimalHandlerFactory.createHandler(command, repo);
    try {
      handler.execute();
    } catch (SimalAPIException e) {
      fail("Excepotion thrown when adding project" + e.getMessage());
    }
    
    // we don't bother testing to see if the project has been added here
    // that's the job of the repository tests.
  }
}
