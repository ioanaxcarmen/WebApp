package edu.fra.uas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import edu.fra.uas.service.FirstService;
import edu.fra.uas.service.SecondService;

@SpringBootApplication
public class SpringDependencyInjectionApplication {
	
	@Autowired
	private FirstService firstService;
	@Autowired
	private SecondService secondService;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringDependencyInjectionApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init() {
		CommandLineRunner action = new CommandLineRunner() {

			@Override
			public void run(String... args) throws Exception {
				firstService.setSecondService(secondService);
				firstService.doSomething();
			}
		};
		return action;
	}

}
