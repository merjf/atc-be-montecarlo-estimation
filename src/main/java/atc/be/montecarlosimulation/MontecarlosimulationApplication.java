package atc.be.montecarlosimulation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
//@EnableRedisEnhancedRepositories(basePackages = "atc.be.montecarlosimulation.*")
@EnableMongoRepositories
public class MontecarlosimulationApplication {
	public static void main(String[] args) {
		SpringApplication.run(MontecarlosimulationApplication.class, args);
	}
}
