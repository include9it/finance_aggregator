package io.include9it.finance_aggregator.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class RestUtil {
    public static HttpHeaders getHeader() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }
}
