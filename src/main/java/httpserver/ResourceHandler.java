package httpserver;

import httpserver.resourcemanagement.FileResource;

import java.net.URI;

public interface ResourceHandler {

    FileResource getResource(URI path);
}
