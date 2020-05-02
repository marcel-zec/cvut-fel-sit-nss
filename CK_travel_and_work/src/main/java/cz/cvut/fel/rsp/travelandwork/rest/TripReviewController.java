package cz.cvut.fel.rsp.travelandwork.rest;

import cz.cvut.fel.rsp.travelandwork.exception.NotFoundException;
import cz.cvut.fel.rsp.travelandwork.model.TripReview;
import cz.cvut.fel.rsp.travelandwork.service.TripReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create() {

    }

    @PatchMapping(value = "/{identificator}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable Long identifictor) throws NotFoundException{

    }

    @DeleteMapping(value = "/{identificator}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long identifictor) throws NotFoundException {

    }
}
