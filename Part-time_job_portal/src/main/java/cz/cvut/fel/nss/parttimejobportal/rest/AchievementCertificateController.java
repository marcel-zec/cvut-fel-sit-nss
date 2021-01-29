package cz.cvut.fel.nss.parttimejobportal.rest;

import cz.cvut.fel.nss.parttimejobportal.model.AchievementCertificate;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityConstants;
import cz.cvut.fel.nss.parttimejobportal.service.AchievementCertificateService;
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
@RequestMapping("/achievement/certificate")
public class AchievementCertificateController {

    private static final Logger LOG = LoggerFactory.getLogger(AchievementCertificateController.class);
    private final AchievementCertificateService achievementCertificateService;

    @Autowired
    public AchievementCertificateController(AchievementCertificateService achievementCertificateService) {
        this.achievementCertificateService = achievementCertificateService;
    }

    /**
     * method return AchievementCertificate by id
     * @param id
     * @return response with AchievementCertificate
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AchievementCertificate> get(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(achievementCertificateService.find(id));
    }

    /**
     * update AchievementCertificate
     * @param achievement - JSON representation of AchievementCertificate
     * @return response 204
     */
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> update(@RequestBody AchievementCertificate achievement){
        achievementCertificateService.update(achievement);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * method return list of AchievementCertificate
     * @return response with list
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AchievementCertificate>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(achievementCertificateService.findAll());
    }

    /**
     * method create AchievementCategorized
     * @param achievement - JSON representation of AchievementCertificate
     * @return response 201
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> create(@RequestBody AchievementCertificate achievement){
        achievementCertificateService.create(achievement);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

