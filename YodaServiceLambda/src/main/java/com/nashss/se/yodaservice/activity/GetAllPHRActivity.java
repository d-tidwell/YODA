package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.GetAllPHRRequest;
import com.nashss.se.yodaservice.activity.results.GetAllPHRResult;
import com.nashss.se.yodaservice.converters.ModelConverter;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import com.nashss.se.yodaservice.dynamodb.models.PHR;
import com.nashss.se.yodaservice.models.PHRModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class GetAllPHRActivity{

    private final Logger log = LogManager.getLogger();
    private final PatientDAO patientDAO;
    private final PHRDAO phrdao;

    @Inject
    public GetAllPHRActivity(PatientDAO patientDAO, PHRDAO phrdao) {
        this.patientDAO = patientDAO;
        this.phrdao = phrdao;
    }

    public GetAllPHRResult handleRequest(final GetAllPHRRequest request){
        patientDAO.getPatient(request.getPatientId());
        List<PHR> all = phrdao.getPhrsForPatient(request.getPatientId());
        List<PHRModel> results =ModelConverter.convertListPHRtoModels(all);
        return GetAllPHRResult.builder()
                .withPHRId(results)
                .build();
    }
}
