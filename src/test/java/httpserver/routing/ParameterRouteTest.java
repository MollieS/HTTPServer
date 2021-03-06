package httpserver.routing;

import httpserver.Request;
import httpserver.Response;
import httpserver.httprequests.RequestFake;
import org.junit.Test;

import java.nio.charset.Charset;

import static httpserver.routing.Method.GET;
import static httpserver.routing.Method.POST;
import static org.junit.Assert.assertEquals;

public class ParameterRouteTest {

    private ParameterRoute parameterRoute = new ParameterRoute(GET);
    private RequestFake getRequest = new RequestFake(GET, "/parameters");

    @Test
    public void addsParametersToResponseBody() {
        getRequest.setParams("variable_1=parameter");

        Response httpResponse = parameterRoute.performAction(getRequest);
        String body = new String(httpResponse.getBody(), Charset.forName("UTF-8"));

        assertEquals("variable_1 = parameter", body);
    }

    @Test
    public void addsMultipleParametersToResponseBody() {
        getRequest.setParams("variable_1=parameter&variable_2=parameter2");

        Response httpResponse = parameterRoute.performAction(getRequest);
        String body = new String(httpResponse.getBody(), Charset.forName("UTF-8"));

        assertEquals("variable_1 = parameter\nvariable_2 = parameter2", body);
    }

    @Test
    public void canFormatSpecialCharacters() {
        getRequest.setParams("variable_1=&,=!&variable_2=stuff");

        Response httpResponse = parameterRoute.performAction(getRequest);
        String body = new String(httpResponse.getBody(), Charset.forName("UTF-8"));

        assertEquals("variable_1 = &,=!\nvariable_2 = stuff", body);
    }

    @Test
    public void canGiveA405ForMethodNotAllowed() {
        Request httpRequest = new RequestFake(POST, "/parameters");

        Response httpResponse = parameterRoute.performAction(httpRequest);

        assertEquals(405, httpResponse.getStatusCode());
    }
}
