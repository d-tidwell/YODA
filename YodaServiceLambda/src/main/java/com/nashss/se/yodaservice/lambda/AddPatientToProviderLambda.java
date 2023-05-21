package com.nashss.se.yodaservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.yodaservice.activity.requests.AddPatientToProviderRequest;
import com.nashss.se.yodaservice.activity.results.AddPatientToProviderResult;


public class AddPatientToProviderLambda
        extends LambdaActivityRunner<AddPatientToProviderRequest, AddPatientToProviderResult>
        implements RequestHandler<AuthenticatedLambdaRequest<AddPatientToProviderRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<AddPatientToProviderRequest> input, Context context) {
        return super.runActivity(
                () -> input.fromPath(path -> AddPatientToProviderRequest.builder()
                        .withPatientId(path.get("patientId"))
                        .withProviderName(path.get("providerId"))
                        .build()),
                (request, serviceComponent) ->
                        serviceComponent.provideAddPatientToProviderActivity().handleRequest(request)
        );
    }
}