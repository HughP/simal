package uk.ac.osswatch.simal.service.derby;


import java.util.Collection;

import javax.persistence.EntityManager;

import uk.ac.osswatch.simal.model.Project;
import uk.ac.osswatch.simal.service.IProjectService;

public class ManagedProjectBean implements IProjectService {

    public boolean delete(Project project) {
        // TODO Auto-generated method stub
        return false;
    }

    public Project findProject(long id) {
        EntityManager em = JPAResourceBean.getEMF().createEntityManager();
        try {
            return em.find(Project.class, id);
        } finally {
            em.close();
        }
    }

    public int save(Project project) {
        EntityManager em = JPAResourceBean.getEMF().createEntityManager();
        em.getTransaction().begin();
        if (project.getId() != 0) {
            em.merge(project);
        } else {
            em.persist(project);
        }
        em.getTransaction().commit();
        return 1;
    }

    public boolean update(Project project) {
        // TODO Auto-generated method stub
        return false;
    }

    public Collection<Project> findAll() {
        EntityManager em = JPAResourceBean.getEMF().createEntityManager();
        try {
           return em. createQuery("SELECT p FROM Project p").getResultList();
        } finally {
            em.close();
        }
    }

}
