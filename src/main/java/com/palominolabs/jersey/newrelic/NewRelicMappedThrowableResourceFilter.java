package com.palominolabs.jersey.newrelic;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

import javax.annotation.concurrent.ThreadSafe;

import com.newrelic.api.agent.NewRelic;

/**
 * Changes from original by Pieter Bos (pieter.bos@nedap.com)
 *
 * Informs New Relic about mapped throwables that are being handled by Jersey rather than propagated up the servlet
 * handling chain.
 */
@ThreadSafe
final class NewRelicMappedThrowableResourceFilter implements ResourceFilter, ContainerResponseFilter {


    NewRelicMappedThrowableResourceFilter() {
    }

    @Override
    public ContainerRequestFilter getRequestFilter() {
        // don't filter requests
        return null;
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        return this;
    }

    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        Throwable mappedThrowable = response.getMappedThrowable();
        if (mappedThrowable != null) {
            NewRelic.noticeError(mappedThrowable);
        }
        return response;
    }
}
