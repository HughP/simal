/*
* Copyright 2007 University of Oxford
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
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

    public Contributor findContributor(long id) {
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
