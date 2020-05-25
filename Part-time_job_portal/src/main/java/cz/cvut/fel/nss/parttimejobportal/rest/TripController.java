package cz.cvut.fel.nss.parttimejobportal.rest;

import cz.cvut.fel.nss.parttimejobportal.dto.OfferDto;
import cz.cvut.fel.nss.parttimejobportal.dto.JobSessionDto;
import cz.cvut.fel.nss.parttimejobportal.exception.BadDateException;
import cz.cvut.fel.nss.parttimejobportal.exception.MissingVariableException;
import cz.cvut.fel.nss.parttimejobportal.exception.NotAllowedException;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.model.Role;
import cz.cvut.fel.nss.parttimejobportal.model.Offer;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityConstants;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityUtils;
import cz.cvut.fel.nss.parttimejobportal.service.OfferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = SecurityConstants.ORIGIN_URI)
@RestController
@RequestMapping("/trip")
public class TripController {

    private static final Logger LOG = LoggerFactory.getLogger(TripController.class);
    private OfferService offerService;

    @Autowired
    public TripController(OfferService offerService) {
        this.offerService = offerService;
    }

    //endpoint looks like that:
    // localhost:8080/trip/filter?location=Tokyo, Japan&max_price=4000&from_date=2020-06-07&to_date=2020-06-18&search=fugu
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OfferDto> getAllTripsByFilter(@RequestParam(required = false) String location,
                                             @RequestParam(required = false) String from_date,
                                             @RequestParam(required = false) String to_date,
                                             @RequestParam(value = "max_price", required = false) Double maxPrice,
                                             @RequestParam(value = "search", required = false) String[] search) {
        return offerService.getAllTripsByFilter(location, from_date, to_date, maxPrice, search);
    }

    @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OfferDto> getAll() {

        if(!SecurityUtils.isAuthenticatedAnonymously()) {
            if(SecurityUtils.getCurrentUser().getRole().equals(Role.ADMIN) || SecurityUtils.getCurrentUser().getRole().equals(Role.SUPERUSER)) {
                return offerService.findAllDto();
            }
        }

        return offerService.findAllDtoFiltered();
    }

    @GetMapping(value = "/{identificator}", produces = MediaType.APPLICATION_JSON_VALUE)
    public OfferDto get(@PathVariable String identificator) {

        if(!SecurityUtils.isAuthenticatedAnonymously()) {
            if(SecurityUtils.getCurrentUser().getRole().equals(Role.ADMIN) || SecurityUtils.getCurrentUser().getRole().equals(Role.SUPERUSER)) {
                return offerService.findByString(identificator);
            }
        }

        return offerService.findByStringFiltered(identificator);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERUSER', 'ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody Offer trip) throws BadDateException, MissingVariableException {
        offerService.create(trip);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERUSER', 'ROLE_ADMIN')")
    @PatchMapping(value = "/{identificator}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String identificator, @RequestBody Offer trip) throws BadDateException, NotFoundException, MissingVariableException {

        offerService.update(identificator, trip);
        LOG.info("Offer {} updated.", identificator);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERUSER', 'ROLE_ADMIN')")
    @DeleteMapping(value = "/{identificator}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String identificator) throws NotFoundException {

        offerService.delete(identificator);
        LOG.info("Offer {} deleted.", identificator);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{identificator}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void signUpToTrip(@RequestBody JobSessionDto tripSessionDto) throws NotAllowedException {
        //ResponseEntity<Void>
        //return new ResponseEntity<>(headers, HttpStatus.SUCCESS);
        offerService.signUpToTrip(tripSessionDto, SecurityUtils.getCurrentUser());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/cannotAfford", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Offer> showAllTripsCantUserAfford() throws NotAllowedException {
        return offerService.findNotAfford(SecurityUtils.getCurrentUser());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/canAfford", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Offer> showAllTripsCanUserAfford() throws NotAllowedException {
        return offerService.findAfford(SecurityUtils.getCurrentUser());
    }
}
