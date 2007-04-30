package uk.ac.osswatch.simal.service.derby;

import java.util.HashSet;
import java.util.Set;

import uk.ac.osswatch.simal.model.Contributor;
import uk.ac.osswatch.simal.service.IContributorService;

public class ManagedContributorBean implements IContributorService {
    Set<Contributor> contributors;
    
    public boolean delete(Contributor contributor) {
        // TODO Auto-generated method stub
        return false;
    }

    public Contributor findContributor(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    public int save(Contributor contributor) {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean update(Contributor contributor) {
        // TODO Auto-generated method stub
        return false;
    }

    private void init() {
        contributors = new HashSet<Contributor>();
        Contributor contributor = new Contributor(
                "Contributor 1",
                "cont1@test.com");
        contributors.add(contributor);
        
        contributor = new Contributor(
                "Contributor 2",
                "cont2@test.com");
        contributors.add(contributor);
        
        contributor = new Contributor(
                "Contributor 3",
                "cont3@test.com");
        contributors.add(contributor);
    }

    public Set<Contributor> findAll() {
        if (contributors == null) init();
        return contributors;
    }
}
