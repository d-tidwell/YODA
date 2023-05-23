package com.nashss.se.yodaservice.lambda;

import com.nashss.se.yodaservice.activity.requests.GetPHRRangeRequest;
import com.nashss.se.yodaservice.activity.results.GetPHRRangeResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetPHRRangeLambda
        extends LambdaActivityRunner<GetPHRRangeRequest, GetPHRRangeResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetPHRRangeRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetPHRRangeRequest> input, Context context) {
        return super.runActivity(
                () -> input.fromPathAndQuery((path, query) -> GetPHRRangeRequest.builder()
                        .withPatientId(path.get("patientId"))
                        .withFrom(query.get("from"))
                        .withTo(query.get("to"))
                        .build()),
                (request, serviceComponent) ->
                        serviceComponent.provideGetPHRRangeActivity().handleRequest(request)
        );
    }

}

