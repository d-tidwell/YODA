package com.nashss.se.yodaservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.yodaservice.activity.requests.EditPHRRequest;
import com.nashss.se.yodaservice.activity.results.EditPHRResult;


public class EditPHRLambda
        extends LambdaActivityRunner<EditPHRRequest, EditPHRResult>
        implements RequestHandler<AuthenticatedLambdaRequest<EditPHRRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<EditPHRRequest> input, Context context) {

        EditPHRRequest unauthenticatedRequest = input.fromBody(EditPHRRequest.class);
        return super.runActivity(
                () -> input.fromPath(path -> EditPHRRequest.builder()
                        .withPhrId(path.get("phrId"))
                        .withText(unauthenticatedRequest.getText())
                        .build()),
                (request, serviceComponent) ->
                        serviceComponent.provideEditPHRActivity().handleRequest(request)
        );
    }
}