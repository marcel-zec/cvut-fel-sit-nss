package cz.cvut.fel.nss.parttimejobportal.dao;

import cz.cvut.fel.nss.parttimejobportal.model.Manager;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.Objects;

@Repository
public class ManagerDao extends BaseDao<Manager>{

    public ManagerDao() {
        super(Manager.class);
    }


    public Manager find(Integer id) {
        Objects.requireNonNull(id);
        return em.find(Manager.class, id);
    }

    public Manager findByEmail(String email) {
        try {
            return em.createNamedQuery("Manager.findByEmail", Manager.class).setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
