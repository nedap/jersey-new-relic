package com.palominolabs.jersey.newrelic;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.newrelic.api.agent.NewRelic;

/**
 * Uses the name provided by {@link NewRelicResourceFilterFactory} to assign the New Relic transaction name for the
 * active request.
 */
@ThreadSafe
final class NewRelicTransactionNameResourceFilter implements ResourceFilter, ContainerRequestFilter {

    private final String transactionName;
    private final String category;

    /**
     * @param newRelicWrapper wrapper
     * @param category        new relic category
     * @param transactionName the transaction name that this filter will apply to all requests.
     */
    NewRelicTransactionNameResourceFilter(@Nullable String category, String transactionName) {
        this.category = category;
        this.transactionName = transactionName;
    }

    @Override
    public ContainerRequestFilter getRequestFilter() {
        return this;
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        // don't filter responses
        return null;
    }

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        NewRelic.setTransactionName(category, this.transactionName);
        return request;
    }
}
