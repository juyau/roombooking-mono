package org.thebreak.roombooking;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableMongoAuditing
@EnableAsync
@OpenAPIDefinition(info = @Info(title="Room Booking API", version="1.0.0", description="api for room booking microservice"))
@EnableMongoRepositories(basePackages = "org.thebreak.roombooking.dao")
public class RoombookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoombookingApplication.class, args);
    }

}
