package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
@JsonDeserialize(builder = AddPatientToProviderRequest.Builder.class)
public class AddPatientToProviderRequest{

    private final String patientId;

    private final String providerName;

    public AddPatientToProviderRequest(String patientId, String providerName) {
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
        return "AddPatientToProviderRequest{" +
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

        public AddPatientToProviderRequest build() {
            return new AddPatientToProviderRequest(patientId, providerName);
        }

    }
}
