package com.palominolabs.jersey.newrelic;
/*
* Copyright (c) 2012 Palomino Labs, Inc.
*/

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.api.model.AbstractResourceMethod;
import com.sun.jersey.api.model.AbstractSubResourceLocator;
import com.sun.jersey.core.util.FeaturesAndProperties;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Context;

import javax.servlet.http.HttpServletRequest;
import com.palominolabs.servlet.newrelic.NewRelicUnmappedThrowableFilter;

/**
 * Changes from original by Pieter Bos (pieter.bos@nedap.com)
 *
 * Adds resource filters to integrate New Relic into the Jersey invocation stack.
 */
public final class NewRelicResourceFilterFactory implements ResourceFilterFactory {

    private static final Logger logger = LoggerFactory.getLogger(NewRelicResourceFilterFactory.class);

    private HttpServletRequest threadLocalRequest;//this may not seem threadlocal, but it is really, due to jersey

    public NewRelicResourceFilterFactory(@Context HttpServletRequest request) {
        this.threadLocalRequest = request;
    }

    @Override
    public List<ResourceFilter> create(AbstractMethod am) {
        
        // documented to only be AbstractSubResourceLocator, AbstractResourceMethod, or AbstractSubResourceMethod
        if (am instanceof AbstractSubResourceLocator) {
            // not actually invoked per request, nothing to do
            logger.debug("Ignoring AbstractSubResourceLocator " + am);
            return null;
        } else if (am instanceof AbstractResourceMethod) {
            String transactionName = ResourceTransactionNamer.getTransactionName((AbstractResourceMethod) am);
            return Arrays.asList(new NewRelicTransactionNameResourceFilter(threadLocalRequest, null, transactionName),
                new NewRelicMappedThrowableResourceFilter());
        } else {
            logger.warn("Got an unexpected instance of " + am.getClass().getName() + ": " + am);
            return null;
        }
    }
}

