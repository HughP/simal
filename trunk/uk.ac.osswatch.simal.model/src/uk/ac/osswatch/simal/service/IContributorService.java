package uk.ac.osswatch.simal.service;

import java.util.Collection;

import uk.ac.osswatch.simal.model.Contributor;

public interface IContributorService {
    public Contributor findContributor(long id);
    public int save(Contributor contributor);
    public boolean delete(Contributor contributor);
    public boolean update(Contributor contributor);
    /**
     * Find all contributors known in the system
     * @return
     */
    public Collection<Contributor> findAll();

}
