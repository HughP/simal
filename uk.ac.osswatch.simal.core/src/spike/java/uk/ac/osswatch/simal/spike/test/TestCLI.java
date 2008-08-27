package uk.ac.osswatch.simal.spike.test;
/*
 * Copyright 2007 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
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
  static PrintStream origError;
  static PrintStream origOut;
  static ByteArrayOutputStream out;
  static ByteArrayOutputStream err;

  /**
   * Capture the streams so we can listen
   * 
   */
  @BeforeClass
  public static void setUp() {
    origError = System.err;
    origOut = System.out;

    out = new ByteArrayOutputStream();
    err = new ByteArrayOutputStream();
    PrintStream outPS = new PrintStream(out);
    PrintStream errPS = new PrintStream(err);

    System.setOut(outPS);
    System.setErr(errPS);
  }

  /**
   * Reset System.out and System.err
   * @throws SimalRepositoryException 
   * 
   */
  @AfterClass
  public static void tearDown() throws SimalRepositoryException {
    System.setOut(origOut);
    System.setErr(origError);
    
    Simal.getRepository().destroy();
  }
  
  /**
   * Dump log details.
   */
  @After
  public void dumpLog() {
    origOut.append("Log output:\n");
    origOut.append(out.toString());
    origError.append("\n\nError output:\n");
    origError.append(err.toString());
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
    String cmd = "addxml";
    String url = ISimalRepository.class.getClassLoader().getResource(
        ModelSupport.TEST_FILE_URI_NO_QNAME).toString();
    String[] args = { cmd, url };
    Simal.main(args);
    assertEquals("Error reported", "", err.toString());
    assertTrue(out.toString().contains("Data added."));
  }
}
