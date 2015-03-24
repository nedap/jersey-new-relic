package com.palominolabs.jersey.newrelic;

import com.sun.jersey.api.model.AbstractResource;
import com.sun.jersey.api.model.AbstractResourceMethod;
import com.sun.jersey.api.model.AbstractSubResourceMethod;
import com.sun.jersey.api.model.PathValue;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.Path;
import java.lang.annotation.Annotation;

import static org.junit.Assert.assertEquals;

public final class ResourceTransactionNamerImplTest {

    @Test
    public void testNullPathValue() {
        assertEquals("", ResourceTransactionNamer.getPathWithoutSurroundingSlashes(null));
    }

    @Test
    public void testPathValueLeadingSlash() {
        doPathValueTest("foo", "/foo");
    }

    @Test
    public void testPathValueTrailingSlash() {
        doPathValueTest("foo", "foo/");
    }

    @Test
    public void testPathValueLeadingAndTrailingSlash() {
        doPathValueTest("foo", "/foo/");
    }

    @Test
    public void testGetMetricIdClassWithPathMethodWithoutPath() {
        AbstractResource resource = new AbstractResource(FooResource.class, new PathValue("/res"));
        AbstractResourceMethod method =
            new AbstractResourceMethod(resource, null, Void.class, Void.class, "GET", new Annotation[]{});

        assertEquals("/res GET", ResourceTransactionNamer.getTransactionName(method));
    }

    @Test
    public void testGetMetricIdClassWithPathMethodWithPath() {
        AbstractResource resource = new AbstractResource(FooResource.class, new PathValue("/res"));
        AbstractResourceMethod method =
            new AbstractSubResourceMethod(resource, null, Void.class, Void.class, new PathValue("/meth"), "GET",
                new Annotation[]{});

        assertEquals("/res/meth GET", ResourceTransactionNamer.getTransactionName(method));
    }

    @Test
    public void testGetMetricIdClassWithoutPathMethodWithPath() {
        AbstractResource resource = new AbstractResource(FooResource.class, null);
        AbstractResourceMethod method =
            new AbstractSubResourceMethod(resource, null, Void.class, Void.class, new PathValue("/meth"), "GET",
                new Annotation[]{});

        assertEquals("/meth GET", ResourceTransactionNamer.getTransactionName(method));
    }

    private static void doPathValueTest(String expected, String input) {
        assertEquals(expected, ResourceTransactionNamer.getPathWithoutSurroundingSlashes(new PathValue(input)));
    }

    @Path("/foo")
    private static class FooResource {

    }
}
