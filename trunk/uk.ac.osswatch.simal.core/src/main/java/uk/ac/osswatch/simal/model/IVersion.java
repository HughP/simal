package uk.ac.osswatch.simal.model;

import java.util.Set;

/**
 * A wrapper around a repository representation of a version of a
 * project release.
 * 
 * @see org.openrdf.concepts.doap.Version
 */
public interface IVersion extends IDoapResource {

	public abstract Set<String> getFileReleases();
	public abstract Set<String> getRevisions();
}
