package rs.ac.ni.pmf.rwa.estore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(EStoreApplication.class, args);
    }

}
