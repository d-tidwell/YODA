package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = EditPHRRequest.Builder.class)
public class EditPHRRequest {

    private final String phrId;
    private final String text;

    private EditPHRRequest(String phrId, String text) {
        this.phrId = phrId;
        this.text= text;
    }

    public String getPhrId() {
        return this.phrId;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public String toString() {
        return "EditPHRRequest{" +
                "phrId='" + phrId + '\'' +
                "text='" + text + '\'' +
                '}';
    }
    //CHECKSTYLE:OFF:Builder
    public static Builder builder(){return new Builder(); }

    @JsonPOJOBuilder
    public static class Builder {
        private String phrId;
        private String text;

        public Builder withPhrId(String phrId) {
            this.phrId = phrId;
            return this;
        }
        public Builder withText(String text) {
            this.text = text;
            return this;
        }

        public EditPHRRequest build() {
            return new EditPHRRequest(phrId, text);
        }
    }
}

