package httpserver.routing;

import httpserver.httprequests.HTTPRequest;
import httpserver.httpresponse.HTTPResponse;
import org.junit.Test;

import static httpserver.routing.Method.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MethodOptionsRouteTest {

    private MethodOptionsRoute methodOptionsRoute = new MethodOptionsRoute(GET, PUT, POST, HEAD, OPTIONS);

    @Test
    public void returnsA200ResponseForAGetRequest() {
        HTTPRequest httpRequest = new HTTPRequest(GET, "/method_options");

        HTTPResponse httpResponse = methodOptionsRoute.performAction(httpRequest);

        assertEquals(200, httpResponse.getStatusCode());
        assertEquals("OK", httpResponse.getReasonPhrase());
    }

    @Test
    public void returnsA200ResponseForAPostRequest() {
        HTTPRequest httpRequest = new HTTPRequest(POST, "/method_options");

        HTTPResponse httpResponse = methodOptionsRoute.performAction(httpRequest);

        assertEquals(200, httpResponse.getStatusCode());
        assertEquals("OK", httpResponse.getReasonPhrase());
    }

    @Test
    public void returnsA405IfMethodNotAllowed() {
        HTTPRequest httpRequest = new HTTPRequest(BOGUS, "/method_options");

        HTTPResponse httpResponse = methodOptionsRoute.performAction(httpRequest);

        assertEquals(405, httpResponse.getStatusCode());
    }

    @Test
    public void returnsResponseWithMethodsForOptionsRequest() {
        HTTPRequest httpRequest = new HTTPRequest(OPTIONS, "/method_options");

        HTTPResponse httpResponse = methodOptionsRoute.performAction(httpRequest);

        assertTrue(httpResponse.getAllowedMethods().contains(GET));
        assertTrue(httpResponse.getAllowedMethods().contains(PUT));
        assertTrue(httpResponse.getAllowedMethods().contains(POST));
        assertTrue(httpResponse.getAllowedMethods().contains(HEAD));
        assertTrue(httpResponse.getAllowedMethods().contains(OPTIONS));
    }
}
