//package cz.cvut.fel.nss.parttimejobportal.service;
//
//import cz.cvut.fel.nss.parttimejobportal.environment.util.Generator;
//import cz.cvut.fel.nss.parttimejobportal.model.Achievement;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Transactional
//@TestPropertySource(locations = "classpath:application-test.properties")
//public class AchievementServiceTest {
//
//    Achievement testAchievement = Generator.generateAchievement();
//    List<Achievement> list = Arrays.asList(Generator.generateAchievement(), Generator.generateAchievement(), Generator.generateAchievement());
//
//    @Autowired
//    TranslateService ts;
//
//    @Autowired
//    private EntityManager em;
//
//    @Autowired
//    private AchievementService sut;
//
//    @Before
//    public void init() {
//        sut.create(testAchievement);
//        for (Achievement as: list) {
//            sut.create(as);
//            System.out.println(as.toString());
//        }
//    }
//
//    @Test
//    @Transactional
//    @Rollback
//    public void findAll_findsAllAchievements() {
//        assertNotNull(sut.findAll());
//    }
//
//    @Test
//    @Transactional
//    @Rollback
//    public void find_findAchievById() {
//        assertEquals(testAchievement, sut.find(testAchievement.getId()));
//    }
//
//    @Test
//    @Transactional
//    @Rollback
//    public void create_createsNewAchievement() {
//        Achievement newAchiev = Generator.generateAchievement();
//        sut.create(newAchiev);
//        assertEquals(newAchiev, sut.find(newAchiev.getId()));
//    }
//
//    @Test
//    @Transactional
//    @Rollback
//    public void update_updatesAchievement() {
//        testAchievement.setName("No Named Achievement. haha.");
//        sut.update(testAchievement);
//        assertEquals(testAchievement, sut.find(testAchievement.getId()));
//    }
//
//
//
//
//}
