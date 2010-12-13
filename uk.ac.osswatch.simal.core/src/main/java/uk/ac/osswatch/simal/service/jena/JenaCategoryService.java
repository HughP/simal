package uk.ac.osswatch.simal.service.jena;
/*
 * Copyright 2007 University of Oxford 
 *  
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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.jena.Category;
import uk.ac.osswatch.simal.model.jena.simal.JenaSimalRepository;
import uk.ac.osswatch.simal.rdf.AbstractSimalRepository;
import uk.ac.osswatch.simal.rdf.Doap;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.ICategoryService;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;

public class JenaCategoryService extends JenaService implements
		ICategoryService {
	public static final Logger logger = LoggerFactory
			.getLogger(JenaCategoryService.class);

	public JenaCategoryService(ISimalRepository simalRepository) {
		super(simalRepository);
	}

	public IDoapCategory create(String uri) throws DuplicateURIException,
			SimalRepositoryException {
		if (getRepository().containsResource(uri)) {
			throw new DuplicateURIException(
					"Attempt to create a second project category with the URI "
							+ uri);
		}

		Model model = ((JenaSimalRepository) getRepository()).getModel();
		Property o = Doap.CATEGORY;
		com.hp.hpl.jena.rdf.model.Resource r = model.createResource(uri);
		Statement s = model.createStatement(r, RDF.type, o);
		model.add(s);

		IDoapCategory cat = new Category(r);
		cat.setSimalID(getNewID());
		return cat;
	}

	public IDoapCategory findById(String id) throws SimalRepositoryException {
		String queryStr = "PREFIX rdf: <"
				+ AbstractSimalRepository.RDF_NAMESPACE_URI + ">"
				+ "PREFIX simal: <"
				+ AbstractSimalRepository.SIMAL_NAMESPACE_URI + ">"
				+ "SELECT DISTINCT ?category WHERE { "
				+ "?category simal:categoryId \"" + id + "\"}";
		Query query = QueryFactory.create(queryStr);
		Model model = ((JenaSimalRepository) getRepository()).getModel();
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();

		IDoapCategory category = null;
		while (results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			RDFNode node = soln.get("category");
			if (node.isResource()) {
				category = new Category(
						(com.hp.hpl.jena.rdf.model.Resource) node);
			}
		}
		qe.close();

		return category;
	}

	public IDoapCategory get(String uri) throws SimalRepositoryException {
		if (getRepository().containsResource(uri)) {
			Model model = ((JenaSimalRepository) getRepository()).getModel();
			return new Category(model.getResource(uri));
		} else {
			return null;
		}
	}

	public Set<IDoapCategory> getAll() throws SimalRepositoryException {
		Model model = ((JenaSimalRepository) getRepository()).getModel();
	    NodeIterator itr = model.listObjectsOfProperty(Doap.CATEGORY);
	    Set<IDoapCategory> categories = new HashSet<IDoapCategory>();
	    while (itr.hasNext()) {
	      String uri = itr.nextNode().toString();
	      categories.add(new Category(model.getResource(uri)));
	    }
	    return categories;
	}

	public String getNewID() throws SimalRepositoryException {
	  return getNewID(SimalProperties.PROPERTY_SIMAL_NEXT_CATEGORY_ID, "cat");
	}

	public IDoapCategory getOrCreate(String uri)
			throws SimalRepositoryException {
		if (getRepository().containsResource(uri)) {
			return get(uri);
		} else {
			try {
				return create(uri);
			} catch (DuplicateURIException e) {
				logger
						.error(
								"Threw a DuplicateURIEception when we had already checked for resource existence",
								e);
				return null;
			}
		}
	}

  /**
   * @see uk.ac.osswatch.simal.service.ICategoryService#getAllCategoriesAsJSON()
   */
  public String getAllCategoriesAsJSON() throws SimalRepositoryException {
    StringBuffer json = new StringBuffer("{ \"items\": [");
    Iterator<IDoapCategory> categories = getAll().iterator();
    IDoapCategory category;
    while (categories.hasNext()) {
      category = categories.next();
      json.append(category.toJSONRecord());
      if (categories.hasNext()) {
        json.append(",");
      }
    }
    json.append("]}");
    return json.toString();  }

}
