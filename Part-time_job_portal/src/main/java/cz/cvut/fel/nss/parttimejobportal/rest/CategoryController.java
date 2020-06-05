package cz.cvut.fel.nss.parttimejobportal.rest;

import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.model.Category;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityConstants;
import cz.cvut.fel.nss.parttimejobportal.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = SecurityConstants.ORIGIN_URI)
@RestController
@RequestMapping("/category")
public class CategoryController {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Method find all Categories.
     * @return response with list of Categories
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findAll());
    }

    /**
     * Method find and return Category by id.
     * @param id
     * @return response with Category
     * @throws NotFoundException - when Category was not found
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> get(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.find(id));
    }

    /**
     * Create new Category. Available only for user with ADMIN role.
     * @param category - JSON representation of Category
     * @return response 201
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> create(@RequestBody Category category){
        categoryService.create(category);
        LOG.info("Category with ID:{} and name '{}' created.", category.getId(), category.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Update of Category by id. Available only for user with ADMIN role.
     * @param id
     * @param category - JSON representation of updated Category
     * @return response 204
     * @throws NotFoundException - when category was not found
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody Category category) throws NotFoundException {
        categoryService.update(id,category);
        LOG.info("Category with ID:{} and updated.", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
