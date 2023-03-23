package me.nefti.learning.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import me.nefti.learning.client.model.UserList;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Client for REST API.
 */
public class ApiClient {

    private final String url;

    private final int timeoutMillis;

    private final HttpClient httpClient;

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public ApiClient(String serviceUrl, int timeoutMillis) {
        this.url = serviceUrl;
        this.timeoutMillis = timeoutMillis;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.of(timeoutMillis, ChronoUnit.MILLIS))
                .build();
    }

    public UserList getUserList() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(url + "/users"))
                .timeout(Duration.of(timeoutMillis, ChronoUnit.MILLIS))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), UserList.class);
    }
}
