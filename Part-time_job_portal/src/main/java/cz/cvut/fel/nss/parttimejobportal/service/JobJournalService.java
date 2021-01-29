package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.model.*;
import cz.cvut.fel.nss.parttimejobportal.dao.CategoryDao;
import cz.cvut.fel.nss.parttimejobportal.dao.JobJournalDao;
import cz.cvut.fel.nss.parttimejobportal.dao.OfferDao;
import cz.cvut.fel.nss.parttimejobportal.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class JobJournalService {
    private final JobJournalDao dao;
    private final OfferDao offerDao;
    private final CategoryDao categoryDao;
    private final AchievementCategorizedService achievementCategorizedService;

    @Autowired
    public JobJournalService(JobJournalDao dao, OfferDao offerDao, CategoryDao categoryDao, AchievementCategorizedService achievementCategorizedService) {
        this.dao = dao;
        this.offerDao = offerDao;
        this.categoryDao = categoryDao;
        this.achievementCategorizedService = achievementCategorizedService;
    }


    /**
     * Add offer to jobJournal.
     * @param jobJournalId
     * @param offerId
     */
    @Transactional
    public void addTrip(Long jobJournalId, Long offerId) {
        Objects.requireNonNull(jobJournalId);
        Objects.requireNonNull(offerId);

        JobJournal jobJournal = dao.find(jobJournalId);
        Offer offer = offerDao.find(offerId);
        Category category = categoryDao.find(offer.getCategory().getId());

        System.out.println("ADDING OFFER" + category.getName());

        jobJournal.addTrip(offer.getCategory().getId());
        dao.update(jobJournal);
        checkCategorizedAchievements(offer.getCategory(), jobJournal);
    }


    /**
     * Add achievementsCategorized to jobJournal.
     * @param jobJournal
     * @param achievementCategorized
     */
    @Transactional
    public void addOwnedCategorizedAchievement(JobJournal jobJournal, AchievementCategorized achievementCategorized) {
        Objects.requireNonNull(achievementCategorized);
        Objects.requireNonNull(jobJournal);
        jobJournal.addEarnedAchievementCategorized(achievementCategorized);
        dao.update(jobJournal);
    }


    /**
     * Add achievementsCertificate to jobJournal.
     * @param jobJournal
     * @param achievementCertificate
     */
    @Transactional
    public void addOwnedCertificates(JobJournal jobJournal, AchievementCertificate achievementCertificate) {
        Objects.requireNonNull(jobJournal);
        Objects.requireNonNull(achievementCertificate);
        jobJournal.addCertificate(achievementCertificate);
        dao.update(jobJournal);
    }


    /**
     * Add achievementsSpecial to jobJournal.
     * @param jobJournal
     * @param achievementSpecial
     */
    @Transactional
    public void addOwnedSpecialAchievement(JobJournal jobJournal, AchievementSpecial achievementSpecial) {
        Objects.requireNonNull(jobJournal);
        Objects.requireNonNull(achievementSpecial);
        jobJournal.addEarnedAchievementSpecial(achievementSpecial);
        dao.update(jobJournal);
    }

    /**
     * It is used after finalizing/closing the enrollment and adding new offer to hashmap in job journal
     * @param category
     * @param currentJobJournal
     */
    @Transactional
    public void checkCategorizedAchievements(Category category, JobJournal currentJobJournal) {
        int numberOfTripsInCat = currentJobJournal.findAndGetCategoryValueIfExists(category.getId());
        List<AchievementCategorized> categorizedAchievements = achievementCategorizedService.findAllInCategory(category);
        List<AchievementCategorized> ownedAchievements = currentJobJournal.getEarnedAchievementsCategorized();

        if((numberOfTripsInCat == -1) || (categorizedAchievements == null)) return;

        for(AchievementCategorized cA : categorizedAchievements) {
            if((cA.getLimit() <= numberOfTripsInCat) && (!ownedAchievements.contains(cA))) {
                currentJobJournal.addEarnedAchievementCategorized(cA);
                cA.addJobJournal(currentJobJournal);
                achievementCategorizedService.update(cA);
            }
        }

        dao.update(currentJobJournal);
    }


    /**
     * Add xp to users job Journal.
     * @param jobJournalId
     * @param actual_xp_reward
     */
    @Transactional
    public void addXP(Long jobJournalId,int actual_xp_reward) {
        JobJournal jobJournal = dao.find(jobJournalId);
        jobJournal.addsXp(actual_xp_reward);
        dao.update(jobJournal);
    }
}
