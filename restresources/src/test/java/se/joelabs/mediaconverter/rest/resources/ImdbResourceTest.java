package se.joelabs.mediaconverter.rest.resources;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import javax.ws.rs.core.Response;
import java.io.InputStream;

import static junit.framework.Assert.assertEquals;

/**
 * ImdbResource Tester.
 */
public class ImdbResourceTest {


    private ImdbResource imdbResource;

    @Before
    public void before() throws Exception {
        imdbResource = new ImdbResource();
    }

    /**
     * Method: getImage(@QueryParam("imdburl") String imdbUrl)
     */
    @Test
    public void testGetImage() throws Exception {
        Response response = imdbResource.getImage("/mobile/film-40x54.png");
        InputStream is = (InputStream) response.getEntity();
        assertEquals(3, is.available());
    }
}
