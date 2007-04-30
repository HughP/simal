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
