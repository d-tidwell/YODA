package com.nashss.se.yodaservice.activity.results;

import com.nashss.se.yodaservice.models.PatientModel;

import java.util.List;
//CHECKSTYLE:OFF:Builder
public class GetAllPatientResult {
    private final List<PatientModel> patients;

    public List<PatientModel> getPatients() {
        return patients;
    }

    public GetAllPatientResult(List<PatientModel> patients) {
        this.patients = patients;
    }

    @Override
    public String toString() {
        return "GetAllPatientResult{" +
                "patients=" + patients +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<PatientModel> patients;

        public Builder withPatients(List<PatientModel> patients){
            this.patients = patients;
             return this;
        }

        public GetAllPatientResult build() {return new GetAllPatientResult(patients); }
    }
}
