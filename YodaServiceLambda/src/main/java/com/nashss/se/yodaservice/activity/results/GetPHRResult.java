package com.nashss.se.yodaservice.activity.results;

public class GetPHRResult {
    private final String patientId;
    private final String providerName;
    private final String date;
    private final String status;
    private final String age;
    private final String dictationId;


    public GetPHRResult(String patientId, String providerName, String date, String status, String age, String dictationId) {
        this.patientId = patientId;
        this.providerName = providerName;
        this.date = date;
        this.status = status;
        this.age = age;
        this.dictationId = dictationId;
    }

    @Override
    public String toString() {
        return "GetPHRResult{" +
                "patientId='" + patientId + '\'' +
                ", providerName='" + providerName + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", age='" + age + '\'' +
                ", dictationId='" + dictationId + '\'' +
                '}';
    }
    
    public static Builder builder(){return new Builder();}
    
    public static class Builder{
        private  String patientId;
        private  String providerName;
        private  String date;
        private  String status;
        private  String age;
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
        public Builder withAge(String age){this.age = age; return this;}
        public Builder withDicationId(String dicationId){this.dictationId = dicationId; return this;}

        public GetPHRResult build() {return new GetPHRResult(patientId, providerName, date, status, age, dictationId);}
    }
}
