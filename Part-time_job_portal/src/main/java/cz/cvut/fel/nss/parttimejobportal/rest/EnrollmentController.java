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
import org.springframework.http.HttpStatus;
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

    /**
     * Method find and return enrollent by id. Available only for user with ADMIN or MANAGER role.
     * @param id
     * @return response with EnrollmentDto
     */
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EnrollmentDto> get(@PathVariable Long id)  {
        return ResponseEntity.status(HttpStatus.OK).body(enrollmentService.findDto(id));
    }

    /**
     * Method find all completed enrollments of current user. Available only for user with USER role.
     * @return response with list of EnrollmentDto
     * @throws NotAllowedException
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EnrollmentDto>> getAllOfUserFinished() throws NotAllowedException {
        return ResponseEntity.status(HttpStatus.OK).body(enrollmentService.findAllOfUserFinished(SecurityUtils.getCurrentUser()));
    }

    /**
     * Method find all active enrollments of current user. Available only for user with USER role.
     * @return response with list of EnrollmentDto
     * @throws NotAllowedException
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/active", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EnrollmentDto>> getAllOfUserActiveAndCancel() throws NotAllowedException {
        return ResponseEntity.status(HttpStatus.OK).body(enrollmentService.findAllOfUserActive(SecurityUtils.getCurrentUser()));
    }

    /**
     * Method find all completed enrollments of user by user id. Available only for user with MANAGER of ADMIN role.
     * @return response with list of EnrollmentDto
     * @throws NotAllowedException
     * @throws NotFoundException
     */
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping(value = "/complete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EnrollmentDto>> getAllOfUserFinishedAdmin(@PathVariable Long id) throws NotAllowedException, NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(enrollmentService.findAllOfUserFinished(id));
    }

    /**
     * Method find all active enrollments of user by user id. Available only for user with MANAGER of ADMIN role.
     * @return response with list of EnrollmentDto
     * @throws NotAllowedException
     * @throws NotFoundException
     */
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping(value = "/active/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EnrollmentDto>> getAllOfUserActiveAndCancelAdmin(@PathVariable Long id) throws NotAllowedException, NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(enrollmentService.findAllOfUserActive(id));
    }

    /**
     * Method find all enrollments and theirs owner after end_date that should be finished. Available only for user with MANAGER of ADMIN role.
     * @return response with list of RequestWrapper (with EnrollmentDto and UserDto)
     */
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping(value = "/close", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RequestWrapperEnrollmentGet>> getAllActiveEnded() {
        return ResponseEntity.status(HttpStatus.OK).body(enrollmentService.findAllActiveEndedWithUser());
    }


    /**
     * Close concrete enrollment with edited reward and make user review. Available only for user with MANAGER role.
     * @param requestWrapperEnrollment - contains EnrollmentDto and UserDto
     * @return response 204
     * @throws Exception
     */
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PatchMapping(value = "/close", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> close(@RequestBody RequestWrapperEnrollment requestWrapperEnrollment) throws Exception {
        enrollmentService.close(requestWrapperEnrollment.getEnrollmentDto());
        userReviewService.create(requestWrapperEnrollment.getEnrollmentDto().getId(), SecurityUtils.getCurrentUser(),
                requestWrapperEnrollment.getTripSessionId(), requestWrapperEnrollment.getUserReview() );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Method find enrollment by id and its owner after end_date that should be finished. Available only for user with MANAGER of ADMIN role.
     * @return response with list of RequestWrapper (with EnrollmentDto and UserDto)
     * @throws NotAllowedException
     */
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping(value = "/close/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RequestWrapperEnrollmentGet> getWithUser(@PathVariable Long id) throws NotAllowedException {
        return ResponseEntity.status(HttpStatus.OK).body(enrollmentService.findActiveEndedWithUser(id));
    }

    /**
     * Close concrete enrollment by id with full reward. Available only for user with MANAGER role.
     * @return response 204
     * @throws Exception
     */
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping(value = "/close/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> closeOk(@PathVariable Long id) throws Exception {
        enrollmentService.closeOk(id);
        userReviewService.create(id,SecurityUtils.getCurrentUser());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Cancel enrollemt by id.
     * @param id
     * @return response 204
     * @throws Exception
     */
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping(value = "cancel/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> cancel(@PathVariable Long id) throws Exception {
        enrollmentService.cancel(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


//    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
//    @PostMapping(value = "changePayment/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Void> changePayment(@PathVariable Long id) throws Exception {
//        enrollmentService.changePaymnet(id);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }
}
