package cz.cvut.fel.nss.parttimejobportal.rest;

import cz.cvut.fel.nss.parttimejobportal.model.AchievementSpecial;
import cz.cvut.fel.nss.parttimejobportal.service.AchievementSpecialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/achievement/special")
public class AchievementSpecialController {
    private static final Logger LOG = LoggerFactory.getLogger(AchievementCertificateController.class);
    private final AchievementSpecialService achievementSpecialService;

    @Autowired
    public AchievementSpecialController(AchievementSpecialService achievementSpecialService) {
        this.achievementSpecialService = achievementSpecialService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AchievementSpecial get(@PathVariable Long id){
        return achievementSpecialService.find(id);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody AchievementSpecial achievement){
        achievementSpecialService.update(achievement);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AchievementSpecial> getAll() {
        return achievementSpecialService.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody AchievementSpecial achievement){
        achievementSpecialService.create(achievement);
    }
}
