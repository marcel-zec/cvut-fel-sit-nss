package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.model.*;
import cz.cvut.fel.nss.parttimejobportal.dao.CategoryDao;
import cz.cvut.fel.nss.parttimejobportal.dao.TravelJournalDao;
import cz.cvut.fel.nss.parttimejobportal.dao.TripDao;
import cz.cvut.fel.nss.parttimejobportal.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class TravelJournalService {
    private final TravelJournalDao dao;
    private final TripDao tripDao;
    private final CategoryDao categoryDao;
    private final AchievementCategorizedService achievementCategorizedService;

    @Autowired
    public TravelJournalService(TravelJournalDao dao, TripDao tripDao, CategoryDao categoryDao, AchievementCategorizedService achievementCategorizedService) {
        this.dao = dao;
        this.tripDao = tripDao;
        this.categoryDao = categoryDao;
        this.achievementCategorizedService = achievementCategorizedService;
    }

    @Transactional
    public void addTrip(Long travelJournalId, Long tripId) {
        Objects.requireNonNull(travelJournalId);
        Objects.requireNonNull(tripId);

        TravelJournal travelJournal = dao.find(travelJournalId);
        Offer trip = tripDao.find(tripId);
        Category category = categoryDao.find(trip.getCategory().getId());

        System.out.println("ADDING TRIP" + category.getName());

        travelJournal.addTrip(trip.getCategory().getId());
        dao.update(travelJournal);
        checkCategorizedAchievements(trip.getCategory(), travelJournal);
    }

    @Transactional
    public void addOwnedCategorizedAchievement(TravelJournal travelJournal, AchievementCategorized achievementCategorized) {
        Objects.requireNonNull(achievementCategorized);
        Objects.requireNonNull(travelJournal);
        travelJournal.addEarnedAchievementCategorized(achievementCategorized);
        dao.update(travelJournal);
    }

    @Transactional
    public void addOwnedCertificates(TravelJournal travelJournal, AchievementCertificate achievementCertificate) {
        Objects.requireNonNull(travelJournal);
        Objects.requireNonNull(achievementCertificate);
        travelJournal.addCertificate(achievementCertificate);
        dao.update(travelJournal);
    }

    @Transactional
    public void addOwnedSpecialAchievement(TravelJournal travelJournal, AchievementSpecial achievementSpecial) {
        Objects.requireNonNull(travelJournal);
        Objects.requireNonNull(achievementSpecial);
        travelJournal.addEarnedAchievementSpecial(achievementSpecial);
        dao.update(travelJournal);
    }


    //this should be used after finalizing/closing the enrollment and adding new trip to hashmap in travel journal
    @Transactional
    public void checkCategorizedAchievements(Category category, TravelJournal currentTravelJournal) {
        int numberOfTripsInCat = currentTravelJournal.findAndGetCategoryValueIfExists(category.getId());
        List<AchievementCategorized> categorizedAchievements = achievementCategorizedService.findAllInCategory(category);
        List<AchievementCategorized> ownedAchievements = currentTravelJournal.getEarnedAchievementsCategorized();

        if((numberOfTripsInCat == -1) || (categorizedAchievements == null)) return;

        for(AchievementCategorized cA : categorizedAchievements) {
            if((cA.getLimit() <= numberOfTripsInCat) && (!ownedAchievements.contains(cA))) {
                currentTravelJournal.addEarnedAchievementCategorized(cA);
                cA.addTravelJournal(currentTravelJournal);
                achievementCategorizedService.update(cA);
            }
        }

        dao.update(currentTravelJournal);
    }

    @Transactional
    public void addXP(Long travelJournalId,int actual_xp_reward) {
        TravelJournal travelJournal = dao.find(travelJournalId);
        travelJournal.addsXp(actual_xp_reward);
        dao.update(travelJournal);
    }
}
