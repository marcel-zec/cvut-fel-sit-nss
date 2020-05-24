package cz.cvut.fel.nss.parttimejobportal.service.security;

import cz.cvut.fel.nss.parttimejobportal.environment.util.Generator;
import cz.cvut.fel.nss.parttimejobportal.model.User;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityUtils;
import cz.cvut.fel.nss.parttimejobportal.security.model.UserDetails;
import cz.cvut.fel.nss.parttimejobportal.environment.util.Environment;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.security.core.context.SecurityContextHolder;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SecurityUtilsTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private User user;

    @Before
    public void setUp() {
        this.user = Generator.generateUser();
    }

    @After
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void getCurrentUserReturnsCurrentlyLoggedInUser() {
        Environment.setCurrentUser(user);
        final User result = SecurityUtils.getCurrentUser();
        assertEquals(user, result);
    }

    @Test
    public void getCurrentUserDetailsReturnsUserDetailsOfCurrentlyLoggedInUser() {
        Environment.setCurrentUser(user);
        final UserDetails result = SecurityUtils.getCurrentUserDetails();
        assertNotNull(result);
        assertTrue(result.isEnabled());
        assertEquals(user, result.getUser());
    }

    @Test
    public void getCurrentUserDetailsReturnsNullIfNoUserIsLoggedIn() {
        TestCase.assertNull(SecurityUtils.getCurrentUserDetails());
    }

}
