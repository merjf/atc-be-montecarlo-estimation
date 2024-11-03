package atc.be.montecarlosimulation.repository.redis;

import atc.be.montecarlosimulation.document.HandHistoryRedisDocument;
import com.redis.om.spring.repository.RedisDocumentRepository;

public interface HandHistoryRedisDocumentRepository extends RedisDocumentRepository<HandHistoryRedisDocument, String> {
}