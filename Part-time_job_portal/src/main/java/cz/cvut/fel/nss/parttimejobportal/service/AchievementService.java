package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.dto.AchievementCertificateDto;
import cz.cvut.fel.nss.parttimejobportal.dto.AchievementDto;
import cz.cvut.fel.nss.parttimejobportal.dto.AchievementSpecialDto;
import cz.cvut.fel.nss.parttimejobportal.model.*;
import cz.cvut.fel.nss.parttimejobportal.dao.AchievementCategorizedDao;
import cz.cvut.fel.nss.parttimejobportal.dao.AchievementCertificateDao;
import cz.cvut.fel.nss.parttimejobportal.dao.AchievementSpecialDao;
import cz.cvut.fel.nss.parttimejobportal.dto.AchievementCategorizedDto;
import cz.cvut.fel.nss.parttimejobportal.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AchievementService {
    private final AchievementCertificateDao achievementCertificateDao;
    private final AchievementCategorizedDao achievementCategorizedDao;
    private final AchievementSpecialDao achievementSpecialDao;
    private final TranslateService translateService;
    private final AchievementCategorizedService achievementCategorizedService;
    private final AchievementSpecialService achievementSpecialService;
    private final AchievementCertificateService achievementCertificateService;


    @Autowired
    public AchievementService(AchievementCertificateDao achievementCertificateDao, AchievementCategorizedDao achievementCategorizedDao, AchievementSpecialDao achievementSpecialDao, TranslateService translateService, AchievementCategorizedService achievementCategorizedService, AchievementSpecialService achievementSpecialService, AchievementCertificateService achievementCertificateService) {

        this.achievementCertificateDao = achievementCertificateDao;
        this.achievementCategorizedDao = achievementCategorizedDao;
        this.achievementSpecialDao = achievementSpecialDao;
        this.translateService = translateService;
        this.achievementCategorizedService = achievementCategorizedService;
        this.achievementSpecialService = achievementSpecialService;
        this.achievementCertificateService = achievementCertificateService;
    }

    private List<Achievement> findAll(){
        List<Achievement> achievements = new ArrayList<>();
        achievements.addAll(achievementCategorizedDao.findAll());
        achievements.addAll(achievementCertificateDao.findAll());
        achievements.addAll(achievementSpecialDao.findAll());
        return achievements;
    }


    @Transactional
    public List<AchievementDto> findAllOfUser(User user){
        Objects.requireNonNull(user);

        List<AchievementDto> achievementDtos = new ArrayList<>();
        List<AchievementCertificateDto> achievementCertificates = new ArrayList<>();
        List<AchievementCategorizedDto> achievementCategorized = new ArrayList<>();
        List<AchievementSpecialDto> achievementSpecials = new ArrayList<>();

        for (AchievementCertificate certificate: user.getTravel_journal().getCertificates() ) {
           achievementCertificates.add(translateService.translateAchievementCertificate(certificate));
        }

        for (AchievementCategorized categorized: user.getTravel_journal().getEarnedAchievementsCategorized()) {
            achievementCategorized.add(translateService.translateAchievementCategorized(categorized));
        }

        for (AchievementSpecial special: user.getTravel_journal().getEarnedAchievementsSpecial() ) {
            achievementSpecials.add(translateService.translateAchievementSpecial(special));
        }

        achievementDtos.addAll(achievementCategorized);
        achievementDtos.addAll(achievementCertificates);
        achievementDtos.addAll(achievementSpecials);

        return achievementDtos;
    }
}
