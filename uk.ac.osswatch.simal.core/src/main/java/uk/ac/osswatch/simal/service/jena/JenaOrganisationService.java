package uk.ac.osswatch.simal.service.jena;

import java.util.HashSet;
import java.util.Set;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

import uk.ac.osswatch.simal.model.Foaf;
import uk.ac.osswatch.simal.model.IOrganisation;
import uk.ac.osswatch.simal.model.jena.Organisation;
import uk.ac.osswatch.simal.model.jena.simal.JenaSimalRepository;
import uk.ac.osswatch.simal.rdf.DuplicateURIException;
import uk.ac.osswatch.simal.rdf.ISimalRepository;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.service.AbstractService;
import uk.ac.osswatch.simal.service.IOrganisationService;

public class JenaOrganisationService extends AbstractService implements
		IOrganisationService {

	public JenaOrganisationService(ISimalRepository simalRepository) {
		super(simalRepository);
	}

	public IOrganisation create(String uri) throws DuplicateURIException,
			SimalRepositoryException {
		if (getRepository().containsProject(uri)) {
			throw new DuplicateURIException(
					"Attempt to create a second project with the URI " + uri);
		}

		Model model = ((JenaSimalRepository) getRepository()).getModel();
		com.hp.hpl.jena.rdf.model.Resource foafOrg = model.createResource(uri);
		Statement s = model.createStatement(foafOrg, RDF.type,
				Foaf.ORGANIZATION);
		model.add(s);

		IOrganisation org = new Organisation(foafOrg);
		return org;
	}

	public String getNewOrganisationID() throws SimalRepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	public IOrganisation getOrganisationById(String fullID) {
		// TODO Auto-generated method stub
		return null;
	}

	public IOrganisation get(String uri)
			throws SimalRepositoryException {
		Model model = ((JenaSimalRepository) getRepository()).getModel();
		if (getRepository().containsOrganisation(uri)) {
			return new Organisation(model.getResource(uri));
		} else {
			return null;
		}
	}

	/**
	 * Get all the organisations known in this repository.
	 * 
	 * @return
	 * @throws SimalRepositoryException
	 */
	public Set<IOrganisation> getAll() throws SimalRepositoryException {
		Model model = ((JenaSimalRepository) getRepository()).getModel();
		StmtIterator itr = model.listStatements(null, RDF.type,
				Foaf.ORGANIZATION);
		Set<IOrganisation> orgs = new HashSet<IOrganisation>();
		while (itr.hasNext()) {
			String uri = itr.nextStatement().getSubject().getURI();
			orgs.add(new Organisation(model.getResource(uri)));
		}
		return orgs;
	}

}
