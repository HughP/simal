package uk.ac.osswatch.simal.service.jena;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.IOrganisation;
import uk.ac.osswatch.simal.model.jena.Category;
import uk.ac.osswatch.simal.model.jena.simal.JenaSimalRepository;
import uk.ac.osswatch.simal.rdf.AbstractSimalRepository;
import uk.ac.osswatch.simal.rdf.Doap;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.AbstractService;
import uk.ac.osswatch.simal.service.ICategoryService;

public class JenaCategoryService extends AbstractService implements
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
		String fullID = null;
		String strEntityID = SimalProperties.getProperty(
				SimalProperties.PROPERTY_SIMAL_NEXT_CATEGORY_ID, "1");
		long entityID = Long.parseLong(strEntityID);

		/**
		 * If the properties file is lost for any reason the next ID value will
		 * be lost. We therefore need to perform a sanity check that this is
		 * unique.
		 */
		boolean validID = false;
		while (!validID) {
			fullID = getRepository().getUniqueSimalID(
					"cat" + Long.toString(entityID));
			if (findById(fullID) == null) {
				validID = true;
			} else {
				entityID = entityID + 1;
			}
		}

		long newId = entityID + 1;
		SimalProperties.setProperty(
				SimalProperties.PROPERTY_SIMAL_NEXT_CATEGORY_ID, Long
						.toString(newId));
		try {
			SimalProperties.save();
		} catch (Exception e) {
			logger.warn("Unable to save properties file", e);
			throw new SimalRepositoryException(
					"Unable to save properties file when creating the next category ID",
					e);
		}
		return fullID;
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

}
