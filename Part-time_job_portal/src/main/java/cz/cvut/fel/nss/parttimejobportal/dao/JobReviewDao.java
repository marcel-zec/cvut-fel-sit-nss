package cz.cvut.fel.nss.parttimejobportal.dao;

import cz.cvut.fel.nss.parttimejobportal.model.JobReview;
import org.springframework.stereotype.Repository;


@Repository
public class JobReviewDao extends BaseDao<JobReview> {
    public JobReviewDao() {
        super(JobReview.class);
    }
}