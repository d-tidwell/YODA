package com.nashss.se.yodaservice.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.yodaservice.dynamodb.models.Dictation;
import com.nashss.se.yodaservice.exceptions.DictationNotFoundException;
import com.nashss.se.yodaservice.exceptions.PHRNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Objects;

public class DictationDAO {
    private final Logger log = LogManager.getLogger();
    private final DynamoDBMapper dynamoDbMapper;

    @Inject
    public DictationDAO(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    public Dictation getDictation(String dictationId, String date){
        Dictation dic = this.dynamoDbMapper.load(Dictation.class, dictationId, date);

        if(Objects.isNull(dic)){
            log.info(String.format("DictationNotFoundException, %s", dic));
            throw new DictationNotFoundException("Could not find PHR");
        }
        return dic;
    }
}
