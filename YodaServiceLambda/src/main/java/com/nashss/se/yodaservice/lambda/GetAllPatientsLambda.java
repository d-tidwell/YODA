package com.nashss.se.yodaservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.yodaservice.activity.requests.GetAllPatientsRequest;
import com.nashss.se.yodaservice.activity.results.GetAllPatientResult;


public class GetAllPatientsLambda extends LambdaActivityRunner<GetAllPatientsRequest, GetAllPatientResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetAllPatientsRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetAllPatientsRequest> input, Context context) {
        return super.runActivity(
                () -> input.fromPath(path -> GetAllPatientsRequest.builder()
                        .withProviderName(path.get("providerName"))
                        .build()),
                (request, serviceComponent) -> serviceComponent.provideGetAllPatientActivity().handleRequest()
        );
    }
}



