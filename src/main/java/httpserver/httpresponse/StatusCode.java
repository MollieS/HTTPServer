package httpserver.httpresponse;

public enum StatusCode {

    OK("OK", 200),
    NOTFOUND("Not Found", 404),
    TEAPOT("I'm a teapot", 418),
    REDIRECT("Found", 302),
    PARTIAL("Partial Content", 206),
    NOTALLOWED("Method Not Allowed", 405);

    public final String reason;
    public final int code;

    StatusCode(String reason, int code) {
        this.reason = reason;
        this.code = code;
    }
}
