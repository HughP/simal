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
