package atc.be.montecarlosimulation.repository;

import atc.be.montecarlosimulation.document.HandHistoryMongoDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HandHistoryMongoRepository extends MongoRepository<HandHistoryMongoDocument, String> {

        List<HandHistoryMongoDocument> findAll();

        public long count();

}
