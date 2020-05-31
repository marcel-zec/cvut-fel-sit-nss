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

    @Transactional
    public void addTrip(Long jobJournalId, Long tripId) {
        Objects.requireNonNull(jobJournalId);
        Objects.requireNonNull(tripId);

        JobJournal jobJournal = dao.find(jobJournalId);
        Offer trip = offerDao.find(tripId);
        Category category = categoryDao.find(trip.getCategory().getId());

        System.out.println("ADDING TRIP" + category.getName());

        jobJournal.addTrip(trip.getCategory().getId());
        dao.update(jobJournal);
        checkCategorizedAchievements(trip.getCategory(), jobJournal);
    }

    @Transactional
    public void addOwnedCategorizedAchievement(JobJournal jobJournal, AchievementCategorized achievementCategorized) {
        Objects.requireNonNull(achievementCategorized);
        Objects.requireNonNull(jobJournal);
        jobJournal.addEarnedAchievementCategorized(achievementCategorized);
        dao.update(jobJournal);
    }

    @Transactional
    public void addOwnedCertificates(JobJournal jobJournal, AchievementCertificate achievementCertificate) {
        Objects.requireNonNull(jobJournal);
        Objects.requireNonNull(achievementCertificate);
        jobJournal.addCertificate(achievementCertificate);
        dao.update(jobJournal);
    }

    @Transactional
    public void addOwnedSpecialAchievement(JobJournal jobJournal, AchievementSpecial achievementSpecial) {
        Objects.requireNonNull(jobJournal);
        Objects.requireNonNull(achievementSpecial);
        jobJournal.addEarnedAchievementSpecial(achievementSpecial);
        dao.update(jobJournal);
    }


    //this should be used after finalizing/closing the enrollment and adding new trip to hashmap in travel journal
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

    @Transactional
    public void addXP(Long jobJournalId,int actual_xp_reward) {
        JobJournal jobJournal = dao.find(jobJournalId);
        jobJournal.addsXp(actual_xp_reward);
        dao.update(jobJournal);
    }
}
