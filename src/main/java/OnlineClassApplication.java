

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableAutoConfiguration
@EnableJpaRepositories("com.onlineclass.repository")
@EntityScan("com.onlineclass.entity")
@ComponentScan("com.onlineclass")
public class OnlineClassApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineClassApplication.class, args);
    }

}
