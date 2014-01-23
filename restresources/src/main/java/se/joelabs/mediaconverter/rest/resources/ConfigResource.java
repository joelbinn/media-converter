package se.joelabs.mediaconverter.rest.resources;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.ArrayList;
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
        return Response.ok().build();
    }

    @GET
    @Path("/outputdir")
    @Produces("application/json")
    public String getOutputDir() {
        List<String> dir = configuration.get("outputdir");
        logger.fine("outputdir=" + dir);
        if (dir != null && dir.size() == 1) {
            return dir.get(0);
        } else {
            return null;
        }
    }

    @POST
    @Path("/scandirs")
    public Response addScanDir(@QueryParam("dirname") String dirName) {
        List<String> dirs = configuration.get("scandirs");
        if (dirs == null) {
            dirs = new ArrayList<>();
        }
        configuration.put("scandirs", Arrays.asList(dirName));
        db.commit();
        logger.fine("Saved scandirs=" + dirs);
        return Response.ok().build();
    }

    @GET
    @Path("/scandirs")
    @Produces("application/json")
    public List<String> getScanDirs() {
        List<String> dirs = configuration.get("scandirs");
        logger.fine("scandirs=" + dirs);
        return dirs;
    }
}
