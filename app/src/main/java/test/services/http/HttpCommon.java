package test.services.http;

import com.sun.net.httpserver.Headers;

public class HttpCommon {
    public static void setDefaultHeaders(Headers headers) {
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.add("Content-type", "application/json");
        headers.add("Access-Control-Allow-Headers", "*");
        headers.add("Access-Control-Allow-Credentials", "true");
        headers.add("Access-Control-Allow-Credentials-Header", "*");
    }
}
