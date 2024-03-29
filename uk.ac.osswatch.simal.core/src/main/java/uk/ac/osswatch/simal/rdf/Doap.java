package uk.ac.osswatch.simal.rdf;

/*
 * Copyright 2007, 2010 University of Oxford 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Vocabulary definitions from http://usefulinc.com/ns/doap#
 * 
 * @author Auto-generated by schemagen on 20 Jul 2008 04:04
 * Enhanced manually to incorporate new additions.
 */
public class Doap {
  /**
   * <p>
   * The RDF model that holds the vocabulary terms
   * </p>
   */
  private static Model m_model = ModelFactory.createDefaultModel();

  /**
   * <p>
   * The namespace of the vocabulary as a string
   * </p>
   */
  public static final String NS = "http://usefulinc.com/ns/doap#";

  /**
   * <p>
   * The namespace of the vocabulary as a string
   * </p>
   * 
   * @see #NS
   */
  public static String getURI() {
    return NS;
  }

  /**
   * <p>
   * The namespace of the vocabulary as a resource
   * </p>
   */
  public static final Resource NAMESPACE = m_model.createResource(NS);

  /**
   * <p>
   * Repository for anonymous access.
   * </p>
   */
  public static final Property ANON_ROOT = m_model
      .createProperty("http://usefulinc.com/ns/doap#anon-root");

  /**
   * <p>
   * Web browser interface to repository.
   * </p>
   */
  public static final Property BROWSE = m_model
      .createProperty("http://usefulinc.com/ns/doap#browse");

  /**
   * <p>
   * Bug tracker para un proyecto.Suivi des bugs pour un projet.Bug tracker for
   * a project.
   * </p>
   */
  public static final Property BUG_DATABASE = m_model
      .createProperty("http://usefulinc.com/ns/doap#bug-database");

  /**
   * <p>
   * A category of project.
   * </p>
   */
  public static final Property CATEGORY = m_model
      .createProperty("http://usefulinc.com/ns/doap#category");

  /**
   * <p>
   * URI of a blog related to a project.
   * </p>
   */
  public static final Property BLOG = m_model
      .createProperty("http://usefulinc.com/ns/doap#blog");
  
  /**
   * <p>
   * Date when something was created, in YYYY-MM-DD form. e.g. 2004-04-05
   * </p>
   */
  public static final Property CREATED = m_model
      .createProperty("http://usefulinc.com/ns/doap#created");

  /**
   * <p>
   * Plain text description of a project, of 2-4 sentences in length.
   * </p>
   */
  public static final Property DESCRIPTION = m_model
      .createProperty("http://usefulinc.com/ns/doap#description");

  /**
   * <p>
   * Developer of software for the project.
   * </p>
   */
  public static final Property DEVELOPER = m_model
      .createProperty("http://usefulinc.com/ns/doap#developer");

  /**
   * <p>
   * Contributor of documentation to the project.
   * </p>
   */
  public static final Property DOCUMENTER = m_model
      .createProperty("http://usefulinc.com/ns/doap#documenter");

  /**
   * <p>
   * Mirror of software download web page.
   * </p>
   */
  public static final Property DOWNLOAD_MIRROR = m_model
      .createProperty("http://usefulinc.com/ns/doap#download-mirror");

  /**
   * <p>
   * Web page from which the project software can be downloaded.
   * </p>
   */
  public static final Property DOWNLOAD_PAGE = m_model
      .createProperty("http://usefulinc.com/ns/doap#download-page");

  /**
   * <p>
   * URI of download associated with this release.
   * </p>
   */
  public static final Property FILE_RELEASE = m_model
      .createProperty("http://usefulinc.com/ns/doap#file-release");

  /**
   * <p>
   * Project contributor.
   * </p>
   */
  public static final Property HELPER = m_model
      .createProperty("http://usefulinc.com/ns/doap#helper");

  /**
   * <p>
   * URL of a project's homepage, associated with exactly one project.
   * </p>
   */
  public static final Property HOMEPAGE = m_model
      .createProperty("http://usefulinc.com/ns/doap#homepage");

  /**
   * <p>
   * The URI of an RDF description of the license the software is distributed
   * under.
   * </p>
   */
  public static final Property LICENSE = m_model
      .createProperty("http://usefulinc.com/ns/doap#license");

  /**
   * <p>
   * Location of a repository.
   * </p>
   */
  public static final Property LOCATION = m_model
      .createProperty("http://usefulinc.com/ns/doap#location");

  /**
   * <p>
   * Mailing list home page or email address.
   * </p>
   */
  public static final Property MAILING_LIST = m_model
      .createProperty("http://usefulinc.com/ns/doap#mailing-list");

  /**
   * <p>
   * Maintainer of a project, a project leader.
   * </p>
   */
  public static final Property MAINTAINER = m_model
      .createProperty("http://usefulinc.com/ns/doap#maintainer");

  /**
   * <p>
   * Module name of a CVS, BitKeeper or Arch repository.
   * </p>
   */
  public static final Property MODULE = m_model
      .createProperty("http://usefulinc.com/ns/doap#module");

  /**
   * <p>
   * A name of something.
   * </p>
   */
  public static final Property NAME = m_model
      .createProperty("http://usefulinc.com/ns/doap#name");

  /**
   * <p>
   * URL of a project's past homepage, associated with exactly one project.
   * </p>
   */
  public static final Property OLD_HOMEPAGE = m_model
      .createProperty("http://usefulinc.com/ns/doap#old-homepage");

  /**
   * <p>
   * Operating system that a project is limited to. Omit this property if the
   * project is not OS-specific.
   * </p>
   */
  public static final Property OS = m_model
      .createProperty("http://usefulinc.com/ns/doap#os");

  /**
   * <p>
   * Programming language a project is implemented in or intended for use with.
   * </p>
   */
  public static final Property PROGRAMMING_LANGUAGE = m_model
      .createProperty("http://usefulinc.com/ns/doap#programming-language");

  /**
   * <p>
   * A project release.
   * </p>
   */
  public static final Property RELEASE = m_model
      .createProperty("http://usefulinc.com/ns/doap#release");

  /**
   * <p>
   * Source code repository.
   * </p>
   */
  public static final Property REPOSITORY = m_model
      .createProperty("http://usefulinc.com/ns/doap#repository");

  /**
   * <p>
   * Revision identifier of a software release.
   * </p>
   */
  public static final Property REVISION = m_model
      .createProperty("http://usefulinc.com/ns/doap#revision");

  /**
   * <p>
   * Web page with screenshots of project.
   * </p>
   */
  public static final Property SCREENSHOTS = m_model
      .createProperty("http://usefulinc.com/ns/doap#screenshots");

  /**
   * <p>
   * Short (8 or 9 words) plain text description of a project.
   * </p>
   */
  public static final Property SHORTDESC = m_model
      .createProperty("http://usefulinc.com/ns/doap#shortdesc");

  /**
   * <p>
   * A tester or other quality control contributor.
   * </p>
   */
  public static final Property TESTER = m_model
      .createProperty("http://usefulinc.com/ns/doap#tester");

  /**
   * <p>
   * Contributor of translations to the project.
   * </p>
   */
  public static final Property TRANSLATOR = m_model
      .createProperty("http://usefulinc.com/ns/doap#translator");

  /**
   * <p>
   * URL of Wiki for collaborative discussion of project.
   * </p>
   */
  public static final Property WIKI = m_model
      .createProperty("http://usefulinc.com/ns/doap#wiki");

  /**
   * <p>
   * GNU Arch source code repository.
   * </p>
   */
  public static final Resource ARCH_REPOSITORY = m_model
      .createResource("http://usefulinc.com/ns/doap#ArchRepository");

  /**
   * <p>
   * Bazaar source code repository.
   * </p>
   */
  public static final Resource BAZAARREPOSITORY = m_model
      .createResource("http://usefulinc.com/ns/doap#BazaarBranch");

  /**
   * <p>
   * BitKeeper source code repository.
   * </p>
   */
  public static final Resource BKREPOSITORY = m_model
      .createResource("http://usefulinc.com/ns/doap#BKRepository");

  /**
   * <p>
   * CVS source code repository.
   * </p>
   */
  public static final Resource CVSREPOSITORY = m_model
      .createResource("http://usefulinc.com/ns/doap#CVSRepository");

  /**
   * <p>
   * darcs source code repository.
   * </p>
   */
  public static final Resource DARCSREPOSITORY = m_model
      .createResource("http://usefulinc.com/ns/doap#DarcsRepository");

  /**
   * <p>
   * Git source code repository.
   * </p>
   */
  public static final Resource GITREPOSITORY = m_model
      .createResource("http://usefulinc.com/ns/doap#GitRepository");

  /**
   * <p>
   * Mercurial source code repository.
   * </p>
   */
  public static final Resource HGREPOSITORY = m_model
      .createResource("http://usefulinc.com/ns/doap#HgRepository");

  /**
   * <p>
   * SVN source code repository.
   * </p>
   */
  public static final Resource SVNREPOSITORY = m_model
      .createResource("http://usefulinc.com/ns/doap#SVNRepository");

}
