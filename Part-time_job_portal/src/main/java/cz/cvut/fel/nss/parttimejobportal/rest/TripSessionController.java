package cz.cvut.fel.nss.parttimejobportal.rest;

import cz.cvut.fel.nss.parttimejobportal.dto.JobSessionDto;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.model.JobSession;
import cz.cvut.fel.nss.parttimejobportal.service.JobSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trip_session")
public class TripSessionController {

    private JobSessionService jobSessionService;

    @Autowired
    public TripSessionController(JobSessionService jobSessionService) {
        this.jobSessionService = jobSessionService;
    }


    @GetMapping(value = "/{trip_short_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JobSessionDto> findAllInTrip(@PathVariable String trip_short_name) {
        return jobSessionService.findAllInTrip(trip_short_name);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERUSER', 'ROLE_ADMIN')")
    @PostMapping(value = "/{trip_short_name}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@PathVariable String trip_short_name, @RequestBody JobSession tripSession) throws Exception {
        jobSessionService.create(trip_short_name, tripSession);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable Long id) throws NotFoundException{

    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws NotFoundException {

    }
}
