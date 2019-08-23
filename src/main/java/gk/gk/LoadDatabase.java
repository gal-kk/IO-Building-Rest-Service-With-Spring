package gk.gk;

import gk.gk.Repository.EmployeeRepository;
import gk.gk.Repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {
    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository){
        return args -> {
            log.info("Preloading " + employeeRepository.save(new Employee("Gal", "kk", "CEO")));
            log.info("Preloading " + employeeRepository.save(new Employee("Yearning", "gk", "CFO")));
            orderRepository.save(new Order("MacBook Pro", Status.COMPLETED));
            orderRepository.save(new Order("iPhone", Status.IN_PROGRESS));

            orderRepository.findAll().forEach(order -> {
                log.info("Preloaded " + order);
            });
        };


    }

}
