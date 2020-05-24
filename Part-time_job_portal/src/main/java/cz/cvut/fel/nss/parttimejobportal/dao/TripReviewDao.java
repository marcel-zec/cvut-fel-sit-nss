package cz.cvut.fel.nss.parttimejobportal.dao;

import cz.cvut.fel.nss.parttimejobportal.model.TripReview;
import org.springframework.stereotype.Repository;


@Repository
public class TripReviewDao extends BaseDao<TripReview> {
    public TripReviewDao() {
        super(TripReview.class);
    }
}