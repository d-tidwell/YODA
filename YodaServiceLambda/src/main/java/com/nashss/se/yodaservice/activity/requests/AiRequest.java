package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = AiRequest.Builder.class)
public class AiRequest {

    private final String phrId;
    private final String date;

    private AiRequest(String phrId, String date) {
        this.phrId = phrId;
        this.date = date;
    }

    public String getPhrId() {
        return this.phrId;
    }

    public String getDate() {
        return this.date;
    }

    @Override
    public String toString() {
        return "AiRequest{" +
                "phrId='" + phrId + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String phrId;
        private String date;

        public Builder withPhrId(String phrId) {
            this.phrId = phrId;
            return this;
        }

        public Builder withDate(String date) {
            this.date = date;
            return this;
        }

        public AiRequest build() {
            return new AiRequest(phrId, date);
        }
    }
}
