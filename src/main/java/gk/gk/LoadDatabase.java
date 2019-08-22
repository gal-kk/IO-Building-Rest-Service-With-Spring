package gk.gk;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {
    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository){
        return args -> {
            log.info("Preloading " + employeeRepository.save(new Employee("Yearning", "CEO")));
            log.info("Preloading " + employeeRepository.save(new Employee("Gal_kk", "CFO")));
        };
    }
}
