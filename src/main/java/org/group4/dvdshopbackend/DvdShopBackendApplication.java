package org.group4.dvdshopbackend;

import org.group4.dvdshopbackend.security.jwt.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // BaseEntity
@EnableConfigurationProperties(JwtProperties.class) // JwtProperties
public class DvdShopBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DvdShopBackendApplication.class, args);
	}

}
