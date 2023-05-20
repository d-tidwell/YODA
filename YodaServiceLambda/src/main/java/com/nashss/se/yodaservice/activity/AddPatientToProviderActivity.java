package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.AddPatientToProviderRequest;
import com.nashss.se.yodaservice.activity.results.AddPatientToProviderResult;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

//public class AddPatientToProviderActivity {
//
//    private final Logger log = LogManager.getLogger();
//
//    private final PatientDAO patientDAO;
//
//    private final ProviderDAO providerDAO;
//
//    @Inject
//    public AddPatientToProviderActivity(PatientDAO patientDAO, ProviderDAO providerDAO) {
//        this.patientDAO = patientDAO;
//        this.providerDAO = providerDAO;
//    }
//
//    public AddPatientToProviderResult handleRequest(final AddPatientToProviderRequest request){
//
//        return AddPatientToProviderResult.builder()
//                .build();
//    }
//}
