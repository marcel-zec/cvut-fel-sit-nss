package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.model.Category;
import cz.cvut.fel.nss.parttimejobportal.dao.CategoryDao;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {
    private final CategoryDao categoryDao;

    @Autowired
    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }


    @Transactional
    public void create(Category category){
        Objects.requireNonNull(category);
        categoryDao.persist(category);
    }

    @Transactional
    public void update(Long id, Category category) throws NotFoundException {
        Objects.requireNonNull(category);
        Category found = categoryDao.find(id);
        if (found == null) throw new NotFoundException();
        found.setName(category.getName());
        categoryDao.update(found);
    }

    public Category find(Long id) throws NotFoundException {
        Category found = categoryDao.find(id);
        if (found == null) throw new NotFoundException();
        return found;
    }

    public List<Category> findAll(){
        return categoryDao.findAll();
    }
}
