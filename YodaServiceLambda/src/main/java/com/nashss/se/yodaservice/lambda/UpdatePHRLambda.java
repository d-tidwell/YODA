package com.nashss.se.yodaservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.yodaservice.activity.requests.UpdatePHRRequest;
import com.nashss.se.yodaservice.activity.results.UpdatePHRResult;


public class UpdatePHRLambda
        extends LambdaActivityRunner<UpdatePHRRequest, UpdatePHRResult>
        implements RequestHandler<AuthenticatedLambdaRequest<UpdatePHRRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<UpdatePHRRequest> input, Context context) {

        UpdatePHRRequest unauthenticatedRequest = input.fromBody(UpdatePHRRequest.class);
        return super.runActivity(
            () -> input.fromPath(path -> UpdatePHRRequest.builder()
                                .withPhrId(path.get("phrId"))
                                .withStatus(unauthenticatedRequest.getStatus())
                                .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideUpdatePHRActivity().handleRequest(request)
        );
    }
}