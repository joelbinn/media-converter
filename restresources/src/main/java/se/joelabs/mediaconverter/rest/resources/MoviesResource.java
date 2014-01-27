package se.joelabs.mediaconverter.rest.resources;

import com.sun.xml.txw2.annotation.XmlAttribute;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.logging.Logger;


@Path("/movies")
public class MoviesResource {
    @XmlRootElement
    public static class Movie implements Serializable {
        @javax.xml.bind.annotation.XmlAttribute
        String file;
        @javax.xml.bind.annotation.XmlAttribute
        String title;
        @javax.xml.bind.annotation.XmlAttribute
        String imdbId;
        @javax.xml.bind.annotation.XmlAttribute
        String image;
        @javax.xml.bind.annotation.XmlAttribute
        Long id;

        protected Movie() {
        }

        public Movie(String file, String title, String imdbId, String image, Long id) {
            this.file = file;
            this.title = title;
            this.imdbId = imdbId;
            this.image = image;
            this.id = id;
        }
    }

    private static final Logger logger = Logger.getLogger(MoviesResource.class.getName());
    private ConcurrentNavigableMap<Long, Movie> movies;
    private ConcurrentNavigableMap<String, String> metadata;
    private DB db;

    @PostConstruct
    protected void init() {
        db = MConvDB.instance().getDB();
        movies = db.getTreeMap("movies");
        metadata = db.getTreeMap("metadata");
        logger.info("Initialized DB for ConfigResource");
    }

    @PreDestroy
    public void dispose() {
        db.close();
    }

    @PUT
    @Path("/")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateMovie(Movie movie) {
        if (movie.id == null) {
            Long nextId = Long.parseLong(metadata.get("nextId") != null ? metadata.get("nextId") : "1");
            movie.id = nextId;
            metadata.put("nextId", (nextId + 1) + "");
        }
        movies.put(movie.id, movie);
        db.commit();
        return Response.ok(movie.id).build();
    }

    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Movie> getMovies() {
        List<Movie> movieList = new ArrayList<>(movies.values());
        logger.fine("movieList=" + movieList);
        return movieList;
    }
}
