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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@CrossOrigin(origins = SecurityConstants.ORIGIN_URI)
@RestController
@RequestMapping("/trip")
public class OfferController {

    private static final Logger LOG = LoggerFactory.getLogger(OfferController.class);
    private OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    //endpoint looks like that:
    // localhost:8080/trip/filter?location=Tokyo, Japan&max_price=4000&from_date=2020-06-07&to_date=2020-06-18&search=fugu
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OfferDto>> getAllTripsByFilter(@RequestParam(required = false) String location,
                                                              @RequestParam(required = false) String from_date,
                                                              @RequestParam(required = false) String to_date,
                                                              @RequestParam(value = "max_price", required = false) Double maxPrice,
                                                              @RequestParam(value = "search", required = false) String[] search) {
        return ResponseEntity.status(HttpStatus.OK).body(offerService.getAllTripsByFilter(location, from_date, to_date, maxPrice, search));

    }

    @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<OfferDto>> getAll() {

        if(!SecurityUtils.isAuthenticatedAnonymously()) {
            if(SecurityUtils.getCurrentUser().getRole().equals(Role.ADMIN) || SecurityUtils.getCurrentUser().getRole().equals(Role.MANAGER)) {

                return ResponseEntity.status(HttpStatus.OK).body(offerService.findAllDto());
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(offerService.findAllDtoFiltered());

    }

    @GetMapping(value = "/{identificator}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OfferDto> get(@PathVariable String identificator) {

        if(!SecurityUtils.isAuthenticatedAnonymously()) {
            if(SecurityUtils.getCurrentUser().getRole().equals(Role.ADMIN) || SecurityUtils.getCurrentUser().getRole().equals(Role.MANAGER)) {
                return ResponseEntity.status(HttpStatus.OK).body(offerService.findByStringFiltered(identificator));
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(offerService.findByStringFiltered(identificator));
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestBody Offer offer) throws BadDateException, MissingVariableException {
        offerService.create(offer);
        LOG.info("Offer {} created.", offer.getShort_name());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PatchMapping(value = "/{identificator}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> update(@PathVariable String identificator, @RequestBody Offer offer) throws BadDateException, NotFoundException, MissingVariableException, NotAllowedException {

        offerService.update(identificator, offer);
        LOG.info("Offer {} updated.", identificator);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @DeleteMapping(value = "/{identificator}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable String identificator) throws NotFoundException, NotAllowedException {

        offerService.delete(identificator);
        LOG.info("Offer {} deleted.", identificator);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{identificator}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> signUpToTrip(@RequestBody JobSessionDto jobSessionDto) throws NotAllowedException {

        offerService.signUpToTrip(jobSessionDto, SecurityUtils.getCurrentUser());
        LOG.info("Signed up to job session (ID: {}).", jobSessionDto.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/cannotAfford", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Offer>> showAllTripsCantUserAfford() throws NotAllowedException {
        return ResponseEntity.status(HttpStatus.OK).body(offerService.findNotAfford(SecurityUtils.getCurrentUser()));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/canAfford", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Offer>> showAllTripsCanUserAfford() throws NotAllowedException {
        return ResponseEntity.status(HttpStatus.OK).body(offerService.findNotAfford(SecurityUtils.getCurrentUser()));
    }
}
