package com.nashss.se.yodaservice.dependency;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.yodaservice.activity.*;

import com.nashss.se.yodaservice.activity.requests.EditPHRRequest;
import com.nashss.se.yodaservice.activity.requests.GetOpenPHRByProviderRequest;
import com.nashss.se.yodaservice.activity.requests.GetPresigneds3Request;
import com.nashss.se.yodaservice.dynamodb.AmazonS3AndTranscribeProviders;
import com.nashss.se.yodaservice.lambda.EditPHRLambda;
import com.nashss.se.yodaservice.lambda.GetPresignedAudioLambda;
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

    GetOpenPHRByProviderActivity provideGetOpenPHRByProviderActivity();

    GetAllPatientActivity provideGetAllPatientActivity();

    CreateProviderActivity provideCreateProviderActivity();

    GetPresignedAudioActivity provideGetPresignedAudioActivity();

    EditPHRActivity provideEditPHRActivity();
}
