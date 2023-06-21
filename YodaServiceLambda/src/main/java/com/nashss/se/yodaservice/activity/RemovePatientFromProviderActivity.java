package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.requests.RemovePatientFromProviderRequest;
import com.nashss.se.yodaservice.activity.results.RemovePatientFromProviderResult;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.ProviderDAO;
import com.nashss.se.yodaservice.dynamodb.models.Patient;
import com.nashss.se.yodaservice.dynamodb.models.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class RemovePatientFromProviderActivity {

    private final Logger log = LogManager.getLogger();

    private final PatientDAO patientDAO;

    private final ProviderDAO providerDAO;

    @Inject
    public RemovePatientFromProviderActivity(PatientDAO patientDAO, ProviderDAO providerDAO) {
        this.patientDAO = patientDAO;
        this.providerDAO = providerDAO;
    }

    public RemovePatientFromProviderResult handleRequest(final RemovePatientFromProviderRequest request) {

        Patient patient = patientDAO.getPatient(request.getPatientId());
        Optional<Provider> provider = providerDAO.getProvider(request.getProviderName());

        if(provider.isPresent()){
            Provider optionalProvider = provider.get();
            List<String> pendingPatients = optionalProvider.getPendingPatients();
            if(pendingPatients != null){
                try{
                    int position = Integer.parseInt(request.getPosition());
                    if(position >= 0 && position < pendingPatients.size()){
                        String id = pendingPatients.remove(position);
                        if(id.equals(patient.getPatientId())){
                            optionalProvider.setPendingPatients(pendingPatients);
                            providerDAO.updateProvider(optionalProvider);
                            return RemovePatientFromProviderResult.builder()
                                    .withSuccess(true)
                                    .build();
                        } else {
                            throw new NoSuchElementException("Names did not match from queue. aborted");
                        }
                    } else {
                        throw new IndexOutOfBoundsException("Requested Patient is outside of Providers Queue");
                    }
                } catch(NumberFormatException e){
                   throw new IllegalArgumentException("Only positive integers of Provider Queue Size are Valid.");
                }
            } else {
                throw new IndexOutOfBoundsException("Patient Queue is empty already");
            }
        } else {
           throw new NoSuchElementException("Provider does not exists in record");
        }

    }
    
}
