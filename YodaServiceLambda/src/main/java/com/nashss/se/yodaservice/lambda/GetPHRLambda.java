package com.nashss.se.yodaservice.lambda;

import com.nashss.se.yodaservice.activity.requests.GetPHRRequest;
import com.nashss.se.yodaservice.activity.results.GetPHRResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetPHRLambda
        extends LambdaActivityRunner<GetPHRRequest, GetPHRResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetPHRRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetPHRRequest> input, Context context) {
        GetPHRRequest unauthenticatedRequest = input.fromBody(GetPHRRequest.class);
        return super.runActivity(
            () -> input.fromPath(path ->
                            GetPHRRequest.builder()
                                    .withPhrId(path.get("phrId"))
                                    .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideGetPHRActivity().handleRequest(request)
        );
    }
}

