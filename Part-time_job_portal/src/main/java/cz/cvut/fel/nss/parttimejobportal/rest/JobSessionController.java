package cz.cvut.fel.nss.parttimejobportal.rest;

import cz.cvut.fel.nss.parttimejobportal.dto.JobSessionDto;
import cz.cvut.fel.nss.parttimejobportal.model.JobSession;
import cz.cvut.fel.nss.parttimejobportal.service.JobSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trip_session")
public class JobSessionController {

    private JobSessionService jobSessionService;

    @Autowired
    public JobSessionController(JobSessionService jobSessionService) {
        this.jobSessionService = jobSessionService;
    }


    /**
     * method returns all session in offer
     * @param trip_short_name
     * @return response of list of JobSessionDto
     */
    @GetMapping(value = "/{trip_short_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<JobSessionDto>> findAllInTrip(@PathVariable String trip_short_name) {
        return ResponseEntity.status(HttpStatus.OK).body(jobSessionService.findAllInTrip(trip_short_name));

    }


    /**
     * method add session to offer
     * @param trip_short_name offer where I want to put session
     * @param tripSession session which I want to create
     * @return void response
     * @throws Exception
     */
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping(value = "/{trip_short_name}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@PathVariable String trip_short_name, @RequestBody JobSession tripSession) throws Exception {
        jobSessionService.create(trip_short_name, tripSession);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
