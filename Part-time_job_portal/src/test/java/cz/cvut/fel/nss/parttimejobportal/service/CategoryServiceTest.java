package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.model.Category;
import cz.cvut.fel.nss.parttimejobportal.environment.util.Generator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class CategoryServiceTest {
    Category testCategory = Generator.generateCategory();
    List<Category> list = Arrays.asList(Generator.generateCategory(), Generator.generateCategory(), Generator.generateCategory());

    @Autowired
    TranslateService ts;

    @Autowired
    private EntityManager em;

    @Autowired
    private CategoryService sut;

    @Before
    public void init() {
        sut.create(testCategory);
        for (Category cs: list) {
            sut.create(cs);
            System.out.println(cs.toString());
        }
    }

    @Test
    @Transactional
    @Rollback
    public void findAll_findsAllCategories() {
        assertNotNull(sut.findAll());
    }

    @Test
    @Transactional
    @Rollback
    public void find_findCategoryById() throws NotFoundException {
        Assert.assertEquals(testCategory, sut.find(testCategory.getId()));
    }

    @Test
    @Transactional
    @Rollback
    public void create_createsNewCategory() throws NotFoundException {
        Category newCategory = Generator.generateCategory();
        sut.create(newCategory);
        Assert.assertEquals(newCategory, sut.find(newCategory.getId()));
    }

    @Test
    @Transactional
    @Rollback
    public void update_updatesCategory() throws NotFoundException {
        testCategory.setName("No Named Achievement. haha.");
        sut.update(testCategory.getId(), testCategory);
        Assert.assertEquals(testCategory, sut.find(testCategory.getId()));
    }


}
