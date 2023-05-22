package com.nashss.se.yodaservice.activity.results;

public class GetPHRResult {
    private final String patientId;
    private final String providerName;
    private final String date;
    private final String status;
    private final String dictationId;


    public GetPHRResult(String patientId, String providerName, String date, String status, String dictationId) {
        this.patientId = patientId;
        this.providerName = providerName;
        this.date = date;
        this.status = status;
        this.dictationId = dictationId;
    }

    @Override
    public String toString() {
        return "GetPHRResult{" +
                "patientId='" + patientId + '\'' +
                ", providerName='" + providerName + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", dictationId='" + dictationId + '\'' +
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
        public GetPHRResult build() {return new GetPHRResult(patientId, providerName, date, status, dictationId);}
    }
}
