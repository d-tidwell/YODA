package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
@JsonDeserialize(builder = CreatePatientRequest.class)
public class CreatePatientRequest{

    private final String patientName;

    public CreatePatientRequest(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientName() {
        return patientName;
    }

    @Override
    public String toString() {
        return "AddPatientToProviderRequest{" +
                "patientName='" + patientName + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String patientName;

        public Builder withPatientName(String patientName) {
            this.patientName = patientName;
            return this;
        }

        public CreatePatientRequest build() {
            return new CreatePatientRequest(patientName);
        }

    }
