package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.dao.*;
import cz.cvut.fel.nss.parttimejobportal.model.Enrollment;
import cz.cvut.fel.nss.parttimejobportal.model.Offer;
import cz.cvut.fel.nss.parttimejobportal.model.TripReview;
import cz.cvut.fel.nss.parttimejobportal.dao.*;
import cz.cvut.fel.nss.parttimejobportal.exception.AlreadyExistsException;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.exception.UnauthorizedException;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class TripReviewService {

    private final TripReviewDao tripReviewDao;
    private final UserDao userDao;
    private final TripDao tripDao;
    private final TripSessionDao tripSessionDao;
    private final EnrollmentDao enrollmentDao;

    public TripReviewService(TripReviewDao tripReviewDao, UserDao userDao, TripDao tripDao, TripSessionDao tripSessionDao, EnrollmentDao enrollmentDao) {
        this.tripReviewDao = tripReviewDao;
        this.userDao = userDao;
        this.tripDao = tripDao;
        this.tripSessionDao = tripSessionDao;
        this.enrollmentDao = enrollmentDao;
    }

    @Transactional
    public List<TripReview> findAll() {
        return tripReviewDao.findAll();
    }

    @Transactional
    public TripReview find(Long id) {
        return tripReviewDao.find(id);
    }

    @Transactional
    public void create(TripReview tripReview, Long enrollmentId) throws AlreadyExistsException, UnauthorizedException, NotFoundException {
        Objects.requireNonNull(tripReview);
        if (SecurityUtils.isAuthenticatedAnonymously()) throw new UnauthorizedException();

        Enrollment enrollment = enrollmentDao.find(enrollmentId);
        if (enrollment == null) throw new NotFoundException();
        if (enrollment.hasTripReview()) throw new AlreadyExistsException();

        tripReview.setTrip(enrollment.getTrip());
        tripReview.setAuthor(SecurityUtils.getCurrentUser());
        tripReview.setEnrollment(enrollment);
        tripReviewDao.persist(tripReview);

        Offer trip = enrollment.getTrip();
        long noReviews = trip.getTripReviews().size();
        double currentRating = trip.getRating();
        trip.setRating((currentRating*(noReviews-1) + tripReview.getRating())/noReviews);
        tripDao.update(trip);
    }

    @Transactional
    public void update(TripReview tripReview) {
        Objects.requireNonNull(tripReview);

        TripReview old = tripReviewDao.find(tripReview.getId());
        double oldRating = old.getRating();
        double newRating = tripReview.getRating();

        Offer trip = old.getTrip();
        double currentRating = trip.getRating();
        long noReviews = trip.getTripReviews().size();

        trip.setRating((currentRating*(noReviews) + newRating - oldRating)/noReviews);

        tripDao.update(trip);
        tripReviewDao.update(tripReview);
    }
}
