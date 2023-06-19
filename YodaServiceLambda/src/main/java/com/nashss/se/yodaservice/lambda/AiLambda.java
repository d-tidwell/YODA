package com.nashss.se.yodaservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.yodaservice.activity.requests.AiRequest;
import com.nashss.se.yodaservice.activity.results.AiResult;

public class AiLambda
        extends LambdaActivityRunner<AiRequest, AiResult>
        implements RequestHandler<AuthenticatedLambdaRequest<AiRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<AiRequest> input, Context context) {

        return super.runActivity(
                () -> input.fromPath(path -> AiRequest.builder()
                        .withPhrId(path.get("phrId"))
                        .withDate(path.get("date"))
                        .build()),
                (request, serviceComponent) ->
                        serviceComponent.provideAiActivity().handleRequest(request)
        );
    }
}
