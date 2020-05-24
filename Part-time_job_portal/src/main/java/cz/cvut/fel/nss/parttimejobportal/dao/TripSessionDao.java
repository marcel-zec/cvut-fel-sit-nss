package cz.cvut.fel.nss.parttimejobportal.dao;

import cz.cvut.fel.nss.parttimejobportal.model.TripSession;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class TripSessionDao extends BaseDao<TripSession> {
    public TripSessionDao() {
        super(TripSession.class);
    }

    public List<TripSession> find(String trip_short_name){
        {
            try {
                return em.createNamedQuery("TripSession.findByTrip", TripSession.class).setParameter("trip_short_name", trip_short_name)
                        .getResultList();
            } catch (NoResultException e) {
                return null;
            }
        }
    }
}