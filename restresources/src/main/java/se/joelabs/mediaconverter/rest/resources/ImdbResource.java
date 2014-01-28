/*
 * User: joel
 * Date: 2014-01-28
 * Time: 19:56
 */
package se.joelabs.mediaconverter.rest.resources;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Path("/imdb")
public class ImdbResource {

    @GET
    @Path("/image/{imdburl: .*}")
    @Produces("image/*")
    public Response getImage(@PathParam("imdburl") String imdbUrl) throws IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String uri = "http://ia.media-imdb.com/images/" + imdbUrl;
        final HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader(HttpHeaders.ACCEPT, "image/webp");
        httpGet.setHeader(HttpHeaders.ACCEPT, "*/*;q=0.8");
        httpGet.setHeader("Referer", "");
        final HttpResponse response = httpclient.execute(httpGet);
        final String contentType = response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue();
        final StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    byte[] buf = new byte[1024 * 8];
                    InputStream is = response.getEntity().getContent();
                    int n;
                    while ((n = is.read(buf)) >= 0) {
                        output.write(buf, 0, n);
                    }
                } catch (IOException e) {
                    //
                } finally {
                    httpGet.releaseConnection();
                }

            }
        };
        return Response
                .ok(stream, contentType)
                .build();
    }

    @GET
    @Path("/suggests/{imdburl: .*}")
    @Produces("application/json")
    public Response getSuggests(@PathParam("imdburl") String imdbUrl) throws IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String uri = "http://sg.media-imdb.com/suggests/" + imdbUrl;
        final HttpGet httpGet = new HttpGet(uri);
        final HttpResponse response = httpclient.execute(httpGet);
        final String contentType = response.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue();
        final StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    byte[] buf = new byte[1024 * 8];
                    InputStream is = response.getEntity().getContent();
                    int n;
                    while ((n = is.read(buf)) >= 0) {
                        output.write(buf, 0, n);
                        output.flush();
                    }
                } finally {
                    httpGet.releaseConnection();
                }
            }
        };
        return Response
                .ok(stream, contentType)
                .build();
    }
}
