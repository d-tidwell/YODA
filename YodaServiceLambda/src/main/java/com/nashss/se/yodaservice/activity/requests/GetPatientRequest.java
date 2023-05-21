package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
@JsonDeserialize(builder = GetPatientRequest.class)
public class GetPatientRequest {

    private final String patientId;

    public GetPatientRequest(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientId() {
        return patientId;
    }

    @Override
    public String toString() {
        return "GetPatientRequest{" +
                "patientId='" + patientId + '\'' +
                '}';
    }


    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String patientId;


        public Builder withPatientId(String patientId) {
            this.patientId = patientId;
            return this;
        }

        public GetPatientRequest build() {
            return new GetPatientRequest(patientId);
        }

    }
}


