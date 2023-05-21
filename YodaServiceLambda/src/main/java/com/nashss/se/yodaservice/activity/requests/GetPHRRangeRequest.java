package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = GetAllPHRRequest.Builder.class)
public class GetPHRRangeRequest {

    private final String patientId;
    private final String from;
    private final String to;

    public GetPHRRangeRequest(String patientId, String from, String to) {
        this.patientId = patientId;
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getPatientId() {
        return this.patientId;
    }

    @Override
    public String toString() {
        return "GetPHRRangeRequest{" +
                "patientId='" + patientId + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
    public static Builder builder(){return new Builder();}
    @JsonPOJOBuilder
    public static class Builder {
        private String patientId;
        private String from;
        private String to;

        public Builder withPatientId(String patientId) {
            this.patientId = patientId;
            return this;
        }
        public Builder withFrom(String from) {
            this.from = from;
            return this;
        }
        public Builder withTo(String to) {
            this.to = to;
            return this;
        }

        public GetPHRRangeRequest build() {
            return new GetPHRRangeRequest(patientId, from, to);
        }
    }
}