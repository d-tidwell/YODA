package com.nashss.se.yodaservice.dynamodb;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.nashss.se.yodaservice.dynamodb.models.Dictation;
import com.nashss.se.yodaservice.exceptions.DictationNotFoundException;
import com.nashss.se.yodaservice.exceptions.PHRNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.net.URL;
import java.util.Date;
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

    public String generatePresignedUrl(String filename) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; // Add 1 hour.
        expiration.setTime(expTimeMillis);

        // Generate the presigned URL.
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, "/audio/" + filename)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiration);

        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toString();
    }
}
}
