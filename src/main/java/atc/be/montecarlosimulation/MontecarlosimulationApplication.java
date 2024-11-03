package atc.be.montecarlosimulation;

import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "atc.be.montecarlosimulation.*")
@EnableRedisDocumentRepositories(basePackages = "atc.be.montecarlosimulation.*")
public class MontecarlosimulationApplication {
	public static void main(String[] args) {
		SpringApplication.run(MontecarlosimulationApplication.class, args);
	}
}
