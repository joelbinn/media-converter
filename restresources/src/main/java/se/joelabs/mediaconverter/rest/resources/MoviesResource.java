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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.logging.Logger;


@Path("/movies")
public class MoviesResource {
    public static class Movie implements Serializable {


    }
    private static final Logger logger = Logger.getLogger(MoviesResource.class.getName());
    private ConcurrentNavigableMap<String, List<Movie>> movies;
    private DB db;

    @PostConstruct
    protected void init() {
        db = MConvDB.instance().getDB();
        movies = db.getTreeMap("movies");
        logger.info("Initialized DB for ConfigResource");
    }

    @PreDestroy
    public void dispose() {
        db.close();
    }

    @PUT
    @Path("/")
    public Response setOutputDir(Movie movie) {
        movies.put(movie.getId(), movie);
        db.commit();
        logger.fine("Saved outputdir=" + dirName);
        return Response.ok().build();
    }

    @GET
    @Path("/")
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
}
