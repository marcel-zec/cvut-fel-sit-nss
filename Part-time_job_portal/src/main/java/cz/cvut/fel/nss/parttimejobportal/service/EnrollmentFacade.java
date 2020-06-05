package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.dto.EnrollmentDto;
import cz.cvut.fel.nss.parttimejobportal.dto.RequestWrapperEnrollment;
import cz.cvut.fel.nss.parttimejobportal.dto.RequestWrapperEnrollmentGet;
import cz.cvut.fel.nss.parttimejobportal.exception.NotAllowedException;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public class EnrollmentFacade {
    private final EnrollmentService enrollmentService;
    private final UserReviewService userReviewService;

    @Autowired
    public EnrollmentFacade(EnrollmentService enrollmentService, UserReviewService userReviewService) {
        this.enrollmentService = enrollmentService;
        this.userReviewService = userReviewService;
    }

    public void close(RequestWrapperEnrollment requestWrapperEnrollment) throws Exception {
        enrollmentService.close(requestWrapperEnrollment.getEnrollmentDto());
        userReviewService.create(requestWrapperEnrollment.getEnrollmentDto().getId(), SecurityUtils.getCurrentUser(),requestWrapperEnrollment.getTripSessionId(),requestWrapperEnrollment.getUserReview());
    }

    public void closeFull(Long id) throws Exception {
        enrollmentService.closeOk(id);
        userReviewService.create(id,SecurityUtils.getCurrentUser());
    }

    public List<RequestWrapperEnrollmentGet> getAllWithUserToClose(){
        return enrollmentService.findAllActiveEndedWithUser();
    }

    public void cancel(Long id){
        enrollmentService.cancel(id);
    }

    public RequestWrapperEnrollmentGet getWithUserToClose(Long id) throws NotAllowedException {
        return enrollmentService.findActiveEndedWithUser(id);
    }

    public List<EnrollmentDto> getAllFinished(Long id) throws NotFoundException, NotAllowedException {
        return enrollmentService.findAllOfUserFinished(id);
    }

    public List<EnrollmentDto>  getAllFinished() throws NotAllowedException {
        return enrollmentService.findAllOfUserFinished(SecurityUtils.getCurrentUser());
    }

    public List<EnrollmentDto> getAllActiveAndCancel(Long id) throws NotFoundException, NotAllowedException {
        return enrollmentService.findAllOfUserActive(id);
    }

    public List<EnrollmentDto> getAllActiveAndCancel() throws NotAllowedException {
        return enrollmentService.findAllOfUserActive(SecurityUtils.getCurrentUser());
    }

    public EnrollmentDto get(Long id){
        return enrollmentService.findDto(id);
    }




}
