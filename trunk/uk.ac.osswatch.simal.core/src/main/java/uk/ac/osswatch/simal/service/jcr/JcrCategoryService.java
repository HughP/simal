package uk.ac.osswatch.simal.service.jcr;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IDoapCategory;
import uk.ac.osswatch.simal.model.jcr.Category;
import uk.ac.osswatch.simal.model.jcr.JcrSimalRepository;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;
import uk.ac.osswatch.simal.service.AbstractService;
import uk.ac.osswatch.simal.service.ICategoryService;

public class JcrCategoryService extends AbstractService implements
		ICategoryService {
	public static final Logger logger = LoggerFactory
			.getLogger(JcrCategoryService.class);

	public JcrCategoryService(ISimalRepository simalRepository) {
		super(simalRepository);
	}

	public IDoapCategory create(String uri) throws DuplicateURIException,
			SimalRepositoryException {
		String simalCategoryURI;
		if (!uri.startsWith(RDFUtils.PROJECT_NAMESPACE_URI)) {
			String projectID = getNewID();
			simalCategoryURI = RDFUtils.getDefaultProjectURI(projectID);
			logger.debug("Creating a new Simal Categroy instance with URI: "
					+ simalCategoryURI);
		} else {
			simalCategoryURI = uri;
		}

		Category cat = new Category(getRepository().getEntityID(
				getNewID()));
		((JcrSimalRepository) SimalRepositoryFactory.getInstance())
				.getObjectContentManager().insert(cat);

		return cat;
	}

	public IDoapCategory findById(String id) throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IDoapCategory get(String uri) throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<IDoapCategory> getAll() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
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
		if (SimalRepositoryFactory.getInstance().containsResource(uri)) {
			return get(uri);
		} else {
			try {
				return create(uri);
			} catch (DuplicateURIException e) {
				logger.error("Threw a DuplicateURIException when we had already checked for resource existence", e);
				return null;
			}
		}
	}

}
