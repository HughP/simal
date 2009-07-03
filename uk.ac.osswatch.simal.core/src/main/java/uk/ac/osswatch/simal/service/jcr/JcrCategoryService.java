package uk.ac.osswatch.simal.service.jcr;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		// TODO Auto-generated method stub
		return null;
	}

	public IDoapCategory getOrCreate(String uri)
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

}
