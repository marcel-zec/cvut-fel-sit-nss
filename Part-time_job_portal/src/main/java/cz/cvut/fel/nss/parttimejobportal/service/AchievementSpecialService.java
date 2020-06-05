package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.model.AchievementSpecial;
import cz.cvut.fel.nss.parttimejobportal.dao.AchievementSpecialDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class AchievementSpecialService {

    private final AchievementSpecialDao achievementSpecialDao;

    @Autowired
    public AchievementSpecialService(AchievementSpecialDao achievementSpecialDao) {
        this.achievementSpecialDao = achievementSpecialDao;
    }


    /**
     * Get all AchievementsSpecial from database.
     * @return  List<AchievementSpecial>
     */
    @Transactional
    public List<AchievementSpecial> findAll() {
        return achievementSpecialDao.findAll();
    }


    /**
     * Get one AchievementSpecial by id.
     * @param id
     * @return AchievementSpecial
     */
    @Transactional
    public AchievementSpecial find(Long id) {
        return achievementSpecialDao.find(id);
    }


    /**
     * Create new AchievementSpecial.
     * @param achievement
     */
    @Transactional
    public void create(AchievementSpecial achievement) {
        achievementSpecialDao.persist(achievement);
    }


    /**
     * Update AchievementSpecial.
     * @param achievement
     */
    @Transactional
    public void update(AchievementSpecial achievement) {
        Objects.requireNonNull(achievement);
        achievementSpecialDao.update(achievement);
    }
}
