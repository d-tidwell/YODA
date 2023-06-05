package com.nashss.se.yodaservice.lambda;

import com.nashss.se.yodaservice.activity.requests.GetPresigneds3Request;
import com.nashss.se.yodaservice.activity.results.GetPresigneds3Result;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetPresigneds3Lambda
        extends LambdaActivityRunner<GetPresigneds3Request, GetPresigneds3Result>
        implements RequestHandler<AuthenticatedLambdaRequest<GetPresigneds3Request>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetPresigneds3Request> input, Context context) {
        return super.runActivity(
                () ->
                    input.fromPathAndQuery((path, query) -> GetPresigneds3Request.builder()
                                    .withFileName(path.get("filename"))
                                    .withPhrId(query.get("phrId"))
                                    .withDate(query.get("date"))
                                    .build()),
                (request, serviceComponent) ->
                        serviceComponent.provideGetPresigneds3Activity().handleRequest(request)
        );
    }
}

