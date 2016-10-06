package httpserver.httpresponses;

import httpserver.ResponseDate;

public class ResponseDateFake implements ResponseDate {

    @Override
    public String getDate() {
        return "Wed, 5 Oct 2016 15:20:15 +0100";
    }
}
