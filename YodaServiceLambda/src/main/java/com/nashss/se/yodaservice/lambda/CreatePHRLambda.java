package com.nashss.se.yodaservice.lambda;

import com.nashss.se.yodaservice.activity.requests.CreatePHRRequest;
import com.nashss.se.yodaservice.activity.results.CreatePHRResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class CreatePHRLambda
        extends LambdaActivityRunner<CreatePHRRequest, CreatePHRResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreatePHRRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreatePHRRequest> input, Context context) {
        CreatePHRRequest unauthenticatedRequest = input.fromBody(CreatePHRRequest.class);
        return super.runActivity(
            () ->
                input.fromPath(path ->
                        CreatePHRRequest.builder()
                                .withPatientId(path.get("patientId"))
                                .withProviderName(unauthenticatedRequest.getProviderName())
                                .withDate(unauthenticatedRequest.getDate())
                                .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideCreatePHRActivity().handleRequest(request)
        );
    }
}

