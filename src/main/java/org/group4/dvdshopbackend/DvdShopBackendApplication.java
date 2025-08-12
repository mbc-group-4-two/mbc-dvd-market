package org.group4.dvdshopbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DvdShopBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DvdShopBackendApplication.class, args);
	}

}
