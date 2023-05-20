package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
@JsonDeserialize(builder = CreatePatientRequest.class)
public class CreatePatientRequest {

    private final String patientName;
    private final String patientAge;

    public CreatePatientRequest(String patientName, String patientAge) {
        this.patientName = patientName;
        this.patientAge = patientAge;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPatientAge() {
        return patientAge;
    }

    @Override
    public String toString() {
        return "CreatePatientRequest{" +
                "patientName='" + patientName + '\'' +
                ", patientAge='" + patientAge + '\'' +
                '}';
    }


    public static Builder builder() {
        return new Builder();
    }


    @JsonPOJOBuilder
    public static class Builder {
        private String patientName;
        private String patientAge;

        public Builder withPatientName(String patientName) {
            this.patientName = patientName;
            return this;
        }

        public Builder withPatientAge(String patientAge) {
            this.patientAge = patientAge;
            return this;
        }

        public CreatePatientRequest build() {
            return new CreatePatientRequest(patientName, patientAge);
        }

    }
}