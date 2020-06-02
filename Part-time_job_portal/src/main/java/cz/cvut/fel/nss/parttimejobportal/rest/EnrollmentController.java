package cz.cvut.fel.nss.parttimejobportal.rest;

import cz.cvut.fel.nss.parttimejobportal.dto.EnrollmentDto;
import cz.cvut.fel.nss.parttimejobportal.dto.RequestWrapperEnrollment;
import cz.cvut.fel.nss.parttimejobportal.dto.RequestWrapperEnrollmentGet;
import cz.cvut.fel.nss.parttimejobportal.exception.NotAllowedException;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityConstants;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityUtils;
import cz.cvut.fel.nss.parttimejobportal.service.EnrollmentService;
import cz.cvut.fel.nss.parttimejobportal.service.UserReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = SecurityConstants.ORIGIN_URI)
@RequestMapping("/enrollment")
public class EnrollmentController {

    private static final Logger LOG = LoggerFactory.getLogger(EnrollmentController.class);
    private final EnrollmentService enrollmentService;
    private final UserReviewService userReviewService;


    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService, UserReviewService userReviewService) {
        this.enrollmentService = enrollmentService;
        this.userReviewService = userReviewService;
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EnrollmentDto get(@PathVariable Long id)  {
        return enrollmentService.findDto(id);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EnrollmentDto> getAllOfUserFinished() throws NotAllowedException {
        return enrollmentService.findAllOfUserFinished(SecurityUtils.getCurrentUser());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/active", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EnrollmentDto> getAllOfUserActiveAndCancel() throws NotAllowedException {
        return enrollmentService.findAllOfUserActive(SecurityUtils.getCurrentUser());
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping(value = "/complete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EnrollmentDto> getAllOfUserFinishedAdmin(@PathVariable Long id) throws NotAllowedException, NotFoundException {
        return enrollmentService.findAllOfUserFinished(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping(value = "/active/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EnrollmentDto> getAllOfUserActiveAndCancelAdmin(@PathVariable Long id) throws NotAllowedException, NotFoundException {
        return enrollmentService.findAllOfUserActive(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping(value = "/close", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RequestWrapperEnrollmentGet> getAllActiveEnded() {
        return enrollmentService.findAllActiveEndedWithUser();
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping(value = "/setPayment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> setPayment(@RequestBody RequestWrapperEnrollment requestWrapperEnrollment) throws  Exception {
        enrollmentService.setPayment(requestWrapperEnrollment.getEnrollmentDto());
        return null;
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PatchMapping(value = "/close", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> close(@RequestBody RequestWrapperEnrollment requestWrapperEnrollment) throws Exception {
        enrollmentService.close(requestWrapperEnrollment.getEnrollmentDto());
        userReviewService.create(requestWrapperEnrollment.getEnrollmentDto().getId(), SecurityUtils.getCurrentUser(),
                requestWrapperEnrollment.getTripSessionId(), requestWrapperEnrollment.getUserReview() );
        //LOG.debug("User {} successfully registered.", user);
        //return new ResponseEntity<>(headers, HttpStatus.CREATED);
        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping(value = "/close/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RequestWrapperEnrollmentGet getWithUser(@PathVariable Long id) throws NotAllowedException {
        return enrollmentService.findActiveEndedWithUser(id);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping(value = "/close/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void closeOk(@PathVariable Long id) throws Exception {
        enrollmentService.closeOk(id);
        userReviewService.create(id,SecurityUtils.getCurrentUser());
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping(value = "cancel/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void cancel(@PathVariable Long id) throws Exception {
        enrollmentService.cancel(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping(value = "changePayment/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void changePayment(@PathVariable Long id) throws Exception {
        enrollmentService.changePaymnet(id);
    }
}
