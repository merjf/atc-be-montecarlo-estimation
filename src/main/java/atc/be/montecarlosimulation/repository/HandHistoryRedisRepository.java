package atc.be.montecarlosimulation.repository;

import atc.be.montecarlosimulation.document.HandHistoryRedisDocument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandHistoryRedisRepository extends CrudRepository<HandHistoryRedisDocument, String> {
    // find by numeric property range
    Iterable<HandHistoryRedisDocument> findByWinBetween(int winLB, int winHB);

}