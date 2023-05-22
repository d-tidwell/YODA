package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = RemovePatientFromProviderRequest.Builder.class)
public class RemovePatientFromProviderRequest {

    private final String patientId;

    private final String providerName;

    public RemovePatientFromProviderRequest(String patientId, String providerName) {
        this.patientId = patientId;
        this.providerName = providerName;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getProviderName() {
        return providerName;
    }

    @Override
    public String toString() {
        return "RemovePatientFromProviderRequest{" +
                "patientId='" + patientId + '\'' +
                ", providerName='" + providerName + '\'' +
                '}';
    }
    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    @JsonPOJOBuilder
    public static class Builder {
        private String patientId;

        private String providerName;

        public Builder withPatientId(String patientId) {
            this.patientId = patientId;
            return this;
        }

        public Builder withProviderName(String providerName) {
            this.providerName = providerName;
            return this;
        }

        public RemovePatientFromProviderRequest build() {
            return new RemovePatientFromProviderRequest(patientId, providerName);
        }

    }
}


