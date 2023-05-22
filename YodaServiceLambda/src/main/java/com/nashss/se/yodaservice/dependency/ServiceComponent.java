package com.nashss.se.yodaservice.dependency;

import com.nashss.se.yodaservice.activity.*;

import com.nashss.se.yodaservice.activity.requests.RemovePatientFromProviderRequest;
import dagger.Component;

import javax.inject.Singleton;

/**
 * Dagger component for providing dependency injection in the Yoda MD Service.
 */
@Singleton
@Component(modules = { DaoModule.class, MetricsModule.class})
public interface ServiceComponent {

    AddPatientToProviderActivity provideAddPatientToProviderActivity();

    CreatePatientActivity provideCreatePatientActivity();

    CreatePHRActivity provideCreatePHRActivity();

    GetAllPHRActivity provideGetAllPHRActivity();

    GetPHRRangeActivity provideGetPHRRangeActivity();

    GetPresigneds3Activity provideGetPresigneds3Activity();

    UpdateDictationActivity provideUpdateDictationActivity();

    UpdatePHRActivity provideUpdatePHRActivity();

    GetProviderActivity provideGetProviderActivity();

    GetPatientActivity provideGetPatientActivity();

    GetPHRActivity provideGetPHRActivity();

    RemovePatientFromProviderActivity provideRemovePatientFromProviderActivity();
}
