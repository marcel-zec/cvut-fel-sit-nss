package cz.cvut.fel.nss.parttimejobportal.environment.config;

import cz.cvut.fel.nss.parttimejobportal.service.*;
import cz.cvut.fel.nss.parttimejobportal.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;


public class MockServiceConfig {
    @Bean
    public UserService userService() {
        return mock(UserService.class);
    }

    @Bean
    public OfferService offerService() {
        return mock(OfferService.class);
    }
/*
    @Bean
    public AchievementService achievementService() {
        return mock(AchievementService.class);
    }
*/
    @Bean
    public EnrollmentService enrollmentService() {
        return mock(EnrollmentService.class);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return mock(UserDetailsService.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public JobJournalService jobJournalService() {
        return mock(JobJournalService.class);
    }


    @Bean
    public JobReviewService jobReviewService() {
        return mock(JobReviewService.class);
    }

    @Bean
    public JobSessionService jobSessionService() {
        return mock(JobSessionService.class);
    }

}
