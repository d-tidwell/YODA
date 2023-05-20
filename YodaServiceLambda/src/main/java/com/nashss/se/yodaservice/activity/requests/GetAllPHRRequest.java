package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = GetAllPHRRequest.Builder.class)
public class GetAllPHRRequest {

    private final String patientId;

    private GetAllPHRRequest(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientId() {
        return this.patientId;
    }

    @Override
    public String toString() {
        return "GetAllPHRRequest{" +
                "patientId='" + patientId + '\'' +
                '}';
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String patientId;

        public Builder withPatientId(String patientId) {
            this.patientId = patientId;
            return this;
        }

        public GetAllPHRRequest build() {
            return new GetAllPHRRequest(patientId);
        }
    }
}
