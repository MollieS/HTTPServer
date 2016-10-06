package httpserver.httprequests;

import org.junit.Test;

import java.net.URI;

import static httpserver.routing.Method.GET;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HTTPRequestTest {

    private HTTPRequest httpRequest = new HTTPRequest(GET, "/");

    @Test
    public void hasAMethod() {
        assertEquals(GET, httpRequest.getMethod());
    }

    @Test
    public void hasAUri() {
        assertTrue(URI.create("/").equals(httpRequest.getRequestURI()));
    }

    @Test
    public void knowsIfItHasParameters() {
        httpRequest.setParams("data=hello");

        assertTrue(httpRequest.hasParams());
    }

    @Test
    public void knowsIfRangeStartIsSet() {
        httpRequest.setRangeStart(1);

        assertTrue(httpRequest.hasRangeStart());
    }

    @Test
    public void knowsIfRangeEndIsSet() {
        httpRequest.setRangeEnd(1);

        assertTrue(httpRequest.hasRangeEnd());
    }

    @Test
    public void knowsIfItHasARangeIfOnlyEndIsSet() {
        httpRequest.setRangeEnd(1);

        assertTrue(httpRequest.hasRange());
    }

    @Test
    public void knowsIfItHasARangeIfOnlyStartIsSet() {
        httpRequest.setRangeStart(1);

        assertTrue(httpRequest.hasRange());
    }

}
