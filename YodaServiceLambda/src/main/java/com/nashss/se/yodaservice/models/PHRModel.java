package com.nashss.se.yodaservice.models;

import java.util.Objects;

public class PHRModel {
    private String phrId;
    private String patientId;
    private String providerName;
    private String date;
    private String status;
    private String comprehendData;
    private String transcription;

    public PHRModel(String phrId, String patientId, String providerName, String date, String status,
                    String comprehendData, String transcription) {
        this.phrId = phrId;
        this.patientId = patientId;
        this.providerName = providerName;
        this.date = date;
        this.status = status;
        this.comprehendData = comprehendData;
        this.transcription = transcription;
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

    public String getComprehendData() { return comprehendData; }

    public String getTranscription() {return transcription;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PHRModel)) return false;
        PHRModel phrModel = (PHRModel) o;
        return getPhrId().equals(phrModel.getPhrId()) && getPatientId().equals(phrModel.getPatientId()) &&
                getProviderName().equals(phrModel.getProviderName()) && getDate().equals(phrModel.getDate()) &&
                getStatus().equals(phrModel.getStatus()) &&
                Objects.equals(getComprehendData(), phrModel.getComprehendData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPhrId(), getPatientId(), getProviderName(), getDate(), getStatus(), getComprehendData());
    }

    @Override
    public String toString() {
        return "PHRModel{" +
                "phrId='" + phrId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", providerName='" + providerName + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", comprehendData='" + comprehendData + '\'' +
                ", transcription='" + transcription + '\'' +
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
        private String comprehendData;
        private String transcription;

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

        public Builder comprehendData(String comprehendData) {
            this.comprehendData = comprehendData;
            return this;
        }

        public Builder transcription(String transcription) {
            this.transcription = transcription;
            return this;
        }

        public PHRModel build() {
            return new PHRModel(phrId, patientId, providerName, date, status, comprehendData, transcription);
        }

    }

}

