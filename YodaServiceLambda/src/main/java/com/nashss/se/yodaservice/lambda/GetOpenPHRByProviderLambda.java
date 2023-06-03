package com.nashss.se.yodaservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.yodaservice.activity.requests.GetOpenPHRByProviderRequest;
import com.nashss.se.yodaservice.activity.results.GetOpenPHRByProviderResult;

public class GetOpenPHRByProviderLambda
        extends LambdaActivityRunner<GetOpenPHRByProviderRequest, GetOpenPHRByProviderResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetOpenPHRByProviderRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetOpenPHRByProviderRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromPath(path -> GetOpenPHRByProviderRequest.builder()
                                .withProviderName(path.get("providerName"))
                                .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideGetOpenPHRByProviderActivity().handleRequest(request)
        );
    }
}