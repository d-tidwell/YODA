package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = GetPHRRequest.Builder.class)
public class GetPHRRequest {
    private final String phrId;

    public GetPHRRequest(String phrId) {
        this.phrId = phrId;
    }

    public String getPhrId() {
        return phrId;
    }

    @Override
    public String toString() {
        return "GetPHRRequest{" +
                "phrId='" + phrId + '\'' +
                '}';
    }
    //CHECKSTYLE:OFF:Builder
    public static Builder builder(){return new Builder();}

    @JsonPOJOBuilder
    public static class Builder {
        private String phrId;

        public Builder withPhrId(String phrId){
            this.phrId = phrId;
            return this;
        }

        public GetPHRRequest build(){return new GetPHRRequest(phrId);}
    }
}
