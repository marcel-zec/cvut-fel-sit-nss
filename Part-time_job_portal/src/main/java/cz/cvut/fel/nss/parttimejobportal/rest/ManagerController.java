package cz.cvut.fel.nss.parttimejobportal.rest;

import cz.cvut.fel.nss.parttimejobportal.dto.ManagerDto;
import cz.cvut.fel.nss.parttimejobportal.dto.RequestWrapper;
import cz.cvut.fel.nss.parttimejobportal.exception.BadPassword;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
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
@RequestMapping("/manager")
@CrossOrigin(origins = SecurityConstants.ORIGIN_URI, allowCredentials="true")
public class ManagerController {

    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    /**
     * Method create new manager. Available only for user with ADMIN role.
     * @param requestWrapper - contains JSON representation of AbstractUser and attribute password_control
     * @return response 201
     * @throws BadPassword - exception when password not match password_control
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestBody RequestWrapper requestWrapper) throws BadPassword {
        managerService.create((Manager) requestWrapper.getUser(), requestWrapper.getPassword_control());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Method show list of existing managers. Available only for user with ADMIN role.
     * @return list of ManagerDtos
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ManagerDto>> showAll() {
        return ResponseEntity.status(HttpStatus.OK).body(managerService.findAll());
    }

    /**
     * Method show manager by id. Available only for user with ADMIN role.
     * @param id
     * @return response with ManagerDto
     * @throws NotFoundException - when manager was not found by id
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ManagerDto> show(@PathVariable Long id) throws NotFoundException {
        final ManagerDto managerDto = managerService.find(id);
        if (managerDto == null) throw NotFoundException.create("Manager", id);
        return ResponseEntity.status(HttpStatus.OK).body(managerDto);
    }

    /**
     * Update existing manager by id. Available only for user with ADMIN role.
     * @param id
     * @param user - JSON representation with updated body of manager
     * @return response 204
     * @throws NotFoundException - when manager was not found by id
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody AbstractUser user) throws NotFoundException {
        managerService.update((Manager) user, SecurityUtils.getCurrentUser());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
