#Pasar Fechas
[Pasar Fechas](https://zenidas.wordpress.com/recipes/configurable-date-format-in-jax-rs-as-queryparam/)


Goal
Control the format of a query param in a JAX-RS endpoint whose type is Date
Description
JAX-RS dictates that parameter unbinding is done in one of two ways:

The parameter bean has a public constructor that accepts a String (this is the case of the java.util.Date class we will be dealing with in this recipe).
The parameter has a static valueOf(String) method.
According to the previous, the way a date query param is parsed in a REST endpoint using JAX-RS is through the deprecated constructor of Date receiving a String parameter, which, I would say, kind “smells”…

I didn’t like that approach nor others I read about in the Internet that would advise a change in the Java implementation to use a String parameter and to parse the date within the corresponding method.

In this recipe, we will implement a solution I would consider very elegant and flexible, with the use of annotations to control the date format.

How to
Declaration of the two annotations, one for a date, discarding the time part, and another for both the date and the time (notice that the annotations may be used in method parameters, and in class fields – this is for the situation we want to use a complete object as @BeanParam in our REST endpoint method).

The @DateFormat annotation is defined as:


```
package com.linkare.myproject.rest.annotations;
 
// imports
 
@Retention(RUNTIME)
@Target({ FIELD, PARAMETER })
public @interface DateFormat {
 
  public static final String DEFAULT_DATE = "yyyy-MM-dd";
 
  String value() default DEFAULT_DATE;
}
```
Advertisements

REPORT THIS AD

Where the @DateTimeFormat annotation is very similar:

```
package com.linkare.myproject.rest.annotations;
 
// imports
 
@Retention(RUNTIME)
@Target({ FIELD, PARAMETER })
public @interface DateTimeFormat {
 
  public static final String DEFAULT_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZ";
 
  String value() default DEFAULT_DATE_TIME;
}
```

Next, we implement our ParamConverter that will deal with the conversion of the String we get in our requests into our Date object. Notice that the implementation is prepared to deal with the default situation where the used does not specify any of the previously defined annotations. In that case, our param converter will use a default date format to parse the submitted String into our Date object:


```
package com.linkare.myproject.rest.paramconverter;
 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
 
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ParamConverter;
 
import com.linkare.myproject.rest.annotations.DateFormat;
import com.linkare.myproject.rest.annotations.DateTimeFormat;
 
public class DateParameterConverter implements ParamConverter<Date> {
 
  public static final String DEFAULT_FORMAT = DateTimeFormat.DEFAULT_DATE_TIME;
 
  private DateTimeFormat customDateTimeFormat;
  private DateFormat customDateFormat;
 
  public void setCustomDateFormat(DateFormat customDateFormat) {
    this.customDateFormat = customDateFormat;
  }
 
  public void setCustomDateTimeFormat(DateTimeFormat
    customDateTimeFormat) {
    this.customDateTimeFormat = customDateTimeFormat;
  }
 
  @Override
  public Date fromString(String string) {
    String format = DEFAULT_FORMAT;
    if (customDateFormat != null) {
      format = customDateFormat.value();
    } else if (customDateTimeFormat != null) {
      format = customDateTimeFormat.value();
    }
 
    final SimpleDateFormat simpleDateFormat = new
      SimpleDateFormat(format);
 
    try {
      return simpleDateFormat.parse(string);
    } catch (ParseException ex) {
      throw new WebApplicationException(ex);
    }
  }
 
  @Override
  public String toString(Date date) {
    return new SimpleDateFormat(DEFAULT_FORMAT).format(date);
  }
}
```
Advertisements

REPORT THIS AD

And finally, we need to register our ParamConverter so that it may be used, as follows:

```
package com.linkare.myproject.rest.paramconverter;
 
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Date;
 
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
 
import com.linkare.myproject.rest.annotations.DateFormat;
import com.linkare.myproject.rest.annotations.DateTimeFormat;
 
@Provider
public class DateParameterConverterProvider
  implements ParamConverterProvider {
 
  @SuppressWarnings("unchecked")
  @Override
  public <T> ParamConverter<T> getConverter(final Class<T> rawType,
    final Type genericType, final Annotation[] annotations) {
    if (Date.class.equals(rawType)) {
      final DateParameterConverter dateParameterConverter =
        new DateParameterConverter();
 
      for (Annotation annotation : annotations) {
        if (DateTimeFormat.class.equals(
          annotation.annotationType())) {
      dateParameterConverter.
            setCustomDateTimeFormat((DateTimeFormat) annotation);
        } else if (DateFormat.class.equals(
          annotation.annotationType())) {
          dateParameterConverter.
            setCustomDateFormat((DateFormat) annotation);
        }
      }
      return (ParamConverter<T>) dateParameterConverter;
    }
    return null;
  }
}
```
Advertisements

REPORT THIS AD

To test the previous implementation, we may define a test class as follows:


```
@Path("datetests")
public class DateTestsRestController extends RestController {
 
  @Path("test1")
  @GET
  public Date test1(@QueryParam("myDate") @DateFormat final Date myDate) {
    return myDate;
  }
 
  @Path("test2")
  @GET
  public Date test2(@QueryParam("myDate")
    @DateFormat("yyyy/MM/dd") final Date myDate) {
    return myDate;
  }
 
  @Path("test3")
  @GET
  public Date test3(@QueryParam("myDate")
    @DateTimeFormat final Date myDate) {
    return myDate;
  }
 
  @Path("test4")
  @GET
  public Date test4(@QueryParam("myDate")
    @DateTimeFormat("yyyy/MM/dd HH:mm") final Date myDate) {
    return myDate;
  }
 
  @Path("test5")
  @GET
  public MyDateTest test5(@BeanParam final MyDateTest myDateTeste) {
    return myDateTeste;
  }
 
  public static class MyDateTest {
 
    @QueryParam("myDate")
    @DateTimeFormat("dd-MM-yyyy HH:mm")
    private Date myDate;
 
    public Date getMyDate() {
      return myDate;
    }
 
    public void setMyDate(Date myDate) {
      this.myDate = myDate;
    }
  }
}
```

```
REQUEST 1  - .../datetests/test1?myDate=2010-01-01
RESPONSE 1 - "2010-01-01T00:00:00.000+0000"

REQUEST 2  - .../datetests/test2?myDate=2010/01/01
RESPONSE 2 - "2010-01-01T00:00:00.000+0000"

REQUEST 3  - .../datetests/test3?myDate=2010-01-01T08:30:00.000+0000
RESPONSE 3 - "2010-01-01T08:30:00.000+0000"

REQUEST 4  - .../datetests/test4?myDate=2010/01/01 08:30
RESPONSE 4 - "2010-01-01T08:30:00.000+0000"

REQUEST 5  - .../datetests/test5?myDate=23-03-1982 18:23
RESPONSE 5 - { "myDate": "1982-03-23T18:23:00.000+0000" }
```