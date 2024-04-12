package org.rail.spring2024;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main class
 */
@SpringBootApplication
@EnableScheduling
public class Spring2024Application {

    /**
     * Main method
     * @param args arguments that was passed
     */
    public static void main(String[] args) {
        SpringApplication.run(Spring2024Application.class, args);
    }

}
