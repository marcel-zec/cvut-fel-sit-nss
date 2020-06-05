package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.model.AchievementCertificate;
import cz.cvut.fel.nss.parttimejobportal.dao.AchievementCertificateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class AchievementCertificateService {
    private final AchievementCertificateDao achievementCertificateDao;

    @Autowired
    public AchievementCertificateService(AchievementCertificateDao achievementCertificateDao) {
        this.achievementCertificateDao = achievementCertificateDao;
    }


    /**
     * Get all AchievementCertificate from database.
     * @return List<AchievementCertificate>
     */
    @Transactional
    public List<AchievementCertificate> findAll() {
        return achievementCertificateDao.findAll();
    }


    /**
     *  Get AchievementCertificate by id.
     * @param id
     * @return AchievementCertificate
     */
    @Transactional
    public AchievementCertificate find(Long id) {
        return achievementCertificateDao.find(id);
    }


    /**
     * Create new AchievementCertificate.
     * @param achievement
     */
    @Transactional
    public void create(AchievementCertificate achievement) {
        achievementCertificateDao.persist(achievement);
    }


    /**
     * Update AchievementCertificate.
     * @param achievement
     */
    @Transactional
    public void update(AchievementCertificate achievement) {
        Objects.requireNonNull(achievement);
        achievementCertificateDao.update(achievement);
    }
}
