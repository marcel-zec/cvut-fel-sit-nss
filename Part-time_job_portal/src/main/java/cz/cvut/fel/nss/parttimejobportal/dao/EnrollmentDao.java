package cz.cvut.fel.nss.parttimejobportal.dao;

import cz.cvut.fel.nss.parttimejobportal.model.Enrollment;
import org.springframework.stereotype.Repository;

@Repository
public class EnrollmentDao extends BaseDao<Enrollment> {
    protected EnrollmentDao() {
        super(Enrollment.class);
    }
}
