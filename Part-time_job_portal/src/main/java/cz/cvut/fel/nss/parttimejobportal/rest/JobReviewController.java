package cz.cvut.fel.nss.parttimejobportal.rest;

import cz.cvut.fel.nss.parttimejobportal.exception.AlreadyExistsException;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.exception.UnauthorizedException;
import cz.cvut.fel.nss.parttimejobportal.model.JobReview;
import cz.cvut.fel.nss.parttimejobportal.service.JobReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @GetMapping(value = "/{identificator}", produces = MediaType.APPLICATION_JSON_VALUE)
    public JobReview get(Long identificator) {
        return jobReviewService.find(identificator);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JobReview> getAll() {
        return jobReviewService.findAll();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{enrollmentId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody JobReview jobReview, @PathVariable Long enrollmentId ) throws UnauthorizedException, AlreadyExistsException, NotFoundException {
        jobReviewService.create(jobReview, enrollmentId);
    }

//    @PatchMapping(value = "/{identificator}", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public void update(@PathVariable Long identifictor) throws NotFoundException{
//
//    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @DeleteMapping(value = "/{identificator}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long identifictor) throws NotFoundException {

    }
}
