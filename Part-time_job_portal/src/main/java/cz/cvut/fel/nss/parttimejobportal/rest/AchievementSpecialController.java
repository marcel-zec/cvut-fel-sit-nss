package cz.cvut.fel.nss.parttimejobportal.rest;

import cz.cvut.fel.nss.parttimejobportal.model.AchievementSpecial;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityConstants;
import cz.cvut.fel.nss.parttimejobportal.service.AchievementSpecialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = SecurityConstants.ORIGIN_URI)
@RequestMapping("/achievement/special")
public class AchievementSpecialController {
    private static final Logger LOG = LoggerFactory.getLogger(AchievementCertificateController.class);
    private final AchievementSpecialService achievementSpecialService;

    @Autowired
    public AchievementSpecialController(AchievementSpecialService achievementSpecialService) {
        this.achievementSpecialService = achievementSpecialService;
    }

    /**
     * method return AchievementSpecial by id
     * @param id
     * @return response with AchievementSpecial
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AchievementSpecial> get(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(achievementSpecialService.find(id));
    }

    /**
     * update AchievementSpecial
     * @param achievement - JSON representation of AchievementSpecial
     * @return response 204
     */
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> update(@RequestBody AchievementSpecial achievement){
        achievementSpecialService.update(achievement);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * method return list of AchievementSpecial
     * @return response with list
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AchievementSpecial>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(achievementSpecialService.findAll());
    }

    /**
     * method create AchievementCategorized
     * @param achievement - JSON representation of AchievementSpecial
     * @return response 201
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> create(@RequestBody AchievementSpecial achievement){
        achievementSpecialService.create(achievement);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
