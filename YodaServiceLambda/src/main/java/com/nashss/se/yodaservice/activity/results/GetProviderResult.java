package com.nashss.se.yodaservice.activity.results;

import java.util.List;

public class GetProviderResult {

    private final String  name;
    private final String medicalSpecialty;
    private final List<String> pendingPatients;

    public GetProviderResult(String name, String medicalSpecialty, List<String> pendingPatients) {
        this.name = name;
        this.medicalSpecialty = medicalSpecialty;
        this.pendingPatients = pendingPatients;
    }
    public String getName() {
        return name;
    }

    public String getMedicalSpecialty() {
        return medicalSpecialty;
    }

    public List<String> getPendingPatients() {
        return pendingPatients;
    }


    @Override
    public String toString() {
        return "GetProviderResult{" +
                "name='" + name + '\'' +
                ", medicalSpecialty='" + medicalSpecialty + '\'' +
                ", pendingPatients=" + pendingPatients +
                '}';
    }
    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {return new Builder();}

    public static class Builder {
        private  String  name;
        private  String medicalSpecialty;
        private  List<String> pendingPatients;

        public Builder withName(String name){
            this.name = name;
            return this;
        }
        public Builder withMedicalSpecialty(String medicalSpecialty){
            this.medicalSpecialty = medicalSpecialty;
            return this;
        }
        public Builder withPendingPatients(List<String> pendingPatients){
            this.pendingPatients = List.copyOf(pendingPatients);
            return this;
        }

        public GetProviderResult build() { return new GetProviderResult(name, medicalSpecialty, pendingPatients);}
    }
}
