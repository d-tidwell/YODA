package com.nashss.se.yodaservice.converters;

import com.nashss.se.yodaservice.dynamodb.models.PHR;
import com.nashss.se.yodaservice.models.PHRModel;

import java.util.List;
import java.util.stream.Collectors;

public class ModelConverter {

    public static PHRModel phrConvertSingle(PHR phr){
        return PHRModel.builder()
                .phrId(phr.getPhrId())
                .patientId(phr.getPatientId())
                .providerName(phr.getProviderName())
                .date(phr.getDate())
                .status(phr.getStatus())
                .build();
    }
    public static List<PHRModel> convertListPHRtoModels(List<PHR> dynamoObj) {
        return dynamoObj.stream()
                .map(phr -> PHRModel.builder()
                        .phrId(phr.getPhrId())
                        .patientId(phr.getPatientId())
                        .providerName(phr.getProviderName())
                        .date(phr.getDate())
                        .status(phr.getStatus())
                        .build())
                .collect(Collectors.toList());
    }
}
