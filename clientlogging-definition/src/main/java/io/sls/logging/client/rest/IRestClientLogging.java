package io.sls.logging.client.rest;

import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.io.InputStream;

/**
 * User: jarisch
 * Date: 26.01.13
 * Time: 21:12
 */
@Path("/logging")
public interface IRestClientLogging {
    @GET
    @NoCache()
    InputStream logMessage(@QueryParam("type") String logType, @QueryParam("msg") String message);
}
