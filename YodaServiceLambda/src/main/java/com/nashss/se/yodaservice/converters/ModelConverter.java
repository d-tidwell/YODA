package com.nashss.se.yodaservice.converters;

import com.nashss.se.yodaservice.dynamodb.models.PHR;
import com.nashss.se.yodaservice.dynamodb.models.Patient;
import com.nashss.se.yodaservice.models.PHRModel;
import com.nashss.se.yodaservice.models.PatientModel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModelConverter {

    public static PHRModel phrConvertSingle(PHR phr) {
        System.out.println("MODEL CONVERTER= "+phr.toString());
        return PHRModel.builder()
                .phrId(Optional.ofNullable(phr.getPhrId()).orElse(null))
                .patientId(Optional.ofNullable(phr.getPatientId()).orElse(null))
                .providerName(Optional.ofNullable(phr.getProviderName()).orElse(null))
                .date(Optional.ofNullable(phr.getDate()).orElse(null))
                .status(Optional.ofNullable(phr.getStatus()).orElse(null))
                .comprehendData(Optional.ofNullable(phr.getComprehendData()).orElse(null))
                .transcription(Optional.ofNullable(phr.getTranscription()).orElse(null))
                .build();
    }

    public static List<PHRModel> convertListPHRtoModels(List<PHR> dynamoObj) {
        return dynamoObj.stream()
                .map(phr -> PHRModel.builder()
                        .phrId(Optional.ofNullable(phr.getPhrId()).orElse(null))
                        .patientId(Optional.ofNullable(phr.getPatientId()).orElse(null))
                        .providerName(Optional.ofNullable(phr.getProviderName()).orElse(null))
                        .date(Optional.ofNullable(phr.getDate()).orElse(null))
                        .status(Optional.ofNullable(phr.getStatus()).orElse(null))
                        .comprehendData(Optional.ofNullable(phr.getComprehendData()).orElse(null))
                        .transcription(Optional.ofNullable(phr.getTranscription()).orElse(null))
                        .build())
                .collect(Collectors.toList());
    }

    public static List<PatientModel> convertListPatientoModels(List<Patient> dynamoObj) {
        return dynamoObj.stream()
                .map(patient -> PatientModel.builder()
                        .withId(patient.getPatientId())
                        .withName(patient.getName())
                        .withAge(patient.getAge())
                        .build())
                .collect(Collectors.toList());
    }
}
