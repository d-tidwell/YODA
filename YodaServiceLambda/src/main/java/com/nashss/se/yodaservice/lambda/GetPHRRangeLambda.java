package com.nashss.se.yodaservice.lambda;

import com.nashss.se.yodaservice.activity.requests.GetPHRRangeRequest;
import com.nashss.se.yodaservice.activity.results.GetPHRRangeResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetPHRRangeLambda
        extends LambdaActivityRunner<GetPHRRangeRequest, GetPHRRangeResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetPHRRangeRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetPHRRangeRequest> input, Context context) {
        GetPHRRangeRequest unauthenticatedRequest = input.fromBody(GetPHRRangeRequest.class);
        return super.runActivity(
            () -> input.fromPath(path -> GetPHRRangeRequest.builder()
                    .withPatientId(path.get("patientId"))
                    .withFrom(unauthenticatedRequest.getFrom())
                    .withTo(unauthenticatedRequest.getTo())
                    .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideGetPHRRangeActivity().handleRequest(request)
        );
    }
}

