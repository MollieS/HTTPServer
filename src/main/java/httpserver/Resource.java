package httpserver;

public interface Resource {

    byte[] getContents();
    String getContentType();
}
