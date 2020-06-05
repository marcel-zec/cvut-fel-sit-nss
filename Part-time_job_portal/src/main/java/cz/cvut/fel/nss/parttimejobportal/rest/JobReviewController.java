package cz.cvut.fel.nss.parttimejobportal.rest;

import cz.cvut.fel.nss.parttimejobportal.exception.AlreadyExistsException;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.exception.UnauthorizedException;
import cz.cvut.fel.nss.parttimejobportal.model.JobReview;
import cz.cvut.fel.nss.parttimejobportal.service.JobReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job_review")
public class JobReviewController {

    private final JobReviewService jobReviewService;

    @Autowired
    public JobReviewController(JobReviewService jobReviewService) {
        this.jobReviewService = jobReviewService;
    }


    /**
     * method returns 1 jobReview with identificator
     * @param identificator
     * @return response of JobReview
     */
    @GetMapping(value = "/{identificator}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobReview> get(Long identificator) {
        return ResponseEntity.status(HttpStatus.OK).body(jobReviewService.find(identificator));
    }


    /**
     * method returns all jobReviews
     * @return response of list of JobReview
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<JobReview>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(jobReviewService.findAll());
    }


    /**
     * method create new jobReview
     * @param jobReview
     * @param enrollmentId
     * @return void response
     * @throws UnauthorizedException
     * @throws AlreadyExistsException
     * @throws NotFoundException
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{enrollmentId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestBody JobReview jobReview, @PathVariable Long enrollmentId ) throws UnauthorizedException, AlreadyExistsException, NotFoundException {
        jobReviewService.create(jobReview, enrollmentId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
