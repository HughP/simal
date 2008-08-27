package uk.ac.osswatch.simal.unitTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import uk.ac.osswatch.simal.Simal;
import uk.ac.osswatch.simal.model.ModelSupport;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

/**
 * Test the CLI provided by the uk.ac.osswatcy.simal.Simal class.
 * 
 */
public class TestCLI {
  PrintStream origError;
  PrintStream origOut;
  ByteArrayOutputStream out;
  ByteArrayOutputStream err;

  /**
   * Capture the streams so we can listen
   * 
   */
  @Before
  public void setUp() {
    origError = System.err;
    origOut = System.out;

    out = new ByteArrayOutputStream();
    err = new ByteArrayOutputStream();
    PrintStream outPS = new PrintStream(out);
    PrintStream errPS = new PrintStream(err);

    System.setOut(outPS);
    System.setErr(errPS);
  }

  @Test
  public void testUsageBlurb() {
    String[] args = {"--help"};
    Simal.main(args);
    assertEquals("Error reported", "", err.toString());
    assertTrue("Usgage blurb not output", out.toString().contains("usage:"));
  }

  @Test
  public void testAddXMLFile() throws SimalRepositoryException {
    StringBuilder cmd = new StringBuilder("addxml ");
    cmd.append(ISimalRepository.class.getClassLoader().getResource(
        ModelSupport.CATEGORIES_RDF).toString());
    String[] args = { cmd.toString() };
    Simal.main(args);
    assertEquals("Error reported", "", err.toString());
  }
}
