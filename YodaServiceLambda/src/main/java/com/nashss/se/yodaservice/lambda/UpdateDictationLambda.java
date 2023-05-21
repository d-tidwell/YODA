package com.nashss.se.yodaservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.yodaservice.activity.requests.UpdateDictationRequest;
import com.nashss.se.yodaservice.activity.results.UpdateDictationResult;
public class UpdateDictationLambda
        extends LambdaActivityRunner<UpdateDictationRequest, UpdateDictationResult>
        implements RequestHandler<AuthenticatedLambdaRequest<UpdateDictationRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<UpdateDictationRequest> input, Context context) {
        return super.runActivity(
                () -> input.fromPath(path -> UpdateDictationRequest.builder()
                        .withPhrId(path.get("phrId"))
                        .build()),
                (request, serviceComponent) ->
                        serviceComponent.provideUpdateDictationActivity().handleRequest(request)
        );
    }
}