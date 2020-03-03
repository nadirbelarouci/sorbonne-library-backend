package fr.sorbonne.gutenberg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@EnableConfigurationProperties
@SpringBootApplication
@EnableCaching
public class GutenbergApplication {

    public static void main(String[] args) {
        SpringApplication.run(GutenbergApplication.class, args);
    }

}
