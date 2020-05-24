package cz.cvut.fel.nss.parttimejobportal.dao;

import cz.cvut.fel.nss.parttimejobportal.model.UserReview;
import org.springframework.stereotype.Repository;

@Repository
public class UserReviewDao extends BaseDao<UserReview> {
    protected UserReviewDao() {
        super(UserReview.class);
    }
}
