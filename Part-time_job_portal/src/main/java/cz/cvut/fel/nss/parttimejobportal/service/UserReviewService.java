package cz.cvut.fel.nss.parttimejobportal.service;


import cz.cvut.fel.nss.parttimejobportal.dao.*;
import cz.cvut.fel.nss.parttimejobportal.dto.UserReviewDto;
import cz.cvut.fel.nss.parttimejobportal.exception.NotAllowedException;
import cz.cvut.fel.nss.parttimejobportal.model.*;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.exception.UnauthorizedException;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserReviewService {

    private final TranslateService translateService;
    private final UserReviewDao userReviewDao;
    private final UserDao userDao;
    private final JobSessionDao jobSessionDao;
    private final EnrollmentDao enrollmentDao;
    private final ManagerDao managerDao;


    @Autowired
    public UserReviewService(TranslateService translateService, UserReviewDao userReviewDao, UserDao userDao, JobSessionDao jobSessionDao, EnrollmentDao enrollmentDao, ManagerDao managerDao) {

        this.translateService = translateService;
        this.userReviewDao = userReviewDao;
        this.userDao = userDao;
        this.jobSessionDao = jobSessionDao;
        this.enrollmentDao = enrollmentDao;
        this.managerDao = managerDao;
    }


    /**
     * Get UserReview by id.
     * @param id
     * @return UserReviewDto
     */
    @Transactional
    public UserReviewDto find(Long id) {
        Objects.requireNonNull(id);
        return translateService.translateUserReview(userReviewDao.find(id));
    }


    /**
     * Get all UserReviews.
     * @return List<UserReviewDto>
     */
    @Transactional
    public List<UserReviewDto> findAll() {
        List<UserReviewDto> userReviewDtos = new ArrayList<>();

        for (UserReview userReview : userReviewDao.findAll()) {
            userReviewDtos.add(translateService.translateUserReview(userReview));
        }
        return userReviewDtos;
    }


    /**
     * Get all UserReviews which own user by his id.
     * @param userId
     * @return  List<UserReviewDto>
     * @throws NotFoundException if user doesnt exist.
     */
    @Transactional
    public List<UserReviewDto> findAllOfUser(Long userId) throws NotFoundException {
        User user = userDao.find(userId);
        if (user == null) throw new NotFoundException();
        if (user.getUserReviews() == null) throw new NotFoundException();

        List<UserReviewDto> userReviewDtos = new ArrayList<>();

        for (UserReview userReview : user.getUserReviews()) {
            userReviewDtos.add(translateService.translateUserReview(userReview));
        }
        return userReviewDtos;
    }


    /**
     * Get all UserReviews which own current logged in user.
     * @return List<UserReviewDto>
     * @throws UnauthorizedException
     * @throws NotFoundException
     */
    @Transactional
    public List<UserReviewDto> findAllOfUser() throws UnauthorizedException, NotFoundException {
        if (SecurityUtils.isAuthenticatedAnonymously()) throw new UnauthorizedException();

        User user = userDao.find(SecurityUtils.getCurrentUser().getId());
        if (user == null) throw new UnauthorizedException();

        List<UserReviewDto> userReviewDtos = new ArrayList<>();
        if (user.getUserReviews() == null) throw new NotFoundException();

        for (UserReview userReview : user.getUserReviews()) {
            userReviewDtos.add(translateService.translateUserReview(userReview));
        }
        return userReviewDtos;
    }


    /**
     * Create new UserReview from currentUser manager.
     * @param enrollmentId enrollment of user whom review I create.
     * @param currentUser
     * @param tripSessionId offerSession id where was user.
     * @param userReview
     * @throws Exception
     */
    @Transactional
    public void create(long enrollmentId, AbstractUser currentUser, Long tripSessionId, UserReview userReview) throws Exception {

        Enrollment enrollment = enrollmentDao.find(enrollmentId);
        if (enrollment == null) throw new NotFoundException();
        if (!enrollment.getTrip().getAuthor().getId().equals(SecurityUtils.getCurrentUser().getId())) throw new NotAllowedException("Not for you");

        User user = enrollment.getJobJournal().getUser();
        Manager current_user = managerDao.find(currentUser.getId());
        JobSession tripSession = jobSessionDao.find(tripSessionId);

        if (user == null || tripSession==null) throw new NotFoundException();

        userReview.setUser(user);
        userReview.setAuthor(current_user);
        userReview.setTripSession(tripSession);
        userReviewDao.persist(userReview);
    }


    /**
     * Create new UserReview from currentUser manager of full rating.
     * @param enrollmentId enrollment of user whom review I create.
     * @param currentUser
     * @throws Exception
     */
    @Transactional
    public void create(long enrollmentId, AbstractUser currentUser) throws Exception {

        Enrollment enrollment = enrollmentDao.find(enrollmentId);
        if (enrollment == null) throw new NotFoundException();
        if (!enrollment.getTrip().getAuthor().getId().equals(SecurityUtils.getCurrentUser().getId())) throw new NotAllowedException("Not for you");


        User user = enrollment.getJobJournal().getUser();
        Manager current_user = managerDao.find(currentUser.getId());
        JobSession tripSession = jobSessionDao.find(enrollment.getTripSession().getId());
        UserReview userReview = new UserReview();

        if (user == null || tripSession == null) throw new NotFoundException();

        userReview.setRating(5);
        userReview.setNote(null);
        userReview.setUser(user);
        userReview.setAuthor(current_user);
        userReview.setTripSession(tripSession);
        userReviewDao.persist(userReview);
    }

}
