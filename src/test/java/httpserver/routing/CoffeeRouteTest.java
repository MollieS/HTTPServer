package httpserver.routing;

import httpserver.httprequests.HTTPRequest;
import httpserver.httpresponse.HTTPResponse;
import org.junit.Test;

import java.net.URI;
import java.nio.charset.Charset;

import static httpserver.routing.Method.*;
import static org.junit.Assert.assertEquals;

public class CoffeeRouteTest {

    private CoffeeRoute coffeeRoute = new CoffeeRoute(GET, HEAD, OPTIONS);

    @Test
    public void returnsTheCorrectStatusForAGet() {
        HTTPRequest httpRequest = new HTTPRequest(GET, "/coffee");

        HTTPResponse httpResponse = coffeeRoute.performAction(httpRequest);

        assertEquals(418, httpResponse.getStatusCode());
        assertEquals("I'm a teapot", httpResponse.getReasonPhrase());
    }

    @Test
    public void addsBodyToResponseForGet() {
        HTTPRequest httpRequest = new HTTPRequest(GET, "/coffee");

        HTTPResponse httpResponse = coffeeRoute.performAction(httpRequest);
        String body = new String(httpResponse.getBody(), Charset.forName("UTF-8"));

        assertEquals("I'm a teapot", body);
        assertEquals("text/plain", httpResponse.getContentType());
    }

    @Test
    public void hasAURI() {
        CoffeeRoute coffeeRoute = new CoffeeRoute(GET);

        assertEquals(URI.create("/coffee"), coffeeRoute.getUri());
    }

    @Test
    public void returnsA405ForMethodNotAllowed() {
        HTTPRequest httpRequest = new HTTPRequest(BOGUS, "/coffee");

        HTTPResponse httpResponse = coffeeRoute.performAction(httpRequest);

        assertEquals(405, httpResponse.getStatusCode());
    }
}
