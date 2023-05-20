package com.nashss.se.yodaservice.lambda;

import com.nashss.se.yodaservice.dependency.DaggerServiceComponent;
import com.nashss.se.yodaservice.dependency.ServiceComponent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Class LambdaActivityRunner provides a standard way to run lambda activities and handle exceptions.
 * It uses dependency injection for the ServiceComponent required to handle the request.
 * A generic request object is accepted, and a generic response is produced.
 *
 * @param <TRequest>  the type of request object supplied
 * @param <TResult>  the type of result object to be returned
 */
public class LambdaActivityRunner<TRequest, TResult> {
    private ServiceComponent service;
    private final Logger log = LogManager.getLogger();

    /**
     * Handles running the activity and returning a LambdaResponse (either success or failure).
     * @param requestSupplier Provides the activity request.
     * @param handleRequest Runs the activity and provides a response.
     * @return A LambdaResponse
     */
    protected LambdaResponse runActivity(
            Supplier<TRequest> requestSupplier,
            BiFunction<TRequest, ServiceComponent, TResult> handleRequest) {
        log.info("runActivity");
        try {
            TRequest request = requestSupplier.get();
            ServiceComponent serviceComponent = getService();
            TResult result = handleRequest.apply(request, serviceComponent);
            return LambdaResponse.success(result);
        } catch (Exception e) {
            return LambdaResponse.error(e);
        }
    }

    /**
     * Retrieves the current instance of the service component. If it's null, a new instance is created.
     *
     * Uses Dagger for dependency injection of the ServiceComponent. This is a Singleton pattern implementation
     * to ensure that only one instance of ServiceComponent is used throughout the application.
     *
     * @return the instance of ServiceComponent
     */
    private ServiceComponent getService() {
        log.info("getService");
        if (service == null) {
            service = DaggerServiceComponent.create();
        }
        return service;
    }
}
