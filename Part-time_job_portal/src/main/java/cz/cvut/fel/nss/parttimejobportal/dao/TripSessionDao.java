package cz.cvut.fel.nss.parttimejobportal.dao;

import cz.cvut.fel.nss.parttimejobportal.model.JobSession;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class TripSessionDao extends BaseDao<JobSession> {
    public TripSessionDao() {
        super(JobSession.class);
    }

    public List<JobSession> find(String trip_short_name){
        {
            try {
                return em.createNamedQuery("JobSession.findByTrip", JobSession.class).setParameter("trip_short_name", trip_short_name)
                        .getResultList();
            } catch (NoResultException e) {
                return null;
            }
        }
    }
}