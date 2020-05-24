package cz.cvut.fel.nss.parttimejobportal.dao;

import cz.cvut.fel.nss.parttimejobportal.model.Trip;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Repository
public class TripDao extends BaseDao<Trip> {
    public TripDao() {
        super(Trip.class);
    }

    public Trip find(String id){
        {
            try {
                return em.createNamedQuery("Trip.findByStringId", Trip.class).setParameter("id", id)
                        .getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        }
    }

    public List<Trip> find(int required_level){

        {
            try {
                return em.createNamedQuery("Trip.findByLevel", Trip.class).setParameter("required_level", required_level)
                        .getResultList();
            } catch (NoResultException e) {
                return null;
            }
        }
    }

    public List<Trip> findByFilter(String location, LocalDate from_date, LocalDate to_date, Double maxPrice, String[] search){

        try {
                List<Trip> filteredTrips = new ArrayList<>();
                //all trips matching filter, not search words
                filteredTrips = em.createNamedQuery("Trip.findByFilter", Trip.class)
                        .setParameter("location", location)
                        .setParameter("from_date", from_date)
                        .setParameter("to_date", to_date)
                        .setParameter("maxPrice", maxPrice)
                        .getResultList();

                List<Long> ids = new ArrayList<>();
                for (Trip t : filteredTrips){
                    ids.add(t.getId());
                }

                //from all trips that match filter we will find trips matching search words
                if(search != null) {
                    String pattern = null;
                    filteredTrips = null;
                    for (String s : search) {
                        pattern = "%" + s + "%";

                        List<Trip> filteredTrips2 = em.createNamedQuery("Trip.findByPattern", Trip.class)
                                .setParameter("ids", ids)
                                .setParameter("pattern", pattern)
                                .getResultList();

                        if (filteredTrips2 != null) {
                            if (filteredTrips == null) filteredTrips = filteredTrips2;
                            else filteredTrips.addAll(filteredTrips2);
                        }
                    }
                }
                return filteredTrips;
        } catch (NoResultException e) {
                return null;
        }
    }
}