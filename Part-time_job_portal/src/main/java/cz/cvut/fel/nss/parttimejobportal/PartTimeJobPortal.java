package cz.cvut.fel.nss.parttimejobportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class PartTimeJobPortal {

	public static void main(String[] args) {
		SpringApplication.run(PartTimeJobPortal.class, args);
	}

}
