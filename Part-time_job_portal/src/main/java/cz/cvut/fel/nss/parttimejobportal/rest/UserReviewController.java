package cz.cvut.fel.nss.parttimejobportal.rest;

import cz.cvut.fel.nss.parttimejobportal.dto.UserReviewDto;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.exception.UnauthorizedException;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityConstants;
import cz.cvut.fel.nss.parttimejobportal.service.UserReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user_review")
@CrossOrigin(origins = SecurityConstants.ORIGIN_URI, allowCredentials="true")
public class UserReviewController {

    private final UserReviewService userReviewService;

    @Autowired
    public UserReviewController(UserReviewService userReviewService) {
        this.userReviewService = userReviewService;
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserReviewDto>> showAll() {
        return ResponseEntity.status(HttpStatus.OK).body(userReviewService.findAll());
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping(value= "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserReviewDto>> showReviewsOfUser(Long userId) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userReviewService.findAllOfUser(userId));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value= "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserReviewDto>> showReviewsOfCurrentUser() throws UnauthorizedException, NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userReviewService.findAllOfUser());
    }

}
