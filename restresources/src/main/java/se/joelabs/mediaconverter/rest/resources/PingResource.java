
package se.joelabs.mediaconverter.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/ping")
public class PingResource {
    @GET
    @Produces("text/plain")
    @Path("/")
    public String ping() {
        return "MConvREST says: Hi there!";
    }
}
