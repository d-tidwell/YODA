package com.nashss.se.yodaservice.dynamodb;

import com.nashss.se.yodaservice.dynamodb.models.Dictation;
import com.nashss.se.yodaservice.exceptions.DictationNotFoundException;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import javax.inject.Inject;

public class DictationDAO {
    private final Logger log = LogManager.getLogger();
    private final DynamoDBMapper dynamoDbMapper;

    @Inject
    public DictationDAO(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    public Dictation getDictation(String dictationId, String date) {
        Dictation dic = this.dynamoDbMapper.load(Dictation.class, dictationId, date);

        if (Objects.isNull(dic)) {
            log.info(String.format("DictationNotFoundException, %s", dic));
            throw new DictationNotFoundException("Could not find PHR");
        }
        return dic;
    }

    public boolean createDictation(String dictationId, String phRid, String date) {
        Dictation newDic = new Dictation();
        newDic.setDictationId(dictationId);
        newDic.setPhrId(phRid);
        newDic.setDate(date);
        newDic.setDictationText("audio/"+dictationId);
        try {
            this.dynamoDbMapper.save(newDic);
            return true;
        } catch (AmazonDynamoDBException e) {
            log.error(String.format("Create Dictation Error %s, %s", newDic.toString(), e));
        }
        return false;
    }
    public boolean afterTranscriptionUpdate(Dictation dic){
        try {
            this.dynamoDbMapper.save(dic);
            return true;
        } catch (AmazonDynamoDBException e) {
            log.error(String.format("Create Dictation Error %s, %s", dic.toString(), e));
        }
        return false;
    }

}


