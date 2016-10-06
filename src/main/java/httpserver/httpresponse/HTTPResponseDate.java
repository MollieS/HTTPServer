package httpserver.httpresponse;

import httpserver.ResponseDate;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class HTTPResponseDate implements ResponseDate {

    @Override
    public String getDate() {
        return ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }
}
