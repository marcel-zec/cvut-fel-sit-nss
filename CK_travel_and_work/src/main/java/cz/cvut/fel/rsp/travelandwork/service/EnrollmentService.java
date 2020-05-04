package cz.cvut.fel.rsp.travelandwork.service;

import cz.cvut.fel.rsp.travelandwork.dao.*;
import cz.cvut.fel.rsp.travelandwork.dto.AchievementSpecialDto;
import cz.cvut.fel.rsp.travelandwork.dto.EnrollmentDto;
import cz.cvut.fel.rsp.travelandwork.dto.RequestWrapperEnrollmentGet;
import cz.cvut.fel.rsp.travelandwork.exception.NotAllowedException;
import cz.cvut.fel.rsp.travelandwork.exception.NotFoundException;
import cz.cvut.fel.rsp.travelandwork.model.*;
import cz.cvut.fel.rsp.travelandwork.service.security.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentDao enrollmentDao;
    private final TranslateService translateService;
    private final AccessService accessService;
    private final UserDao userDao;
    private final AchievementSpecialDao achievementSpecialDao;

    @Autowired
    public EnrollmentService(EnrollmentDao enrollmentDao, TranslateService translateService, AccessService accessService, UserDao userDao, AchievementSpecialDao achievementSpecialDao) {
        this.enrollmentDao = enrollmentDao;
        this.translateService =  translateService;
        this.accessService = accessService;
        this.userDao = userDao;
        this.achievementSpecialDao = achievementSpecialDao;
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
        RequestWrapperEnrollmentGet wrapperEnrollmentGet = new RequestWrapperEnrollmentGet();
        if (findDto(enrollId).getState() != EnrollmentState.ACTIVE || findDto(enrollId).getTripSession().getTo_date().isBefore(ChronoLocalDate.from(LocalDateTime.now()))) throw new NotAllowedException();
        wrapperEnrollmentGet.setEnrollmentDto(translateService.translateEnrollment(find(enrollId)));
        wrapperEnrollmentGet.setOwner(translateService.translateUser(userDao.find(find(enrollId).getTravelJournal().getUser().getId())));
        return wrapperEnrollmentGet;
    }

    @Transactional
    public List<RequestWrapperEnrollmentGet> findAllActiveEndedWithUser(){
        List<RequestWrapperEnrollmentGet> requestWrappers = new ArrayList<>();

        for (Enrollment e: findAllActiveEnded()) {
            RequestWrapperEnrollmentGet wrapperEnrollmentGet = new RequestWrapperEnrollmentGet();
            wrapperEnrollmentGet.setEnrollmentDto(translateService.translateEnrollment(e));
            wrapperEnrollmentGet.setOwner(translateService.translateUser(e.getTravelJournal().getUser()));
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
                newEnrollments.add(e);
            }
        }
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
    public List<EnrollmentDto> findAllOfUser(User current_user) throws NotAllowedException {

        User user = accessService.getUser(current_user);
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
    public List<EnrollmentDto> findAllOfUserFinished(User current_user) throws NotAllowedException {
        List<EnrollmentDto> userEnrollments = findAllOfUser(current_user);
        List<EnrollmentDto> finished = new ArrayList<EnrollmentDto>();

        for (EnrollmentDto enrollmentDto : userEnrollments) {
            if (enrollmentDto.getState()== EnrollmentState.FINISHED) finished.add(enrollmentDto);
        }
        return finished;
    }

    @Transactional
    public List<EnrollmentDto> findAllOfUserActive(User current_user) throws NotAllowedException {
        List<EnrollmentDto> userEnrollments = findAllOfUser(current_user);
        List<EnrollmentDto> active_canceled = new ArrayList<EnrollmentDto>();

        for (EnrollmentDto enrollmentDto : userEnrollments) {
            if (enrollmentDto.getState()!= EnrollmentState.FINISHED) active_canceled.add(enrollmentDto);
        }
        return active_canceled;
    }

    @Transactional
    public List<EnrollmentDto> findAllOfUserFinished(Long id) throws NotFoundException, NotAllowedException {
        User user = userDao.find(id);
        if (user == null) throw new NotFoundException();
        return findAllOfUserFinished(user);
    }

    @Transactional
    public List<EnrollmentDto> findAllOfUserActive(Long id) throws NotFoundException, NotAllowedException {
        User user = userDao.find(id);
        if (user == null) throw new NotFoundException();
        return findAllOfUserActive(user);
    }

    @Transactional
    public void close(EnrollmentDto enrollmentDto){
        Enrollment enrollment = find(enrollmentDto.getId());
        enrollment.setState(EnrollmentState.FINISHED);
        enrollment.setActual_xp_reward(enrollmentDto.getActual_xp_reward());

        List<AchievementSpecial> achievementSpecials = new ArrayList<>();
        for (AchievementSpecialDto achievementSpecialDto : enrollmentDto.getRecieved_achievements_special()) {
            achievementSpecials.add(achievementSpecialDao.find(achievementSpecialDto.getId()));
        }

        enrollment.setRecieved_achievements_special(achievementSpecials);
        enrollmentDao.update(enrollment);
    }

    @Transactional
    public void closeOk(Long id){
        Enrollment enrollment = find(id);
        enrollment.setState(EnrollmentState.FINISHED);
        enrollment.setActual_xp_reward(enrollment.getTrip().getPossible_xp_reward());
        enrollment.setRecieved_achievements_special(enrollment.getTrip().getGain_achievements_special());
        enrollmentDao.update(enrollment);
    }
}
