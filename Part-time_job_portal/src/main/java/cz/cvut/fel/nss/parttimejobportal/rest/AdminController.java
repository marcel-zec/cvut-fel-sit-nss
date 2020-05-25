package cz.cvut.fel.nss.parttimejobportal.rest;

import cz.cvut.fel.nss.parttimejobportal.dto.UserDto;
import cz.cvut.fel.nss.parttimejobportal.dto.RequestWrapper;
import cz.cvut.fel.nss.parttimejobportal.exception.BadPassword;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.exception.UnauthorizedException;
import cz.cvut.fel.nss.parttimejobportal.model.User;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityConstants;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityUtils;
import cz.cvut.fel.nss.parttimejobportal.service.AdminService;
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

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERUSER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> registerAdmin(@RequestBody RequestWrapper requestWrapper) throws BadPassword {
        adminService.create(requestWrapper.getUser(), requestWrapper.getPassword_control());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERUSER')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> showAll() {
        return adminService.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERUSER')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto showAdminById(@PathVariable Long id) throws NotFoundException {
        final UserDto adminDto = adminService.find(id);
        if (adminDto == null) {
            throw NotFoundException.create("Admin", id);
        }
        return adminDto;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
    @PatchMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody User user) throws NotFoundException, UnauthorizedException {
        adminService.update(user, SecurityUtils.getCurrentUser());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
