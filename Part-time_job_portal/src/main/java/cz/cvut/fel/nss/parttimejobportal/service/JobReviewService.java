package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.dao.*;
import cz.cvut.fel.nss.parttimejobportal.model.Enrollment;
import cz.cvut.fel.nss.parttimejobportal.model.Offer;
import cz.cvut.fel.nss.parttimejobportal.model.JobReview;
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
public class JobReviewService {

    private final JobReviewDao jobReviewDao;
    private final UserDao userDao;
    private final OfferDao offerDao;
    private final JobSessionDao jobSessionDao;
    private final EnrollmentDao enrollmentDao;

    public JobReviewService(JobReviewDao jobReviewDao, UserDao userDao, OfferDao offerDao, JobSessionDao jobSessionDao, EnrollmentDao enrollmentDao) {
        this.jobReviewDao = jobReviewDao;
        this.userDao = userDao;
        this.offerDao = offerDao;
        this.jobSessionDao = jobSessionDao;
        this.enrollmentDao = enrollmentDao;
    }


    /**
     * Get all JobReviews from database.
     * @return List<JobReview>
     */
    @Transactional
    public List<JobReview> findAll() {
        return jobReviewDao.findAll();
    }


    /**
     * Get JobReview by id.
     * @param id
     * @return JobReview
     */
    @Transactional
    public JobReview find(Long id) {
        return jobReviewDao.find(id);
    }


    /**
     * Create new JobReview to enrollment.
     * @param jobReview new JobReview
     * @param enrollmentId
     * @throws AlreadyExistsException if enrollment has alreadz job review
     * @throws UnauthorizedException if nobody is logged in
     * @throws NotFoundException if enrollment doesnt exist
     */
    @Transactional
    public void create(JobReview jobReview, Long enrollmentId) throws AlreadyExistsException, UnauthorizedException, NotFoundException {
        Objects.requireNonNull(jobReview);
        if (SecurityUtils.isAuthenticatedAnonymously()) throw new UnauthorizedException();

        Enrollment enrollment = enrollmentDao.find(enrollmentId);
        if (enrollment == null) throw new NotFoundException();
        if (enrollment.hasJobReview()) throw new AlreadyExistsException();

        jobReview.setTrip(enrollment.getTrip());
        jobReview.setAuthor(userDao.find(SecurityUtils.getCurrentUser().getId()));
        jobReview.setEnrollment(enrollment);
        jobReviewDao.persist(jobReview);

        Offer trip = enrollment.getTrip();
        long noReviews = trip.getJobReviews().size();
        double currentRating = trip.getRating();
        trip.setRating((currentRating*(noReviews-1) + jobReview.getRating())/noReviews);
        offerDao.update(trip);
    }


    /**
     * Update JobReview.
     * @param jobReview
     */
    @Transactional
    public void update(JobReview jobReview) {
        Objects.requireNonNull(jobReview);

        JobReview old = jobReviewDao.find(jobReview.getId());
        double oldRating = old.getRating();
        double newRating = jobReview.getRating();

        Offer trip = old.getTrip();
        double currentRating = trip.getRating();
        long noReviews = trip.getJobReviews().size();

        trip.setRating((currentRating*(noReviews) + newRating - oldRating)/noReviews);

        offerDao.update(trip);
        jobReviewDao.update(jobReview);
    }
}
