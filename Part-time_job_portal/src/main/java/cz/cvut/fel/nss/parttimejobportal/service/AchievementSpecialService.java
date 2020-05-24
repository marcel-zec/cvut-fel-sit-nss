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

    @Transactional
    public List<AchievementSpecial> findAll() {
        return achievementSpecialDao.findAll();
    }

    @Transactional
    public AchievementSpecial find(Long id) {
        return achievementSpecialDao.find(id);
    }

    @Transactional
    public void create(AchievementSpecial achievement) {
        achievementSpecialDao.persist(achievement);
    }

    @Transactional
    public void update(AchievementSpecial achievement) {
        Objects.requireNonNull(achievement);
        achievementSpecialDao.update(achievement);
    }
}
