package cz.cvut.fel.nss.parttimejobportal.rest;

import cz.cvut.fel.nss.parttimejobportal.dto.TripSessionDto;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.model.TripSession;
import cz.cvut.fel.nss.parttimejobportal.service.TripSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trip_session")
public class TripSessionController {

    private TripSessionService tripSessionService;

    @Autowired
    public TripSessionController(TripSessionService tripSessionService) {
        this.tripSessionService = tripSessionService;
    }


    @GetMapping(value = "/{trip_short_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TripSessionDto> findAllInTrip(@PathVariable String trip_short_name) {
        return tripSessionService.findAllInTrip(trip_short_name);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERUSER', 'ROLE_ADMIN')")
    @PostMapping(value = "/{trip_short_name}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@PathVariable String trip_short_name, @RequestBody TripSession tripSession) throws Exception {
        tripSessionService.create(trip_short_name, tripSession);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable Long id) throws NotFoundException{

    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws NotFoundException {

    }
}
