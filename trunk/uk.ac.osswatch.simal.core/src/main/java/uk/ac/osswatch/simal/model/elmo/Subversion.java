package uk.ac.osswatch.simal.model.elmo;

import org.openrdf.concepts.doap.Repository;

import uk.ac.osswatch.simal.model.ISubversion;

/**
 * A wrapper around an Elmo SVNRepository.
 *
 * @see org.openrdf.concepts.doap.SVNRepository
 *
 */
public class Subversion extends RCS implements ISubversion {
	private static final long serialVersionUID = 8614801322874710508L;

	public Subversion(Repository elmoRCS) {
		super(elmoRCS);
	}
}
