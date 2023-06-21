package com.nashss.se.yodaservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
@JsonDeserialize(builder = CreatePatientRequest.Builder.class)
public class CreatePatientRequest {

    private final String patientName;
    private final String patientAge;
    private final String sex;
    private final String address;
    private final String phoneNumber;

    private CreatePatientRequest(String patientName, String patientAge, String sex, String address, String phoneNumber) {
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.sex = sex;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public String getSex() {
        return sex;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "CreatePatientRequest{" +
                "patientName='" + patientName + '\'' +
                ", patientAge='" + patientAge + '\'' +
                ", sex='" + sex + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String patientName;
        private String patientAge;
        private String sex;
        private String address;
        private String phoneNumber;

        public Builder withPatientName(String patientName) {
            this.patientName = patientName;
            return this;
        }

        public Builder withPatientAge(String patientAge) {
            this.patientAge = patientAge;
            return this;
        }

        public Builder withSex(String sex) {
            this.sex = sex;
            return this;
        }

        public Builder withAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder withPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public CreatePatientRequest build() {
            return new CreatePatientRequest(patientName, patientAge, sex, address, phoneNumber);
        }
    }
}


