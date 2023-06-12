package com.nashss.se.yodaservice.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.comprehendmedical.model.DetectEntitiesV2Response;

public class DetectEntitiesV2ResponseConverter implements DynamoDBTypeConverter<String, DetectEntitiesV2Response> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convert(DetectEntitiesV2Response object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to convert DetectEntitiesV2Response to String", e);
        }
    }

    @Override
    public DetectEntitiesV2Response unconvert(String str) {
        try {
            return objectMapper.readValue(str, DetectEntitiesV2Response.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to convert String to DetectEntitiesV2Response", e);
        }
    }
}
