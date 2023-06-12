package com.nashss.se.yodaservice.activity.results;

public class GetPHRResult {
    private final String patientId;
    private final String providerName;
    private final String date;
    private final String status;
    private final String dictationId;

    private final String comprehendData;

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

    public String getDictationId() {
        return dictationId;
    }

    public String getComprehendData() {
        return comprehendData;
    }

    public GetPHRResult(String patientId, String providerName, String date, String status, String dictationId, String comprehendData) {
        this.patientId = patientId;
        this.providerName = providerName;
        this.date = date;
        this.status = status;
        this.dictationId = dictationId;
        this.comprehendData = comprehendData;
    }

    @Override
    public String toString() {
        return "GetPHRResult{" +
                "patientId='" + patientId + '\'' +
                ", providerName='" + providerName + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", dictationId='" + dictationId + '\'' +
                ", comprehendData='" + comprehendData + '\'' +
                '}';
    }
    //CHECKSTYLE:OFF:Builder
    public static Builder builder(){return new Builder();}
    
    public static class Builder{
        private  String patientId;
        private  String providerName;
        private  String date;
        private  String status;
        private  String dictationId;
        private  String comprehendData;

        public Builder withPatientId(String patientId){
            this.patientId = patientId;
            return this;
        }
        public Builder withProviderName(String providerName){
            this.providerName = providerName;
            return this;
        }
        public Builder withDate(String date){this.date = date; return this;}
        public Builder withStatus(String status){this.status = status; return this;}
        public Builder withDictationId(String dictationId){this.dictationId = dictationId; return this;}
        public Builder withComprehendData(String comp){this.comprehendData = comp; return this;}
        public GetPHRResult build() {return new GetPHRResult(patientId, providerName, date, status, dictationId, comprehendData);}
    }
}
