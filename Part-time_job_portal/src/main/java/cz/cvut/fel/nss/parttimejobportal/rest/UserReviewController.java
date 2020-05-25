package cz.cvut.fel.nss.parttimejobportal.rest;

import cz.cvut.fel.nss.parttimejobportal.dto.UserReviewDto;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.exception.UnauthorizedException;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityConstants;
import cz.cvut.fel.nss.parttimejobportal.service.UserReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @PreAuthorize("hasAnyRole('ROLE_SUPERUSER', 'ROLE_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserReviewDto> showAll() {
        return userReviewService.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERUSER', 'ROLE_ADMIN')")
    @GetMapping(value= "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserReviewDto> showReviewsOfUser(Long userId) throws NotFoundException {
        return userReviewService.findAllOfUser(userId);
    }

    @GetMapping(value= "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserReviewDto> showReviewsOfCurrentUser() throws UnauthorizedException, NotFoundException {
        return userReviewService.findAllOfUser();
    }

}
