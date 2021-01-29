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

    /**
     * Method return all Offers for user with ADMIN or MANAGER role and all active (has min. one available session) Offers for other users.
     * @return response with collection of OfferDto
     */
    @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<OfferDto>> getAll() {

        if(!SecurityUtils.isAuthenticatedAnonymously()) {
            if(SecurityUtils.getCurrentUser().getRole().equals(Role.ADMIN) || SecurityUtils.getCurrentUser().getRole().equals(Role.MANAGER)) {
                return ResponseEntity.status(HttpStatus.OK).body(offerService.findAllDto());
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(offerService.findAllDtoFiltered());
    }

    /**
     * Method return filtered Offers.
     * @param location - /filter?location=Tokyo
     * @param from_date - /filter?from_date=2020-06-07
     * @param to_date - /filter?to_date=2020-06-07
     * @param minPrice - /filter?min_price=200 - minimal salary
     * @param search - /filter?search=apples
     * @return response with list of OfferDto
     */
    //endpoint looks like that:
    // localhost:8080/trip/filter?location=Tokyo, Japan&min_price=4000&from_date=2020-06-07&to_date=2020-06-18&search=fugu
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OfferDto>> getAllTripsByFilter(@RequestParam(required = false) String location,
                                                              @RequestParam(required = false) String from_date,
                                                              @RequestParam(required = false) String to_date,
                                                              @RequestParam(value = "min_price", required = false) Double minPrice,
                                                              @RequestParam(value = "search", required = false) String[] search) {
        return ResponseEntity.status(HttpStatus.OK).body(offerService.getAllTripsByFilter(location, from_date, to_date, minPrice, search));

    }

    /**
     * Method find Offer by string identifier.
     * @param identificator
     * @return response with OfferDto
     */
    @GetMapping(value = "/{identificator}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OfferDto> get(@PathVariable String identificator) {

        if(!SecurityUtils.isAuthenticatedAnonymously()) {
            if(SecurityUtils.getCurrentUser().getRole().equals(Role.ADMIN) || SecurityUtils.getCurrentUser().getRole().equals(Role.MANAGER)) {
                return ResponseEntity.status(HttpStatus.OK).body(offerService.findByStringFiltered(identificator));
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(offerService.findByStringFiltered(identificator));
    }

    /**
     * Create new Offer. Available only for user with MANAGER role.
     * @param offer - JSON representation of Offer
     * @return response 201
     * @throws BadDateException - when date_from in session is after date_to
     * @throws MissingVariableException - when some attribute of offer is missing
     */
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestBody Offer offer) throws BadDateException, MissingVariableException {
        offerService.create(offer);
        LOG.info("Offer {} created.", offer.getShort_name());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Update Offer by string identifier. Available only for user with MANAGER role, which is owner of Offer.
     * @param identificator
     * @param offer - JSON representation of updated Offer
     * @return
     * @throws BadDateException - when date_from in session is after date_to
     * @throws NotFoundException - when Offer was not found
     * @throws MissingVariableException - when some attribute of offer is missing
     * @throws NotAllowedException - when Offer is owned by another manager
     */
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PatchMapping(value = "/{identificator}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@PathVariable String identificator, @RequestBody Offer offer) throws BadDateException, NotFoundException, MissingVariableException, NotAllowedException {

        offerService.update(identificator, offer);
        LOG.info("Offer {} updated.", identificator);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Delete Offer by string identifier. Available only for user with MANAGER role, which is owner of Offer, or ADMIN.
     * @param identificator
     * @return response 204
     * @throws NotFoundException - when Offer was not found
     * @throws NotAllowedException - when Offer is owned by another manager
     */
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @DeleteMapping(value = "/{identificator}")
    public ResponseEntity<Void> delete(@PathVariable String identificator) throws NotFoundException, NotAllowedException {

        offerService.delete(identificator);
        LOG.info("Offer {} deleted.", identificator);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Method sign up current user to jobSession which is given to method.
     * @param jobSessionDto - JSON representation of JobSessionDto
     * @return response 204
     * @throws NotAllowedException
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{identificator}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> signUpToTrip(@RequestBody JobSessionDto jobSessionDto) throws NotAllowedException {

        offerService.signUpToTrip(jobSessionDto, SecurityUtils.getCurrentUser());
        LOG.info("Signed up to job session (ID: {}).", jobSessionDto.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //TODO: zapracovat ak bude cas, zmenit na OfferDto
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @GetMapping(value = "/cannotAfford", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<Offer>> showAllTripsCantUserAfford() throws NotAllowedException {
//        return ResponseEntity.status(HttpStatus.OK).body(offerService.findNotAfford(SecurityUtils.getCurrentUser()));
//    }

    //TODO: zapracovat ak bude cas, zmenit na OfferDto
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @GetMapping(value = "/canAfford", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<Offer>> showAllTripsCanUserAfford() throws NotAllowedException {
//        return ResponseEntity.status(HttpStatus.OK).body(offerService.findNotAfford(SecurityUtils.getCurrentUser()));
//    }
}
