
Converting JAX-RS parameters with ParamConverters
#jaxrs
wednesday, july 08, 2020
If you want JAX-RS to automatically convert parameters such as query params, path params, or others, you need to create a ParamConverter. Primitive types, strings, and types who define certain conversion methods, such as a valueOf(String) method, are automatically converted. Here’s how to define a converter for LocalDates.

One gotcha I ran into a few times — besides the fact that I helped specifying the standard in the expert group :-) — is that you need to register a LocalDateParamConverterProvider as JAX-RS @Provider which then forwards the parameter to the actual converter. This behavior differs from the MessageBodyWriter or ExceptionMapper types, for example.

The following two classes are required to automatically convert LocalDate types:

```java
import javax.ws.rs.ext.ParamConverter;
import java.time.LocalDate;

public class LocalDateConverter implements ParamConverter<LocalDate> {

    @Override
    public LocalDate fromString(String value) {
        if (value == null)
            return null;
        return LocalDate.parse(value);
    }

    @Override
    public String toString(LocalDate value) {
        if (value == null)
            return null;
        return value.toString();
    }

}

```

```java
package com.sebastian_daschner.coffee_shop;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;

@Provider
public class LocalDateParamConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType,
            Annotation[] annotations) {
        if (rawType.equals(LocalDate.class))
            return (ParamConverter<T>) new LocalDateConverter();
        return null;
    }

}

```
Once the converter and provider are registered, we can use the LocalDate type as parameter for our resource methods:

```java
@Path("test")
@ApplicationScoped
public class TestResource {

    @GET
    public String testIndex(@QueryParam("date") LocalDate date) {
        return "hello, " + date;
    }

    @GET
    @Path("{date}")
    public String test(@PathParam("date") LocalDate date) {
        return "hello, " + date;
    }

}

```
However, we’re already having conversations in the ongoing JAX-RS specification process to enable both automatic conversion for Java time types, as well as registering converters without the provider, so this solution might be simplified in the future.

Check out the GitHub repository for Jakarta RESTful Web Services and the linked resources to join the discussion.