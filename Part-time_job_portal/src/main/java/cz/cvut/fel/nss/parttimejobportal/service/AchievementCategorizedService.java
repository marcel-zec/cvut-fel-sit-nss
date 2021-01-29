package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.model.AchievementCategorized;
import cz.cvut.fel.nss.parttimejobportal.model.Category;
import cz.cvut.fel.nss.parttimejobportal.dao.AchievementCategorizedDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AchievementCategorizedService {
    private final AchievementCategorizedDao achievementCategorizedDao;

    @Autowired
    public AchievementCategorizedService(AchievementCategorizedDao achievementCategorizedDao) {
        this.achievementCategorizedDao = achievementCategorizedDao;
    }


    /**
     * Get all AchievementCategorized from database.
     * @return  List<AchievementCategorized>
     */

    @Transactional
    public List<AchievementCategorized> findAll() {
        return achievementCategorizedDao.findAll();
    }


    /**
     * Get all AchievementCategorized by category.
     * @param category
     * @return  List<AchievementCategorized>
     */
    @Transactional
    public List<AchievementCategorized> findAllInCategory(Category category) {
        List<AchievementCategorized> all = achievementCategorizedDao.findAll();
        List<AchievementCategorized> result = new ArrayList<AchievementCategorized>();
        for(AchievementCategorized a : all) {
            if(a.getCategory().equals(category)) {
                result.add(a);
            }
        }

        if(result.isEmpty()) {
            result = null;
        }

        return result;
    }


    /**
     * Get AchievementCategorized by id.
     * @param id
     * @return AchievementCategorized
     */
    @Transactional
    public AchievementCategorized find(Long id) {
        return achievementCategorizedDao.find(id);
    }


    /**
     * Create new AchievementCategorized.
     * @param achievement
     */
    @Transactional
    public void create(AchievementCategorized achievement) {
        achievementCategorizedDao.persist(achievement);
    }


    /**
     * Update AchievementCategorized.
     * @param achievement
     */
    @Transactional
    public void update(AchievementCategorized achievement) {
        Objects.requireNonNull(achievement);
        achievementCategorizedDao.update(achievement);
    }
}
