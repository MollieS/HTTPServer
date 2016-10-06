package httpserver.routing;

import httpserver.Resource;
import httpserver.httprequests.HTTPRequest;
import httpserver.httpresponse.HTTPResponse;
import httpserver.resourcemanagement.HTTPResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static httpserver.httpresponse.StatusCode.OK;

public class FormRoute extends Route {

    private final String path;
    private final File file;

    public FormRoute(String path, Method... methods) {
        super("/form", methods);
        this.path = path;
        this.file = new File(path);
    }

    @Override
    public HTTPResponse performAction(HTTPRequest httpRequest) {
        if (methodIsAllowed(httpRequest.getMethod())) {
            try {
                return getHttpResponse(httpRequest);
            } catch (IOException e) {
                createForm();
            }
        }
        return methodNotAllowed();
    }

    private void createForm() {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new FormManagerException(e);
        }
    }

    private HTTPResponse getHttpResponse(HTTPRequest httpRequest) throws IOException {
        HTTPResponse httpResponse = HTTPResponse.create(OK);
        switch (httpRequest.getMethod()) {
            case POST:
                writeToFile(httpRequest);
                break;
            case PUT:
                writeToFile(httpRequest);
                break;
            case GET:
                Resource resource = new HTTPResource(readFromFile());
                httpResponse.withBody(resource);
                break;
            case DELETE:
                deleteFileContents();
                break;
        }
        return httpResponse;
    }

    private void deleteFileContents() throws IOException {
        clearForm(file.toPath());
    }

    private byte[] readFromFile() throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    private void writeToFile(HTTPRequest httpRequest) throws IOException {
        Path path = Paths.get(this.path);
        if (httpRequest.getData() != null) {
            clearForm(path);
            writeToForm(httpRequest, path);
        }
    }

    private void writeToForm(HTTPRequest httpRequest, Path path) throws IOException {
        Files.write(path, httpRequest.getData().getBytes());
    }

    private void clearForm(Path file) throws IOException {
        Files.delete(file);
    }
}
