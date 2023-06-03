package com.nashss.se.yodaservice.converters;

import com.nashss.se.yodaservice.dynamodb.models.PHR;
import com.nashss.se.yodaservice.dynamodb.models.Patient;
import com.nashss.se.yodaservice.models.PHRModel;
import com.nashss.se.yodaservice.models.PatientModel;

import java.util.List;
import java.util.stream.Collectors;

public class ModelConverter {

    public static PHRModel phrConvertSingle(PHR phr) {
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

    public static List<PatientModel> convertListPatientoModels(List<Patient> dynamoObj) {
        return dynamoObj.stream()
                .map(patient -> PatientModel.builder()
                        .withName(patient.getName())
                        .withAge(patient.getAge())
                        .build())
                .collect(Collectors.toList());
    }
}
