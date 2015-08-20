package com.api.server.resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.api.bean.ComplexObject;
import com.google.inject.Singleton;

@Path("/test")
@Singleton
public class TestResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String echo(@QueryParam("text") String text) {
        return text;
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ComplexObject echo(ComplexObject obj) {
        return obj;
    }
}

