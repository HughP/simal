package uk.ac.osswatch.simal.service.jcr;

import java.net.URISyntaxException;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;

import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.model.IDoapHomepage;
import uk.ac.osswatch.simal.model.jcr.Homepage;
import uk.ac.osswatch.simal.model.jcr.JcrSimalRepository;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.AbstractService;
import uk.ac.osswatch.simal.service.IHomepageService;

public class HomepageService extends AbstractService implements IHomepageService {

	public HomepageService(ISimalRepository repo) {
		super(repo);
	}

	public IDoapHomepage createHomepage(String uri)
			throws SimalRepositoryException, DuplicateURIException {
		if (uri == null || uri.length() == 0) {
			throw new SimalRepositoryException("URI cannot be blank or null");
		}
	    if (getRepository().containsResource(uri)) {
	      throw new DuplicateURIException(
	          "Attempt to create a second homepage with the URI " + uri);
	    }

	    Homepage page= new Homepage(getNewHomepageID());
	    ((JcrSimalRepository)SimalRepositoryFactory.getInstance()).getObjectContentManager().insert(page);
	    
	    return page;
	}

	public String getNewHomepageID() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IHomepageService getOrCreateHomepage(String url)
			throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

}
