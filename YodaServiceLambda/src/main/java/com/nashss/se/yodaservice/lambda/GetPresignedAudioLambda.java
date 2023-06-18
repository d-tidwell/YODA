package com.nashss.se.yodaservice.lambda;

import com.nashss.se.yodaservice.activity.requests.GetPresigneds3Request;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.yodaservice.activity.results.GetPresigneds3Result;

public class GetPresignedAudioLambda
        extends LambdaActivityRunner<GetPresigneds3Request, GetPresigneds3Result>
        implements RequestHandler<AuthenticatedLambdaRequest<GetPresigneds3Request>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetPresigneds3Request> input, Context context) {
        return super.runActivity(
                () ->
                    input.fromPathAndQuery((path, query) -> GetPresigneds3Request.builder()
                                    .withPhrId(query.get("phrId"))
                                    .build()),
                (request, serviceComponent) ->
                        serviceComponent.provideGetPresignedAudioActivity().handleRequest(request)
        );
    }
}
