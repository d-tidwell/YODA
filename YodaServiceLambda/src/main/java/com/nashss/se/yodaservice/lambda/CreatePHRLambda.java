package com.nashss.se.yodaservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.yodaservice.activity.requests.CreatePHRRequest;
import com.nashss.se.yodaservice.activity.results.CreatePHRResult;

public class CreatePHRLambda
        extends LambdaActivityRunner<CreatePHRRequest, CreatePHRResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreatePHRRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreatePHRRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    CreatePHRRequest unauthenticatedRequest = input.fromBody(CreatePHRRequest.class);
                    return input.fromUserClaims(claims ->
                            CreatePHRRequest.builder()
                                    .withName(unauthenticatedRequest.getName())
                                    .withTags(unauthenticatedRequest.getTags())
                                    .withCustomerId(claims.get("email"))
                                    .withCustomerName(claims.get("name"))
                                    .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideCreatePHRActivity().handleRequest(request)
        );
    }
}