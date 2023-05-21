package com.nashss.se.yodaservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.yodaservice.activity.requests.GetProviderRequest;
import com.nashss.se.yodaservice.activity.results.GetProviderResult;

public class GetProviderLambda
        extends LambdaActivityRunner<GetProviderRequest, GetProviderResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetProviderRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetProviderRequest> input, Context context) {
        return super.runActivity(
                () -> input.fromPath(path -> GetProviderRequest.builder()
                        .withProviderId(path.get("providerId"))
                        .build()),
                (request, serviceComponent) ->
                        serviceComponent.provideGetProviderActivity().handleRequest(request)
        );
    }
}