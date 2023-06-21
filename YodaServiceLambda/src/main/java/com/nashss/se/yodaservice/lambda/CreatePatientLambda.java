package com.nashss.se.yodaservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.yodaservice.activity.requests.CreatePatientRequest;
import com.nashss.se.yodaservice.activity.results.CreatePatientResult;

public class CreatePatientLambda
    extends LambdaActivityRunner<CreatePatientRequest, CreatePatientResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreatePatientRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreatePatientRequest> input, Context context) {
        CreatePatientRequest unauthenticatedRequest = input.fromBody(CreatePatientRequest.class);
        return super.runActivity(
                () -> CreatePatientRequest.builder()
                        .withPatientName(unauthenticatedRequest.getPatientName())
                        .withPatientAge(unauthenticatedRequest.getPatientAge())
                        .build(),
                (request, serviceComponent) ->
                        serviceComponent.provideCreatePatientActivity().handleRequest(request)
        );
    }
}

