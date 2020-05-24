package cz.cvut.fel.nss.parttimejobportal.dao;

import cz.cvut.fel.nss.parttimejobportal.model.AchievementSpecial;
import org.springframework.stereotype.Repository;

@Repository
public class AchievementSpecialDao extends BaseDao<AchievementSpecial>{
    protected AchievementSpecialDao() {
        super(AchievementSpecial.class);
    }
}
