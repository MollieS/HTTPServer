package httpserver;

import httpserver.resourcemanagement.HTTPResourceHandler;
import httpserver.resourcemanagement.ResourceParser;
import httpserver.routing.Route;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ServerRunnerTest {

    private String path = getClass().getClassLoader().getResource("directory").getPath();
    private ResourceHandler resourceHandler = new HTTPResourceHandler(path, new ResourceParser());
    private ServerRunner serverRunner = new ServerRunner();

    @Test
    public void canParsePortNumber() {
        String[] args = {"-p", "4000", "-d", "/directory/path"};

        int port = serverRunner.parsePort(args);

        assertEquals(4000, port);
    }

    @Test
    public void returns5000IfNoPortNumberGiven() {
        String[] args = {"-d", "/directory/path"};

        int port = serverRunner.parsePort(args);

        assertEquals(5000, port);
    }

    @Test
    public void returnsTheDirectoryPathIfGiven() {
        String[] args = {"-p", "4000", "-d", "/directory/path"};

        String path = serverRunner.parseDirectoryPath(args);

        assertEquals("/directory/path", path);
    }

    @Test
    public void returnsTheDefaultDirectoryPathIfNoneGiven() {
        String[] args = {"-p", "4000"};

        String path = serverRunner.parseDirectoryPath(args);

        assertEquals("/Users/molliestephenson/Java/Server/cob_spec/public", path);
    }

    @Test
    public void returnsAllTheRegisteredRoutes() {
        List<Route> registeredRoutes = serverRunner.createRoutes("localhost:5000", resourceHandler);

        assertEquals(8, registeredRoutes.size());
    }

    @Test
    public void buildsTheUrl() {
        String[] args = {"-p", "4000", "-d", "/directory/path"};

        String url = serverRunner.buildUrl(args);

        assertEquals("http://localhost:4000", url);
    }
}
