package cz.cvut.fel.nss.parttimejobportal.dao;

import cz.cvut.fel.nss.parttimejobportal.model.Offer;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Repository
public class TripDao extends BaseDao<Offer> {
    public TripDao() {
        super(Offer.class);
    }

    public Offer find(String id){
        {
            try {
                return em.createNamedQuery("Offer.findByStringId", Offer.class).setParameter("id", id)
                        .getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        }
    }

    public List<Offer> find(int required_level){

        {
            try {
                return em.createNamedQuery("Offer.findByLevel", Offer.class).setParameter("required_level", required_level)
                        .getResultList();
            } catch (NoResultException e) {
                return null;
            }
        }
    }

    public List<Offer> findByFilter(String location, LocalDate from_date, LocalDate to_date, Double maxPrice, String[] search){

        try {
                List<Offer> filteredTrips = new ArrayList<>();
                //all trips matching filter, not search words
                filteredTrips = em.createNamedQuery("Offer.findByFilter", Offer.class)
                        .setParameter("location", location)
                        .setParameter("from_date", from_date)
                        .setParameter("to_date", to_date)
                        .setParameter("maxPrice", maxPrice)
                        .getResultList();

                List<Long> ids = new ArrayList<>();
                for (Offer t : filteredTrips){
                    ids.add(t.getId());
                }

                //from all trips that match filter we will find trips matching search words
                if(search != null) {
                    String pattern = null;
                    filteredTrips = null;
                    for (String s : search) {
                        pattern = "%" + s + "%";

                        List<Offer> filteredTrips2 = em.createNamedQuery("Offer.findByPattern", Offer.class)
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