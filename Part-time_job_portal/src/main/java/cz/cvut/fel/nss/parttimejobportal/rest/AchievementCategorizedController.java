package cz.cvut.fel.nss.parttimejobportal.rest;

import cz.cvut.fel.nss.parttimejobportal.model.AchievementCategorized;
import cz.cvut.fel.nss.parttimejobportal.service.AchievementCategorizedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/achievement/categorized")
public class AchievementCategorizedController {
    private static final Logger LOG = LoggerFactory.getLogger(AchievementCertificateController.class);
    private final AchievementCategorizedService achievementCategorizedService;

    @Autowired
    public AchievementCategorizedController(AchievementCategorizedService achievementCategorizedService) {
        this.achievementCategorizedService = achievementCategorizedService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AchievementCategorized get(@PathVariable Long id){
        return achievementCategorizedService.find(id);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody AchievementCategorized achievement){
        achievementCategorizedService.update(achievement);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AchievementCategorized> getAll() {
        return achievementCategorizedService.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody AchievementCategorized achievement){
        achievementCategorizedService.create(achievement);
    }
}
