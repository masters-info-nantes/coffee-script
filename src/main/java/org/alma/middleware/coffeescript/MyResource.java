package org.alma.middleware.coffeescript;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("coffeebackend")
public class MyResource {

    final static String authServer = "http://1.1.1.1/";
    final static String apiKey = "azertyuiop";

    @GET
    @Path("bonjour")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBonjour() {
        return Response
                .status(200)
                .type(MediaType.APPLICATION_JSON)
                .entity("{authServer: \""+authServer+"\", apiKey: \""+apiKey+"\"}")
                .build();
    }

    @GET
    @Path("startSession")
    @Produces(MediaType.APPLICATION_JSON)
    public Response postStartSession() {
        return Response
                .status(200)
                .type(MediaType.APPLICATION_JSON)
                .encoding("{authServer: \""+authServer+"\", apiKey: \""+apiKey+"\"}")
                .build();
    }
}
