package cz.cvut.fel.rsp.travelandwork.rest;

import cz.cvut.fel.rsp.travelandwork.dto.TripDto;
import cz.cvut.fel.rsp.travelandwork.dto.TripSessionDto;
import cz.cvut.fel.rsp.travelandwork.exception.BadDateException;
import cz.cvut.fel.rsp.travelandwork.exception.MissingVariableException;
import cz.cvut.fel.rsp.travelandwork.exception.NotFoundException;
import cz.cvut.fel.rsp.travelandwork.model.Trip;
import cz.cvut.fel.rsp.travelandwork.security.SecurityUtils;
import cz.cvut.fel.rsp.travelandwork.service.TripService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/trip")
public class TripController {

    private static final Logger LOG = LoggerFactory.getLogger(TripController.class);
    private TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Trip> getAll() {
        return tripService.findAll();
    }

    @GetMapping(value = "/shortcut", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TripDto> getAllDto() {
        return tripService.findAllDto();
    }

    @GetMapping(value = "/{identificator}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Trip get(@PathVariable String identificator) {
        return tripService.findByString(identificator);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERUSER', 'ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody Trip trip) throws BadDateException, MissingVariableException {
        tripService.create(trip);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERUSER', 'ROLE_ADMIN')")
    @PatchMapping(value = "/{identificator}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String identificator, @RequestBody Trip trip) throws BadDateException, NotFoundException, MissingVariableException {

        tripService.update(identificator, trip);
        LOG.info("Trip {} updated.", identificator);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERUSER', 'ROLE_ADMIN')")
    @DeleteMapping(value = "/{identificator}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String identificator) throws NotFoundException {

        tripService.delete(identificator);
        LOG.info("Trip {} deleted.", identificator);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{identificator}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void signUpToTrip(@RequestBody TripSessionDto tripSessionDto) {
        //ResponseEntity<Void>
        //return new ResponseEntity<>(headers, HttpStatus.SUCCESS);
        tripService.signUpToTrip(tripSessionDto, SecurityUtils.getCurrentUser());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/cannotAfford", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Trip> showAllTripsCantUserAfford() {
        return tripService.findNotAfford(SecurityUtils.getCurrentUser());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/canAfford", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Trip> showAllTripsCanUserAfford() {
        return tripService.findAfford(SecurityUtils.getCurrentUser());
    }
}
