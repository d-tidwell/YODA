package com.nashss.se.yodaservice.models;

import java.util.Objects;

public class PHRModel {
    private String phrId;
    private String patientId;
    private String providerName;
    private String date;
    private String status;

    public PHRModel(String phrId, String patientId, String providerName, String date, String status) {
        this.phrId = phrId;
        this.patientId = patientId;
        this.providerName = providerName;
        this.date = date;
        this.status = status;
    }
    public PHRModel(){}

    public String getPhrId() {
        return phrId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PHRModel)) {
            return false;
        }
        PHRModel phrModel = (PHRModel) o;
        return getPhrId().equals(phrModel.getPhrId()) && getPatientId().equals(phrModel.getPatientId()) &&
                getProviderName().equals(phrModel.getProviderName()) && getDate().equals(phrModel.getDate()) &&
                getStatus().equals(phrModel.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPhrId(), getPatientId(), getProviderName(), getDate(), getStatus());
    }

    @Override
    public String toString() {
        return "PHRModel{" +
                "phrId='" + phrId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", providerName='" + providerName + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String phrId;
        private String patientId;
        private String providerName;
        private String date;
        private String status;

        public Builder phrId(String phrId) {
            this.phrId = phrId;
            return this;
        }

        public Builder patientId(String patientId) {
            this.patientId = patientId;
            return this;
        }

        public Builder providerName(String providerName) {
            this.providerName = providerName;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public PHRModel build() {
            return new PHRModel(phrId, patientId, providerName, date, status);
        }
    }

}

