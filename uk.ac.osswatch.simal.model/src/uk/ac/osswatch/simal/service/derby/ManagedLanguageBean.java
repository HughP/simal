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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import uk.ac.osswatch.simal.model.Language;
import uk.ac.osswatch.simal.service.ILanguageService;

public class ManagedLanguageBean implements ILanguageService {
    Set<Language> Languages;
    
    public boolean delete(Language Language) {
        // TODO Auto-generated method stub
        return false;
    }

    public Language findLanguage(long id) {
        EntityManager em = JPAResourceBean.getEMF().createEntityManager();
        try {
            return em.find(Language.class, id);
        } finally {
            em.close();
        }
    }

    public int save(Language Language) {
        EntityManager em = JPAResourceBean.getEMF().createEntityManager();
        em.getTransaction().begin();
        if (Language.getId() != 0) {
            em.merge(Language);
        } else {
            em.persist(Language);
        }
        em.getTransaction().commit();
        return 1;
    }

    public boolean update(Language Language) {
        // TODO Auto-generated method stub
        return false;
    }
    
    public Collection<Language> findAll() {
        EntityManager em = JPAResourceBean.getEMF().createEntityManager();
        try {
           return em. createQuery("SELECT l FROM Language l").getResultList();
        } finally {
            em.close();
        }
    }
}
