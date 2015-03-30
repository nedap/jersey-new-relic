package com.palominolabs.jersey.newrelic;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.newrelic.api.agent.NewRelic;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Changes from original by Pieter Bos (pieter.bos@nedap.com)
 * Uses the name provided by {@link NewRelicResourceFilterFactory} to assign the New Relic transaction name for the
 * active request.
 */
@ThreadSafe
final class NewRelicTransactionNameResourceFilter implements ResourceFilter, ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(NewRelicTransactionNameResourceFilter.class);

    private final String transactionName;
    private final String category;
    private HttpServletRequest threadLocalRequest;

    /**
     * @param newRelicWrapper wrapper
     * @param category        new relic category
     * @param transactionName the transaction name that this filter will apply to all requests.
     */
    NewRelicTransactionNameResourceFilter(HttpServletRequest request, @Nullable String category, String transactionName) {
        this.category = category;
        this.transactionName = transactionName;
        this.threadLocalRequest = request;
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
        try {
            NewRelic.setTransactionName(category, "/" + ResourceTransactionNamer.getPathWithoutSurroundingSlashes(threadLocalRequest.getContextPath()) 
                + transactionName);
        } catch(Exception e) {
            logger.error("error naming transaction for new relic", e);
        }
        return request;
    }
}
