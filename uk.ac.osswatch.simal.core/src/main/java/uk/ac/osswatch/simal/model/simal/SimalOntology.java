/* CVS $Id: $ */
package uk.ac.osswatch.simal.model.simal;

/*
 * 
 * Copyright 2007 University of Oxford * Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 */

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Vocabulary definitions from src/main/resources/schema/simalOntology.rdf
 * 
 * @author Auto-generated by schemagen on 20 Jul 2008 19:44
 */
public class SimalOntology {
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
  public static final String NS = "http://oss-watch.ac.uk/ns/0.2/simal#";

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

  public static final Property PERSON = m_model
      .createProperty("http://oss-watch.ac.uk/ns/0.2/simal#Person");

  public static final Property PROJECT = m_model
      .createProperty("http://oss-watch.ac.uk/ns/0.2/simal#Project");
  
  public static final Property CATEGORY_ID = m_model
      .createProperty("http://oss-watch.ac.uk/ns/0.2/simal#categoryId");

  public static final Property PERSON_ID = m_model
      .createProperty("http://oss-watch.ac.uk/ns/0.2/simal#personId");
  
  public static final Property PERSON_USERNAME =  m_model
      .createProperty("http://oss-watch.ac.uk/ns/0.2/simal#username");

  public static final Property PERSON_PASSWORD =  m_model
      .createProperty("http://oss-watch.ac.uk/ns/0.2/simal#password");

  public static final Property PROJECT_ID = m_model
      .createProperty("http://oss-watch.ac.uk/ns/0.2/simal#projectId");
  
  public static final Property REVIEW = m_model
    .createProperty("http://oss-watch.ac.uk/ns/0.2/simal#Review");

  public static final Property DATE = m_model
    .createProperty("http://oss-watch.ac.uk/ns/0.2/simal#date");

  public static final Property TYPE = m_model
    .createProperty("http://oss-watch.ac.uk/ns/0.2/simal#type");

}
