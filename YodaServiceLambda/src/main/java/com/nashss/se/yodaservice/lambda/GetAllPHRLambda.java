package com.nashss.se.yodaservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.yodaservice.activity.requests.GetAllPHRRequest;
import com.nashss.se.yodaservice.activity.results.GetAllPHRResult;

public class GetAllPHRLambda
        extends LambdaActivityRunner<GetAllPHRRequest, GetAllPHRResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetAllPHRRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetAllPHRRequest> input, Context context) {
        return super.runActivity(
                () -> input.fromPath(path -> GetAllPHRRequest.builder()
                                    .withPatientId(path.get("patientId"))
                                    .build()),
                (request, serviceComponent) ->
                        serviceComponent.provideGetAllPHRActivity().handleRequest(request)
        );
    }
}