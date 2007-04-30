package uk.ac.osswatch.simal.service.derby;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAResourceBean {

    protected static EntityManagerFactory emf;

    private JPAResourceBean() {
    };

    /*
     * Lazily acquire the EntityManagerFactory and cache it.
     */
    public static EntityManagerFactory getEMF() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("default",
                    new java.util.HashMap());
        }
        return emf;
    }
}
