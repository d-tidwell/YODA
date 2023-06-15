package com.nashss.se.yodaservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.yodaservice.activity.requests.CreateProviderRequest;
import com.nashss.se.yodaservice.activity.results.CreateProviderResult;

public class CreateProviderLambda
    extends LambdaActivityRunner<CreateProviderRequest, CreateProviderResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateProviderRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateProviderRequest> input, Context context) {
            return super.runActivity(
                () -> input.fromPath(path ->
                CreateProviderRequest.builder()
                        .withProviderEmail(path.get("providerEmail"))
                        .withProviderName(path.get("providerName"))
                        .build()),
                (request, serviceComponent) ->
                    serviceComponent.provideCreateProviderActivity().handleRequest(request)
            );
    }
}
