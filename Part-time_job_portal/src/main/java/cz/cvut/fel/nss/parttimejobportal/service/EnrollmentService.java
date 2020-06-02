package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.dao.AchievementSpecialDao;
import cz.cvut.fel.nss.parttimejobportal.dao.EnrollmentDao;
import cz.cvut.fel.nss.parttimejobportal.dao.JobJournalDao;
import cz.cvut.fel.nss.parttimejobportal.dao.UserDao;
import cz.cvut.fel.nss.parttimejobportal.dto.AchievementSpecialDto;
import cz.cvut.fel.nss.parttimejobportal.dto.EnrollmentDto;
import cz.cvut.fel.nss.parttimejobportal.dto.RequestWrapperEnrollmentGet;
import cz.cvut.fel.nss.parttimejobportal.model.*;
import cz.cvut.fel.nss.parttimejobportal.exception.NotAllowedException;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityUtils;
import cz.cvut.fel.nss.parttimejobportal.service.security.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentDao enrollmentDao;
    private final TranslateService translateService;
    private final AccessService accessService;
    private final UserDao userDao;
    private final AchievementSpecialDao achievementSpecialDao;
    private final JobJournalService jobJournalService;

    @Autowired
    public EnrollmentService(EnrollmentDao enrollmentDao, TranslateService translateService, AccessService accessService, UserDao userDao, AchievementSpecialDao achievementSpecialDao, JobJournalService jobJournalService, JobJournalDao jobJournalDao) {
        this.enrollmentDao = enrollmentDao;
        this.translateService =  translateService;
        this.accessService = accessService;
        this.userDao = userDao;
        this.achievementSpecialDao = achievementSpecialDao;
        this.jobJournalService = jobJournalService;
    }

    private List<Enrollment> findAll(){
        return enrollmentDao.findAll();
    }

    @Transactional
    public List<EnrollmentDto> findAllDto(){
        List<EnrollmentDto> enrollmentDtos = new ArrayList<>();

        for (Enrollment e : enrollmentDao.findAll()) {
            enrollmentDtos.add(translateService.translateEnrollment(e));
        }

        return enrollmentDtos;
    }

    @Transactional
    public RequestWrapperEnrollmentGet findActiveEndedWithUser(Long enrollId) throws NotAllowedException {
        if (!find(enrollId).getTrip().getAuthor().getId().equals(SecurityUtils.getCurrentUser().getId())) throw new NotAllowedException("Not for you");
        RequestWrapperEnrollmentGet wrapperEnrollmentGet = new RequestWrapperEnrollmentGet();

        if (findDto(enrollId).getState() != EnrollmentState.ACTIVE || findDto(enrollId).getTripSession().getTo_date().isAfter(ChronoLocalDate.from(LocalDateTime.now()))) throw new NotAllowedException();
        wrapperEnrollmentGet.setEnrollmentDto(translateService.translateEnrollment(find(enrollId)));
        wrapperEnrollmentGet.setOwner(translateService.translateUser(userDao.find(find(enrollId).getJobJournal().getUser().getId())));
        return wrapperEnrollmentGet;
    }

    @Transactional
    public List<RequestWrapperEnrollmentGet> findAllActiveEndedWithUser(){
        List<RequestWrapperEnrollmentGet> requestWrappers = new ArrayList<>();

        for (Enrollment e: findAllActiveEnded()) {
            RequestWrapperEnrollmentGet wrapperEnrollmentGet = new RequestWrapperEnrollmentGet();
            wrapperEnrollmentGet.setEnrollmentDto(translateService.translateEnrollment(e));
            wrapperEnrollmentGet.setOwner(translateService.translateUser(e.getJobJournal().getUser()));
            requestWrappers.add(wrapperEnrollmentGet);
        }
        return requestWrappers;
    }

    @Transactional
    public List<Enrollment> findAllActiveEnded(){
        List<Enrollment> enrollments = findAll();
        List<Enrollment> newEnrollments = new ArrayList<>();

        for (Enrollment e: enrollments) {
            if (e.getState().equals(EnrollmentState.ACTIVE) && e.getTripSession().getTo_date().isBefore(ChronoLocalDate.from(LocalDateTime.now()))){
                if ((SecurityUtils.getCurrentUser().getRole() == Role.MANAGER && SecurityUtils.getCurrentUser().getId().equals(e.getTrip().getAuthor().getId())) || SecurityUtils.getCurrentUser().getRole() == Role.ADMIN){
                    newEnrollments.add(e);
                }
            }
        }
        Collections.sort(newEnrollments, new Comparator<Enrollment>() {
            @Override
            public int compare(Enrollment e1, Enrollment e2)
            {
                return  e1.getTripSession().getTo_date().compareTo(e2.getTripSession().getTo_date());
            }
        });
        return newEnrollments;
    }


    private Enrollment find(Long id){
        return enrollmentDao.find(id);
    }

    @Transactional
    public EnrollmentDto findDto(Long id){

       return translateService.translateEnrollment(enrollmentDao.find(id));
    }

    @Transactional
    public List<EnrollmentDto> findAllOfUser(AbstractUser current_user) throws NotAllowedException {

        User user = userDao.find(current_user.getId());
        if (user == null) throw new NotAllowedException();

        List<Enrollment> enrollments = user.getTravel_journal().getEnrollments();
        List<EnrollmentDto> enrollmentDtos = new ArrayList<EnrollmentDto>();

        if (enrollments != null && enrollments.size()>0){
            for (Enrollment e : enrollments) {
                enrollmentDtos.add(translateService.translateEnrollment(e));
            }
        }
        return enrollmentDtos;
    }

    @Transactional
    public List<EnrollmentDto> findAllOfUserFinished(AbstractUser current_user) throws NotAllowedException {
        List<EnrollmentDto> userEnrollments = findAllOfUser(current_user);
        List<EnrollmentDto> finished = new ArrayList<EnrollmentDto>();

        for (EnrollmentDto enrollmentDto : userEnrollments) {
            if (enrollmentDto.getState()== EnrollmentState.FINISHED) finished.add(enrollmentDto);
        }
        return finished;
    }

    @Transactional
    public List<EnrollmentDto> findAllOfUserActive(AbstractUser current_user) throws NotAllowedException {
        List<EnrollmentDto> userEnrollments = findAllOfUser(current_user);
        List<EnrollmentDto> active_canceled = new ArrayList<EnrollmentDto>();

        for (EnrollmentDto enrollmentDto : userEnrollments) {
            if (enrollmentDto.getState()!= EnrollmentState.FINISHED) active_canceled.add(enrollmentDto);
        }
        return active_canceled;
    }

    @Transactional
    public List<EnrollmentDto> findAllOfUserFinished(Long id) throws NotFoundException, NotAllowedException {
        AbstractUser user = userDao.find(id);
        if (user == null) throw new NotFoundException();
        return findAllOfUserFinished(user);
    }

    @Transactional
    public List<EnrollmentDto> findAllOfUserActive(Long id) throws NotFoundException, NotAllowedException {
        AbstractUser user = userDao.find(id);
        if (user == null) throw new NotFoundException();
        return findAllOfUserActive(user);
    }

    @Transactional
    public void setPayment(EnrollmentDto enrollmentDto) {
        Enrollment enrollment = find(enrollmentDto.getId());
        enrollment.setDeposit_was_paid(enrollmentDto.isDeposit_was_paid());

        enrollmentDao.persist(enrollment);
    }

    @Transactional
    public void close(EnrollmentDto enrollmentDto) throws NotAllowedException {
        Enrollment enrollment = find(enrollmentDto.getId());
        if (!enrollment.getTrip().getAuthor().getId().equals(SecurityUtils.getCurrentUser().getId())) throw new NotAllowedException("Not for you");

        enrollment.setState(EnrollmentState.FINISHED);
        enrollment.setDeposit_was_paid(true);
        enrollment.setActual_xp_reward(enrollmentDto.getActual_xp_reward());

        List<AchievementSpecial> achievementSpecials = new ArrayList<>();
        for (AchievementSpecialDto achievementSpecialDto : enrollmentDto.getRecieved_achievements_special()) {
            achievementSpecials.add(achievementSpecialDao.find(achievementSpecialDto.getId()));
        }

        enrollment.setRecieved_achievements_special(achievementSpecials);
        enrollmentDao.update(enrollment);

        jobJournalService.addXP(enrollment.getJobJournal().getId(), enrollment.getActual_xp_reward());
        jobJournalService.addTrip(enrollment.getJobJournal().getId(), enrollment.getTrip().getId());
        for(AchievementSpecial as : enrollment.getRecieved_achievements()) {
            jobJournalService.addOwnedSpecialAchievement(enrollment.getJobJournal(), as);
        }
    }

    @Transactional
    public void closeOk(Long id) throws NotAllowedException, NotFoundException {
        Enrollment enrollment = find(id);
        if (enrollment == null) throw new NotFoundException();
        if (!enrollment.getTrip().getAuthor().getId().equals(SecurityUtils.getCurrentUser().getId())) throw new NotAllowedException("Not for you");

        List<AchievementSpecial> achievementSpecials = enrollment.getTrip().getGain_achievements_special();
        enrollment.setState(EnrollmentState.FINISHED);
        enrollment.setDeposit_was_paid(true);
        enrollment.setActual_xp_reward(enrollment.getTrip().getPossible_xp_reward());
        enrollment.setRecieved_achievements_special(new ArrayList());
        enrollment.getRecieved_achievements().addAll(achievementSpecials);
       // enrollment.setRecieved_achievements_special(achievementSpecials);

        enrollmentDao.update(enrollment);
        jobJournalService.addXP(enrollment.getJobJournal().getId(), enrollment.getActual_xp_reward());
        jobJournalService.addTrip(enrollment.getJobJournal().getId(), enrollment.getTrip().getId());
        for(AchievementSpecial as : enrollment.getRecieved_achievements()) {
            jobJournalService.addOwnedSpecialAchievement(enrollment.getJobJournal(), as);
        }
    }

    @Transactional
    public void cancel(Long id) {
        Enrollment enrollment = find(id);
        enrollment.setState(EnrollmentState.CANCELED);
        enrollmentDao.update(enrollment);
    }

    @Transactional
    public void changePaymnet(Long id) {
        Enrollment enrollment = find(id);
        enrollment.setDeposit_was_paid(!enrollment.isDeposit_was_paid());
        enrollmentDao.update(enrollment);
    }
}
