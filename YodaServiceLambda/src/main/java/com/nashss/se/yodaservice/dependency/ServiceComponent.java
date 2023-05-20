package com.nashss.se.yodaservice.dependency;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.yodaservice.activity.AddPatientToProviderActivity;
import com.nashss.se.yodaservice.activity.CreatePatientActivity;
import com.nashss.se.yodaservice.activity.requests.AddPatientToProviderRequest;
import com.nashss.se.yodaservice.activity.requests.CreatePHRRequest;
import com.nashss.se.yodaservice.activity.requests.CreatePatientRequest;
import dagger.Component;

import javax.inject.Singleton;

/**
 * Dagger component for providing dependency injection in the Yoda MD Service.
 */
@Singleton
@Component(modules = {DaoModule.class, MetricsModule.class})
public interface ServiceComponent {

    AddPatientToProviderActivity provideAddPatientToProviderActivity();

    CreatePatientActivity provideCreatePatientActivity();

    CreatePHRRequest provideCreatePHRActivity();
}
