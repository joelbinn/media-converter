package se.joelabs.mediaconverter.rest.resources;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.logging.Logger;


@Path("/config")
public class ConfigResource {
    private static final Logger logger = Logger.getLogger(ConfigResource.class.getName());
    private ConcurrentNavigableMap<String, List<String>> configuration;
    private DB db;

    @PostConstruct
    protected void init() {
        db = DBMaker.newFileDB(new File("mconvdb"))
                    .closeOnJvmShutdown()
                    .make();

        configuration = db.getTreeMap("configuration");
        logger.info("Initialized DB for ConfigResource");
    }

    @PreDestroy
    public void dispose() {
        db.close();
    }

    @PUT
    @Path("/outputdir")
    public Response setOutputDir(@QueryParam("dirname") String dirName) {
        configuration.put("outputdir", Arrays.asList(dirName));
        db.commit();
        logger.fine("Saved outputdir=" + dirName);
        return Response.ok("outputdir="+dirName).build();
    }

    @GET
    @Path("/outputdir")
    @Produces("text/plain")
    public String getOutputDir() {
        List<String> dir = configuration.get("outputdir");
        logger.fine("outputdir="+dir);
        if (dir != null && dir.size() == 1) {
            return dir.get(0);
        } else {
            return null;
        }
    }
}
