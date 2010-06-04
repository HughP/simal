package uk.ac.osswatch.simal.rdf.io;

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

import java.io.File;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

import com.hp.hpl.jena.sparql.vocabulary.DOAP;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * A set of RDF utils for working with RDF data. Typically we will call
 * preProcess(url, baseURL, repository) in order to clean up RDF data from other
 * sources and to ensure that the maximum data is available to us.
 * 
 */
public final class RDFUtils {
  public static final String PROJECT_NAMESPACE_URI = "http://simal.oss-watch.ac.uk/doap/";
  public static final String PERSON_NAMESPACE_URI = "http://simal.oss-watch.ac.uk/foaf/";
  public static final String REPOSITORY_NAMESPACE_URI = "http://simal.oss-watch.ac.uk/rcs/";
  public static final String SIMAL_NAMESPACE_URI = "http://oss-watch.ac.uk/ns/0.2/simal#";
  public static final String SIMAL_REVIEW_NAMESPACE_URI = SIMAL_NAMESPACE_URI + "Review";
  public static final String CATEGORY_NAMESPACE_URI = "http://simal.oss-watch.ac.uk/defaultCategoryNS#";
    
  public static final String SIMAL_NS = "http://oss-watch.ac.uk/ns/0.2/simal#";
  public static final String SIMAL_PERSON = SIMAL_NS + "Person";
  public static final String SIMAL_PROJECT = SIMAL_NS + "Project";
  public static final String SIMAL_CATEGORY_ID = "categoryId";
  public static final String SIMAL_PERSON_ID = "personId";
  public static final String SIMAL_PROJECT_ID = "projectId";

  static final Logger LOGGER = LoggerFactory.getLogger(RDFUtils.class);

  public static final String DOAP_NS = DOAP.NS;
  public static final String DC_NS = DC.NS;
  public static final String FOAF_NS = FOAF.NS;
  public static final String RDF_NS = RDF.getURI();
  public static final String RDFS_NS = RDFS.getURI();

  private RDFUtils() {
  }

  public static String getDefaultPersonURI(String name) {
    String uri = PERSON_NAMESPACE_URI;
    uri = uri + name;
    uri = uri + "#Person";
    uri = encode(uri);
    return uri;
  }

  public static String getDefaultReviewURI(String name) {
    String uri = "http://simal.oss-watch.ac.uk/";
    uri = uri + name;
    uri = uri + "#Review";
    uri = encode(uri);
    return uri;
  }

  public static String getDefaultProjectURI(String id) {
    String uri = PROJECT_NAMESPACE_URI;
    uri = uri + id;
    uri = uri + "#Project";
    uri = encode(uri);
    return uri;
  }

  /**
   * Encode the given string so that it can be used to create a valid URI. FOr
   * example, encode ' ' as %20
   * 
   * @param uri
   * @return
   */
  static String encode(String uri) {
    return uri.replace(" ", "%20");
  }

  /**
   * A simple method for getting an SHA1 hash from a string.
   */
  public static String getSHA1(String data) throws NoSuchAlgorithmException {
    String addr;
    data = data.toLowerCase(Locale.getDefault());
    if (data.startsWith("mailto:")) {
      addr = data.substring(7);
    } else {
      addr = data;
    }
    MessageDigest md = MessageDigest.getInstance("SHA");
    StringBuffer sb = new StringBuffer();

    md.update(addr.getBytes());
    byte[] digest = md.digest();
    for (int i = 0; i < digest.length; i++) {
      String hex = Integer.toHexString(digest[i]);
      if (hex.length() == 1) {
        hex = "0" + hex;
      }
      hex = hex.substring(hex.length() - 2);
      sb.append(hex);
    }
    return sb.toString();
  }

  /**
   * Get the File for the local, annotated version of the file with the given
   * name.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public static File getAnnotatedDoapFile(String filename)
      throws SimalRepositoryException {
    String writefile;
    File fileStoreDir;
    try {
      fileStoreDir = new File(SimalProperties
          .getProperty(SimalProperties.PROPERTY_SIMAL_DOAP_FILE_STORE)
          + File.separator + "simal-uploads");
    } catch (SimalRepositoryException e) {
      throw new SimalRepositoryException(
      "Unable to create the filestore for annotated files");
    }
    if(!fileStoreDir.mkdirs()) {
      throw new SimalRepositoryException(
      "Unable to create the filestore for annotated files");
    }

    String path = fileStoreDir.getAbsolutePath();
    if (!(filename.endsWith(".rdf") || filename.endsWith(".xml"))) {
      writefile = filename + ".rdf";
    } else {
      writefile = filename;
    }
    File file = new File(path + File.separator + System.currentTimeMillis()
        + "_" + writefile);
    return file;
  }

  /**
   * Get the File for the local, annotated version of the file located at the
   * the given URL.
   * 
   * @return
   * @throws SimalRepositoryException
   */
  public static File getAnnotatedFile(URL url) throws SimalRepositoryException {
    String filename;
    String path = url.getPath();
    int startName = path.lastIndexOf("/");
    if (startName >= 0) {
      filename = path.substring(startName + 1);
    } else {
      filename = path;
    }
    return getAnnotatedDoapFile(filename);
  }
}