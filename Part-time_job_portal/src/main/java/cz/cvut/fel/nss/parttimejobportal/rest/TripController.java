package cz.cvut.fel.nss.parttimejobportal.rest;

import cz.cvut.fel.nss.parttimejobportal.dto.TripDto;
import cz.cvut.fel.nss.parttimejobportal.dto.TripSessionDto;
import cz.cvut.fel.nss.parttimejobportal.exception.BadDateException;
import cz.cvut.fel.nss.parttimejobportal.exception.MissingVariableException;
import cz.cvut.fel.nss.parttimejobportal.exception.NotAllowedException;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.model.Role;
import cz.cvut.fel.nss.parttimejobportal.model.Trip;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityUtils;
import cz.cvut.fel.nss.parttimejobportal.service.TripService;
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

    //endpoint looks like that:
    // localhost:8080/trip/filter?location=Tokyo, Japan&max_price=4000&from_date=2020-06-07&to_date=2020-06-18&search=fugu
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TripDto> getAllTripsByFilter(@RequestParam(required = false) String location,
                                             @RequestParam(required = false) String from_date,
                                             @RequestParam(required = false) String to_date,
                                             @RequestParam(value = "max_price", required = false) Double maxPrice,
                                             @RequestParam(value = "search", required = false) String[] search) {
        return tripService.getAllTripsByFilter(location, from_date, to_date, maxPrice, search);
    }

    @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TripDto> getAll() {

        if(!SecurityUtils.isAuthenticatedAnonymously()) {
            if(SecurityUtils.getCurrentUser().getRole().equals(Role.ADMIN) || SecurityUtils.getCurrentUser().getRole().equals(Role.SUPERUSER)) {
                return tripService.findAllDto();
            }
        }

        return tripService.findAllDtoFiltered();
    }

    @GetMapping(value = "/{identificator}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TripDto get(@PathVariable String identificator) {

        if(!SecurityUtils.isAuthenticatedAnonymously()) {
            if(SecurityUtils.getCurrentUser().getRole().equals(Role.ADMIN) || SecurityUtils.getCurrentUser().getRole().equals(Role.SUPERUSER)) {
                return tripService.findByString(identificator);
            }
        }

        return tripService.findByStringFiltered(identificator);
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
    public void signUpToTrip(@RequestBody TripSessionDto tripSessionDto) throws NotAllowedException {
        //ResponseEntity<Void>
        //return new ResponseEntity<>(headers, HttpStatus.SUCCESS);
        tripService.signUpToTrip(tripSessionDto, SecurityUtils.getCurrentUser());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/cannotAfford", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Trip> showAllTripsCantUserAfford() throws NotAllowedException {
        return tripService.findNotAfford(SecurityUtils.getCurrentUser());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/canAfford", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Trip> showAllTripsCanUserAfford() throws NotAllowedException {
        return tripService.findAfford(SecurityUtils.getCurrentUser());
    }
}
