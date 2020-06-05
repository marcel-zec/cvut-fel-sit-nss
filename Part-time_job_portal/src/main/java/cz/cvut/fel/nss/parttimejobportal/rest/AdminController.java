package cz.cvut.fel.nss.parttimejobportal.rest;

import cz.cvut.fel.nss.parttimejobportal.dto.ManagerDto;
import cz.cvut.fel.nss.parttimejobportal.dto.RequestWrapper;
import cz.cvut.fel.nss.parttimejobportal.exception.BadPassword;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.exception.UnauthorizedException;
import cz.cvut.fel.nss.parttimejobportal.model.AbstractUser;
import cz.cvut.fel.nss.parttimejobportal.model.Manager;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityConstants;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityUtils;
import cz.cvut.fel.nss.parttimejobportal.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = SecurityConstants.ORIGIN_URI, allowCredentials="true")
public class AdminController {

    private final ManagerService managerService;

    @Autowired
    public AdminController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERUSER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> registerAdmin(@RequestBody RequestWrapper requestWrapper) throws BadPassword {
        managerService.create((Manager) requestWrapper.getUser(), requestWrapper.getPassword_control());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERUSER')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ManagerDto> showAll() {
        return managerService.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERUSER')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ManagerDto showAdminById(@PathVariable Long id) throws NotFoundException {
        final ManagerDto adminDto = managerService.find(id);
        if (adminDto == null) {
            throw NotFoundException.create("Admin", id);
        }
        return adminDto;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
    @PatchMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody AbstractUser user) throws NotFoundException, UnauthorizedException {
        managerService.update((Manager) user, SecurityUtils.getCurrentUser());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
