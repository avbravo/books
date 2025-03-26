# File Download with Jax-RS

```java

@Path("/download")
public class DownloadResource {
 @GET
 @Produces(MediaType.APPLICATION_OCTET_STREAM)
 public Response download() {
 InputStream inStream = Thread.currentThread().getContextClassLoader().
getResourceAsStream("payara-logo.png");
 return Response.ok(inStream, MediaType.APPLICATION_OCTET_STREAM)
 .header("Content-Disposition", "attachment; filename=\"Payara
logo.png\"") // recommended, to specify the filename
 .build();
 }
}



```
