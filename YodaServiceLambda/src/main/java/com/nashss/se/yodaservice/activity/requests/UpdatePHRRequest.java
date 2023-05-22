package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = UpdatePHRRequest.Builder.class)
public class UpdatePHRRequest {
    private final String status;
    private final String age;
    private final String phrId;

    public UpdatePHRRequest(String status, String age, String phrId) {
        this.status = status;
        this.age = age;
        this.phrId = phrId;
    }
    public String getStatus() {
        return status;
    }

    public String getAge() {
        return age;
    }

    public String getPhrId() {
        return this.phrId;
    }

    @Override
    public String toString() {
        return "UpdatePHRRequest{" +
                ", status='" + status + '\'' +
                ", age='" + age + '\'' +
                ", phrId='" + phrId + '\'' +
                '}';
    }
    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {

        private String status;
        private String age;
        private String phrId;


        public Builder withStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder withAge(String age) {
            this.age = age;
            return this;
        }

        public Builder withPhrId(String phrId){
            this.phrId = phrId;
            return this;
        }

        public UpdatePHRRequest build() {
            return new UpdatePHRRequest(status, age, phrId);
        }


    }
}
