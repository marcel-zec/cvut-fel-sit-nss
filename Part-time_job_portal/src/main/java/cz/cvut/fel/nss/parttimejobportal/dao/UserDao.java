package cz.cvut.fel.nss.parttimejobportal.dao;

import cz.cvut.fel.nss.parttimejobportal.model.AbstractUser;
import cz.cvut.fel.nss.parttimejobportal.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.Objects;


@Repository
public class UserDao extends BaseDao<User> {
    public UserDao(){super(User.class);}

    public User find(Integer id) {
        Objects.requireNonNull(id);
        return em.find(User.class, id);
    }

    public User findByEmail(String email) {
        try {
            return em.createNamedQuery("User.findByEmail", User.class).setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}