package cz.cvut.fel.nss.parttimejobportal.rest;

import cz.cvut.fel.nss.parttimejobportal.exception.AlreadyExistsException;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.exception.UnauthorizedException;
import cz.cvut.fel.nss.parttimejobportal.model.TripReview;
import cz.cvut.fel.nss.parttimejobportal.service.TripReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trip_review")
public class TripReviewController {

    private final TripReviewService tripReviewService;

    @Autowired
    public TripReviewController(TripReviewService tripReviewService) {
        this.tripReviewService = tripReviewService;
    }

    @GetMapping(value = "/{identificator}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TripReview get(Long identificator) {
        return tripReviewService.find(identificator);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TripReview> getAll() {
        return tripReviewService.findAll();
    }

    @PostMapping(value = "/{enrollmentId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody TripReview tripReview, @PathVariable Long enrollmentId ) throws UnauthorizedException, AlreadyExistsException, NotFoundException {
        tripReviewService.create(tripReview, enrollmentId);
    }

    @PatchMapping(value = "/{identificator}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable Long identifictor) throws NotFoundException{

    }

    @DeleteMapping(value = "/{identificator}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long identifictor) throws NotFoundException {

    }
}
