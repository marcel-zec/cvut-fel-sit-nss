package cz.cvut.fel.nss.parttimejobportal.dao;

import cz.cvut.fel.nss.parttimejobportal.model.AchievementCategorized;
import org.springframework.stereotype.Repository;

@Repository
public class AchievementCategorizedDao extends BaseDao<AchievementCategorized> {
    protected AchievementCategorizedDao() {
        super(AchievementCategorized.class);
    }
}
