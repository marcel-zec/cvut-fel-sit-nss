package cz.cvut.fel.nss.parttimejobportal.rest;

import cz.cvut.fel.nss.parttimejobportal.model.AchievementCategorized;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityConstants;
import cz.cvut.fel.nss.parttimejobportal.service.AchievementCategorizedService;
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
@RequestMapping("/achievement/categorized")
public class AchievementCategorizedController {
    private static final Logger LOG = LoggerFactory.getLogger(AchievementCertificateController.class);
    private final AchievementCategorizedService achievementCategorizedService;

    @Autowired
    public AchievementCategorizedController(AchievementCategorizedService achievementCategorizedService) {
        this.achievementCategorizedService = achievementCategorizedService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AchievementCategorized> get(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(achievementCategorizedService.find(id));
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> update(@RequestBody AchievementCategorized achievement){
        achievementCategorizedService.update(achievement);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AchievementCategorized>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(achievementCategorizedService.findAll());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> create(@RequestBody AchievementCategorized achievement){
        achievementCategorizedService.create(achievement);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
