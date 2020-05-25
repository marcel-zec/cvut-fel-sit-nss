package cz.cvut.fel.nss.parttimejobportal.service;


import cz.cvut.fel.nss.parttimejobportal.dto.UserReviewDto;
import cz.cvut.fel.nss.parttimejobportal.model.Enrollment;
import cz.cvut.fel.nss.parttimejobportal.model.JobSession;
import cz.cvut.fel.nss.parttimejobportal.model.User;
import cz.cvut.fel.nss.parttimejobportal.model.UserReview;
import cz.cvut.fel.nss.parttimejobportal.dao.EnrollmentDao;
import cz.cvut.fel.nss.parttimejobportal.dao.TripSessionDao;
import cz.cvut.fel.nss.parttimejobportal.dao.UserDao;
import cz.cvut.fel.nss.parttimejobportal.dao.UserReviewDao;
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
    private final TripSessionDao tripSessionDao;
    private final EnrollmentDao enrollmentDao;


    @Autowired
    public UserReviewService(TranslateService translateService, UserReviewDao userReviewDao, UserDao userDao, TripSessionDao tripSessionDao, EnrollmentDao enrollmentDao) {

        this.translateService = translateService;
        this.userReviewDao = userReviewDao;
        this.userDao = userDao;
        this.tripSessionDao = tripSessionDao;
        this.enrollmentDao = enrollmentDao;
    }

    @Transactional
    public UserReviewDto find(Long id) {
        Objects.requireNonNull(id);
        return translateService.translateUserReview(userReviewDao.find(id));
    }

    @Transactional
    public List<UserReviewDto> findAll() {
        List<UserReviewDto> userReviewDtos = new ArrayList<>();

        for (UserReview userReview : userReviewDao.findAll()) {
            userReviewDtos.add(translateService.translateUserReview(userReview));
        }
        return userReviewDtos;
    }

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

    @Transactional
    public List<UserReviewDto> findAllOfUser() throws UnauthorizedException, NotFoundException {
        if (SecurityUtils.isAuthenticatedAnonymously()) throw new UnauthorizedException();

        User user = userDao.find(SecurityUtils.getCurrentUser().getId());

        List<UserReviewDto> userReviewDtos = new ArrayList<>();
        if (user.getUserReviews() == null) throw new NotFoundException();

        for (UserReview userReview : user.getUserReviews()) {
            userReviewDtos.add(translateService.translateUserReview(userReview));
        }
        return userReviewDtos;
    }

    @Transactional
    public void create(long enrollmentId, User currentUser, Long tripSessionId, UserReview userReview) throws Exception {

        Enrollment enrollment = enrollmentDao.find(enrollmentId);
        User user = enrollment.getTravelJournal().getUser();
        User current_user = userDao.find(currentUser.getId());
        JobSession tripSession = tripSessionDao.find(tripSessionId);

        if (user == null || tripSession==null) throw new NotFoundException();

        userReview.setUser(user);
        userReview.setAuthor(current_user);
        userReview.setTripSession(tripSession);
        userReviewDao.persist(userReview);
    }

    @Transactional
    public void create(long enrollmentId, User currentUser) throws Exception {

        Enrollment enrollment = enrollmentDao.find(enrollmentId);
        if (enrollment == null) throw new NotFoundException();

        User user = enrollment.getTravelJournal().getUser();
        User current_user = userDao.find(currentUser.getId());
        JobSession tripSession = tripSessionDao.find(enrollment.getTripSession().getId());
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
