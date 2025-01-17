package fun.lance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdminApp {
    public static void main(String[] args) {
        new SpringApplication(AdminApp.class).run(args);
    }
}
