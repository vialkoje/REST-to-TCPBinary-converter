package hello;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
class LoadDatabase {

	@Bean
	CommandLineRunner initDatabase(MeasurementRepository repository) {
		return args -> {
			log.info("Preloading " + repository.save(new Measurement("Bilbo Baggins", "burglar")));
			log.info("Preloading " + repository.save(new Measurement("Frodo Baggins", "thief")));
		};
	}
}
