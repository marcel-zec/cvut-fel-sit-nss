package cz.cvut.fel.nss.parttimejobportal.dao;

import cz.cvut.fel.nss.parttimejobportal.model.AchievementCertificate;
import org.springframework.stereotype.Repository;

@Repository
public class AchievementCertificateDao extends BaseDao<AchievementCertificate> {
    protected AchievementCertificateDao() {
        super(AchievementCertificate.class);
    }
}
