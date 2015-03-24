package com.palominolabs.servlet.newrelic;

import javax.annotation.concurrent.ThreadSafe;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

import com.newrelic.api.agent.NewRelic;

/**
 * Changes from original by Pieter Bos (pieter.bos@nedap.com)
 *
 * Informs New Relic about throwables that propagate up through the servlet layers (instead of, for instance, being
 * handled by Jersey).
 */
@ThreadSafe
public final class NewRelicUnmappedThrowableFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
        IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Throwable t) {
            NewRelic.noticeError(t);
            throw t;
        }
    }

    @Override
    public void destroy() {
        // no op
    }
}
