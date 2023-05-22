package com.nashss.se.yodaservice.lambda;

import com.nashss.se.yodaservice.activity.requests.GetPatientRequest;
import com.nashss.se.yodaservice.activity.results.GetPatientResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetPatientLambda
        extends LambdaActivityRunner<GetPatientRequest, GetPatientResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetPatientRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetPatientRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromPath(path -> GetPatientRequest.builder()
                    .withPatientId(path.get("patientId"))
                    .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideGetPatientActivity().handleRequest(request)
        );
    }
}
