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
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean update(Project project) {
        // TODO Auto-generated method stub
        return false;
    }

    public Collection<Project> findAll() {
        EntityManager em = JPAResourceBean.getEMF().createEntityManager();
        try {
           return em.createQuery("Select new oracle.toplink.jpa.example.inventory.nonentity.Category(i.category) from Item i group by i.category").getResultList();
        } finally {
            em.close();
        }
    }

    public void createNewProject(Project project) {
        EntityManager em = JPAResourceBean.getEMF().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(project);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

}
