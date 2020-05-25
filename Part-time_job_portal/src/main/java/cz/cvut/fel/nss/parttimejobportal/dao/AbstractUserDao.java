package cz.cvut.fel.nss.parttimejobportal.dao;

import cz.cvut.fel.nss.parttimejobportal.model.AbstractUser;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.Objects;

@Repository
public class AbstractUserDao extends BaseDao<AbstractUser> {

    public AbstractUserDao(){super(AbstractUser.class);}

    public AbstractUser find(Integer id) {
        Objects.requireNonNull(id);
        return em.find(AbstractUser.class, id);
    }

    public AbstractUser find(String email) {
        Objects.requireNonNull(email);
        return em.find(AbstractUser.class, email);
    }

    public AbstractUser findByEmail(String email) {
        try {
            return em.createNamedQuery("AbstractUser.findByEmail", AbstractUser.class).setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
