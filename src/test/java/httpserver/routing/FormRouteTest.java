package httpserver.routing;

import httpserver.httprequests.HTTPRequest;
import httpserver.httpresponse.HTTPResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static httpserver.routing.Method.*;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

public class FormRouteTest {

    private String path = getClass().getClassLoader().getResource("directory").getPath() + ("/form");
    private FormRoute formRoute = new FormRoute(path, GET, POST, PUT, DELETE);
    private HTTPRequest postRequest = new HTTPRequest(POST, "/form");
    private HTTPRequest getRequest = new HTTPRequest(GET, "/form");

    @Before
    public void setUp() throws IOException {
        Files.write(Paths.get(path), "".getBytes());
    }

    @After
    public void tearDown() throws IOException {
        if (new File(path).exists()) {
            Files.delete(Paths.get(path));
        }
    }

    @Test
    public void canPostToFormAndGetA200Response() {
        postRequest.setData("data=fatcat");

        HTTPResponse httpResponse = formRoute.performAction(postRequest);

        assertEquals(200, httpResponse.getStatusCode());
    }

    @Test
    public void canPostToFormAndGetDataInBodyOfGetResponse() {
        postRequest.setData("data=fatcat");

        formRoute.performAction(postRequest);

        HTTPResponse httpResponse = formRoute.performAction(getRequest);
        String body = new String(httpResponse.getBody(), Charset.forName("UTF-8"));

        assertEquals("data=fatcat", body);
    }

    @Test
    public void canPutToFormAndGetDataInBodyOfGetResponse() {
        postRequest.setData("data=hello");

        formRoute.performAction(postRequest);

        HTTPRequest httpRequest2 = new HTTPRequest(PUT, "/");
        httpRequest2.setData("data=goodbye");

        formRoute.performAction(httpRequest2);

        HTTPResponse httpResponse = formRoute.performAction(getRequest);
        String body = new String(httpResponse.getBody(), Charset.forName("UTF-8"));

        assertEquals("data=goodbye", body);
    }

    @Test
    public void canAcceptADeleteRequest() {
        postRequest.setData("data=hello");
        formRoute.performAction(postRequest);

        HTTPRequest httpRequest2 = new HTTPRequest(DELETE, "/form");
        HTTPResponse httpResponse = formRoute.performAction(httpRequest2);

        assertEquals(200, httpResponse.getStatusCode());
    }

    @Test
    public void canDeleteFromAForm() {
        postRequest.setData("data=hello");
        formRoute.performAction(postRequest);

        HTTPRequest httpRequest2 = new HTTPRequest(DELETE, "/form");
        HTTPResponse httpResponse = formRoute.performAction(httpRequest2);

        assertNull(httpResponse.getBody());
    }

    @Test
    public void canGiveAMethodNotAllowedResponse() {
        HTTPRequest httpRequest = new HTTPRequest(BOGUS, "/form");

        HTTPResponse httpResponse = formRoute.performAction(httpRequest);

        assertEquals(405, httpResponse.getStatusCode());
    }

    @Test(expected = FormManagerException.class)
    public void throwsAnExceptionIfFormFileCannotBeAccessed() {
        FormRoute formRoute = new FormRoute("/bad/path", GET);

        HTTPRequest httpRequest = new HTTPRequest(GET, "/form");

        formRoute.performAction(httpRequest);
    }
}
