package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = RemovePatientFromProviderRequest.Builder.class)
public class RemovePatientFromProviderRequest {

    private final String patientId;

    private final String providerName;

    private final String position;

    public RemovePatientFromProviderRequest(String patientId, String providerName, String position) {
        this.patientId = patientId;
        this.providerName = providerName;
        this.position = position;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "RemovePatientFromProviderRequest{" +
                "patientId='" + patientId + '\'' +
                ", providerName='" + providerName + '\'' +
                ", position='" + position + '\'' +
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

        private String position;

        public Builder withPatientId(String patientId) {
            this.patientId = patientId;
            return this;
        }

        public Builder withProviderName(String providerName) {
            this.providerName = providerName;
            return this;
        }

        public Builder withPosition(String position) {
            this.position = position;
            return this;
        }

        public RemovePatientFromProviderRequest build() {
            return new RemovePatientFromProviderRequest(patientId, providerName, position);
        }

    }
}


