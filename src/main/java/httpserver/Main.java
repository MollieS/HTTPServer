package httpserver;

import httpserver.resourcemanagement.HTTPResourceHandler;
import httpserver.resourcemanagement.ResourceParser;
import httpserver.routing.FileRoute;
import httpserver.routing.Route;
import httpserver.routing.Router;
import httpserver.server.HTTPServer;
import httpserver.server.HTTPSocketServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        ServerRunner serverRunner = new ServerRunner();
        int port = serverRunner.parsePort(args);
        String path = serverRunner.parseDirectoryPath(args);
        String url = serverRunner.buildUrl(args);
        HTTPResourceHandler resourceHandler = new HTTPResourceHandler(path, new ResourceParser());
        List<Route> registeredRoutes = serverRunner.createRoutes(url, resourceHandler);
        Router router = new Router(new FileRoute(resourceHandler), registeredRoutes);
        SocketServer socketServer = null;
            try {
                socketServer = new HTTPSocketServer(new ServerSocket(port));
            } catch (IOException e) {
                e.printStackTrace();
            }
        HTTPServer httpServer = new HTTPServer(socketServer, router);
        while(true) {
            httpServer.start();
        }
    }
}
