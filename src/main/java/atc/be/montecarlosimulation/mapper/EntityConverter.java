package atc.be.montecarlosimulation.mapper;

import atc.be.montecarlosimulation.document.HandHistoryMongoDocument;
import atc.be.montecarlosimulation.document.HandHistoryRedisDocument;
import atc.be.montecarlosimulation.response.HandHistory;

public class EntityConverter {

    public static HandHistoryMongoDocument redisToMongoEntity(HandHistoryRedisDocument redisEntity){
        HandHistoryMongoDocument mongoEntity = new HandHistoryMongoDocument();
        mongoEntity.setPlayerHands(redisEntity.getPlayerHands());
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
        redisEntity.setPlayerHands(mongoEntity.getPlayerHands());
        redisEntity.setOtherPlayerCards(mongoEntity.getOtherPlayerCards());
        redisEntity.setId(mongoEntity.getId());
        redisEntity.setWin(mongoEntity.getWin());
        redisEntity.setLose(mongoEntity.getLose());
        redisEntity.setSpare(mongoEntity.getSpare());
        redisEntity.setSamples(mongoEntity.getSamples());
        return redisEntity;
    }

    public static HandHistory redisToResponseDTO(HandHistoryRedisDocument redisEntity){
        HandHistory handHistory = new HandHistory();
        handHistory.setId(redisEntity.getId());
        handHistory.setWin(redisEntity.getWin());
        handHistory.setLose(redisEntity.getLose());
        handHistory.setSpare(redisEntity.getSpare());
        handHistory.setSamples(redisEntity.getSamples());
        handHistory.setCreationDate(redisEntity.getCreationDate());
        return handHistory;
    }
}
