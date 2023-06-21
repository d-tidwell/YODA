package com.nashss.se.yodaservice.lambda;

import com.nashss.se.yodaservice.exceptions.TranscribeActionException;
import com.nashss.se.yodaservice.activity.requests.UpdateDictationRequest;
import com.nashss.se.yodaservice.activity.results.UpdateDictationResult;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class UpdateDictationLambda
        extends LambdaActivityRunner<UpdateDictationRequest, UpdateDictationResult>
        implements RequestHandler<AuthenticatedLambdaRequest<UpdateDictationRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<UpdateDictationRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromPath(path -> UpdateDictationRequest.builder()
                    .withPhrId(path.get("PhrId"))
                    .withPhrDate(path.get("PhrDate"))
                    .withFileName(path.get("fileName"))
                    .withType(path.get("type"))
                    .build()),
            (request, serviceComponent) ->
            {
                try {
                    return serviceComponent.provideUpdateDictationActivity().handleRequest(request);
                } catch (TranscribeActionException e) {
                    throw new RuntimeException("Transcription Service Error",e);
                }
            }
        );
    }
}

