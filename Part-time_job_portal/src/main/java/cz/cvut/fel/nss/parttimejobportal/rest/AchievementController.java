package cz.cvut.fel.nss.parttimejobportal.rest;

import cz.cvut.fel.nss.parttimejobportal.dto.AchievementDto;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityConstants;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityUtils;
import cz.cvut.fel.nss.parttimejobportal.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = SecurityConstants.ORIGIN_URI)
@RequestMapping("/achievement")
public class AchievementController {

    private final AchievementService achievementService;

    @Autowired
    public AchievementController(AchievementService achievementService) {

        this.achievementService = achievementService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AchievementDto> getAllOfUser() {
        return achievementService.findAllOfUser(SecurityUtils.getCurrentUser());
    }
}
