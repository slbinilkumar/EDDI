package ai.labs.resources.rest.regulardictionary;

import ai.labs.resources.rest.IRestVersionInfo;
import ai.labs.resources.rest.documentdescriptor.model.DocumentDescriptor;
import ai.labs.resources.rest.method.PATCH;
import ai.labs.resources.rest.patch.PatchInstruction;
import ai.labs.resources.rest.regulardictionary.model.RegularDictionaryConfiguration;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

/**
 * @author ginccc
 */
@Path("/regulardictionarystore/regulardictionaries")
public interface IRestRegularDictionaryStore extends IRestVersionInfo {
    String resourceURI = "eddi://ai.labs.regulardictionary/regulardictionarystore/regulardictionaries/";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<DocumentDescriptor> readRegularDictionaryDescriptors(@QueryParam("filter") @DefaultValue("") String filter,
                                                              @QueryParam("index") @DefaultValue("0") Integer index,
                                                              @QueryParam("limit") @DefaultValue("20") Integer limit);

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    RegularDictionaryConfiguration readRegularDictionary(@PathParam("id") String id, @QueryParam("version") Integer version,
                                                         @QueryParam("filter") @DefaultValue("") String filter,
                                                         @QueryParam("order") @DefaultValue("") String order,
                                                         @QueryParam("index") @DefaultValue("0") Integer index,
                                                         @QueryParam("limit") @DefaultValue("20") Integer limit);

    @GET
    @Path("/{id}/expressions")
    @Produces(MediaType.APPLICATION_JSON)
    List<String> readExpressions(@PathParam("id") String id, @QueryParam("version") Integer version,
                                 @QueryParam("filter") @DefaultValue("") String filter,
                                 @QueryParam("order") @DefaultValue("") String order,
                                 @QueryParam("index") @DefaultValue("0") Integer index,
                                 @QueryParam("limit") @DefaultValue("20") Integer limit);

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    URI updateRegularDictionary(@PathParam("id") String id, @QueryParam("version") Integer version, RegularDictionaryConfiguration regularDictionaryConfiguration);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    Response createRegularDictionary(RegularDictionaryConfiguration regularDictionaryConfiguration);

    @DELETE
    @Path("/{id}")
    void deleteRegularDictionary(@PathParam("id") String id, @QueryParam("version") Integer version);

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    URI patchRegularDictionary(@PathParam("id") String id, @QueryParam("version") Integer version, PatchInstruction<RegularDictionaryConfiguration>[] patchInstructions);
}