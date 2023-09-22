package atc.be.montecarlosimulation.converter;

import atc.be.montecarlosimulation.document.HandHistoryMongoDocument;
import atc.be.montecarlosimulation.document.HandHistoryRedisDocument;

public class EntityConverter {

    public static HandHistoryMongoDocument redisToMongoEntity(HandHistoryRedisDocument redisEntity){
        HandHistoryMongoDocument mongoEntity = new HandHistoryMongoDocument();
        mongoEntity.setMainPlayerHands(redisEntity.getMainPlayerHands());
        mongoEntity.setOtherPlayerCards(redisEntity.getOtherPlayerCards());
        mongoEntity.setId(redisEntity.getId());
        mongoEntity.setWin(redisEntity.getWin());
        mongoEntity.setLose(redisEntity.getLose());
        mongoEntity.setSpare(redisEntity.getSpare());
        mongoEntity.setSamples(redisEntity.getSamples());
        return mongoEntity;
    }

    public static HandHistoryRedisDocument mongoToRedisEntity(HandHistoryMongoDocument mongoEntity){
        HandHistoryRedisDocument redisEntity = new HandHistoryRedisDocument();
        redisEntity.setMainPlayerHands(mongoEntity.getMainPlayerHands());
        redisEntity.setOtherPlayerCards(mongoEntity.getOtherPlayerCards());
        redisEntity.setId(mongoEntity.getId());
        redisEntity.setWin(mongoEntity.getWin());
        redisEntity.setLose(mongoEntity.getLose());
        redisEntity.setSpare(mongoEntity.getSpare());
        redisEntity.setSamples(mongoEntity.getSamples());
        return redisEntity;
    }
}
