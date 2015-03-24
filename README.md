[New Relic](http://newrelic.com/)'s built-in servlet request transaction naming doesn't do a very good job of handling JAX-RS requests, so this library provides some [Jersey 1](https://jersey.java.net/) helpers to get better New Relic transaction names. It also allows you to track both mapped and un-mapped exceptions with New Relic.

# Fork without Guice

The original library depended on the usage of Guice. This fork has Guice removed. This means it can be used if you only use a web.xml, or servlet 3.0, or basically anything where you do not use Guice.

Also some simplifications, and for now no possibility of setting the category until I figure out how to retrieve the Jersey properties from a ResourceFilterFactory.

# Installation

You need to register both a `javax.servlet.Filter` ([`NewRelicUnmappedThrowableFilter`](https://github.com/palominolabs/jersey-new-relic/blob/master/src/main/java/com/palominolabs/servlet/newrelic/NewRelicUnmappedThrowableFilter.java)) and a Jersey `ResourceFilterFactory` ([`NewRelicResourceFilterFactory`](https://github.com/palominolabs/jersey-new-relic/blob/master/src/main/java/com/palominolabs/jersey/newrelic/NewRelicResourceFilterFactory.java)).

Here's how to register the servlet filter using Guice Servlet:
```
// in your ServletModule
filter("/*").through(NewRelicUnmappedThrowableFilter.class);
```

And the Jersey filter factory:
```
// in your ServletModule
Map<String, String> initParams = new HashMap<>();
initParams.put(ResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES,
    NewRelicResourceFilterFactory.class.getCanonicalName());

bind(GuiceContainer.class);
serve("/*").with(GuiceContainer.class, initParams);
```
