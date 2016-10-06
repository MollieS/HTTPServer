package httpserver.routing;

import httpserver.ResourceHandler;
import httpserver.httprequests.HTTPRequest;
import httpserver.httpresponse.HTTPResponse;
import httpserver.resourcemanagement.HTTPResourceHandler;
import httpserver.resourcemanagement.ResourceParser;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

import static httpserver.routing.Method.GET;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PartialContentRouteTest {

    private String path = getClass().getClassLoader().getResource("directory").getPath();
    private ResourceHandler resourceHandler = new HTTPResourceHandler(path, new ResourceParser());
    private PartialContentRoute partialContentRoute = new PartialContentRoute(resourceHandler, GET);

    @Test
    public void sendsTheCorrectResponseForAGETRequest() {
        HTTPRequest httpRequest = new HTTPRequest(GET, "/partial_content.txt");

        HTTPResponse httpResponse = partialContentRoute.performAction(httpRequest);
        String body = new String(httpResponse.getBody(), Charset.defaultCharset());

        assertEquals(200, httpResponse.getStatusCode());
        assertEquals("OK", httpResponse.getReasonPhrase());
        assertTrue(body.contains("206"));
        assertEquals("text/plain", httpResponse.getContentType());
    }

    @Test
    public void sendsTheCorrectHeaderResponseForAPartialGetRequest() {
        HTTPRequest httpRequest = new HTTPRequest(GET, "/partial_content.txt");
        httpRequest.setRangeStart(0);
        httpRequest.setRangeEnd(4);

        HTTPResponse httpResponse = partialContentRoute.performAction(httpRequest);

        assertEquals(206, httpResponse.getStatusCode());
        assertEquals("Partial Content", httpResponse.getReasonPhrase());
    }

    @Test
    public void sendsTheCorrectResponseWithBodyForAPartialGetRequest() {
        HTTPRequest httpRequest = new HTTPRequest(GET, "/partial_content.txt");
        httpRequest.setRangeStart(0);
        httpRequest.setRangeEnd(4);

        HTTPResponse httpResponse = partialContentRoute.performAction(httpRequest);
        String body = new String(httpResponse.getBody(), Charset.defaultCharset());

        assertEquals("This ", body);
        assertEquals("text/plain", httpResponse.getContentType());
        assertEquals(5, httpResponse.getContentRange());
    }

    @Test
    public void sendsTheCorrectBodyForARequestWithNoStartRange() throws IOException {
        HTTPRequest httpRequest = new HTTPRequest(GET, "/partial_content.txt");
        httpRequest.setRangeEnd(6);

        HTTPResponse httpResponse = partialContentRoute.performAction(httpRequest);
        String body = new String(httpResponse.getBody(), Charset.defaultCharset());

        assertEquals(" 206.\n", body);
        assertEquals("text/plain", httpResponse.getContentType());
        assertEquals(6, httpResponse.getContentRange());
    }

    @Test
    public void sendsTheCorrectBodyForARequestWithNoRangeStart() {
        HTTPRequest httpRequest = new HTTPRequest(GET, "/partial_content.txt");
        httpRequest.setRangeStart(70);

        HTTPResponse httpResponse = partialContentRoute.performAction(httpRequest);
        String body = new String(httpResponse.getBody(), Charset.defaultCharset());

        assertEquals("a 206.\n", body);
        assertEquals("text/plain", httpResponse.getContentType());
        assertEquals(7, httpResponse.getContentRange());
    }
}
