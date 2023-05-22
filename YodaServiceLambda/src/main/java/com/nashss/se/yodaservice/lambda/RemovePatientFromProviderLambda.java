package com.nashss.se.yodaservice.lambda;

import com.nashss.se.yodaservice.activity.requests.RemovePatientFromProviderRequest;
import com.nashss.se.yodaservice.activity.results.RemovePatientFromProviderResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;



public class RemovePatientFromProviderLambda
        extends LambdaActivityRunner<RemovePatientFromProviderRequest, RemovePatientFromProviderResult>
        implements RequestHandler<AuthenticatedLambdaRequest<RemovePatientFromProviderRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(
            AuthenticatedLambdaRequest<RemovePatientFromProviderRequest> input, Context context) {
        return super.runActivity(
                () -> input.fromPath(path -> RemovePatientFromProviderRequest.builder()
                        .withPatientId(path.get("patientId"))
                        .withProviderName(path.get("providerId"))
                        .build()),
                (request, serviceComponent) ->
                        serviceComponent.provideRemovePatientFromProviderActivity().handleRequest(request)
        );
    }
}

