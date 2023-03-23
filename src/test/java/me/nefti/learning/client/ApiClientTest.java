package me.nefti.learning.client;

import me.nefti.learning.client.model.User;
import me.nefti.learning.client.model.UserList;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockserver.model.BinaryBody.binary;
import static org.mockserver.model.Delay.delay;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.JsonBody.json;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.model.BinaryBody;
import org.mockserver.model.JsonBody;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@ExtendWith(MockServerExtension.class)
// @MockServerSettings(ports = {8080})
class ApiClientTest {

    private final ClientAndServer mockServerClient;

    public ApiClientTest(ClientAndServer mockServerClient) {
        this.mockServerClient = mockServerClient;
    }


    @Test
    void testGetUserList() throws Exception {
        ApiClient apiClient = new ApiClient("http://localhost:" + mockServerClient.getPort(), 2_000);
        mockServerClient.when(request("/users")
                        .withMethod("GET")
                        .withBody(""))
                .respond(response()
                        .withBody(fromResource("/json/getUserList_Response.json")));

        UserList users = apiClient.getUserList();

        assertThat(users, is(notNullValue()));
        assertThat(users.getUsers(), is(notNullValue()));
        assertThat(users.getUsers(), contains(
                matchesUser("xU345m12", "john.connor", 1678492800),
                matchesUser("yZ452p56", "james.murphy", 1670630400))
        );
    }

    @Test
    @Timeout(value = 3_000, unit = TimeUnit.MILLISECONDS)
    void testGetUserList_Timeout() throws Exception {
        ApiClient apiClient = new ApiClient("http://localhost:" + mockServerClient.getPort(), 2_000);
        mockServerClient.when(request("/users")
                        .withMethod("GET")
                        .withBody(""))
                .respond(response()
                        .withDelay(delay(TimeUnit.MILLISECONDS, 5_000))
                        .withBody(fromResource("/json/getUserList_Response.json")));

        assertThrows(IOException.class, () -> {
            apiClient.getUserList();
        });
    }

    @Test
    void testPostUser() throws Exception {
        ApiClient apiClient = new ApiClient("http://localhost:" + mockServerClient.getPort(), 2_000);
        mockServerClient.when(request("/users")
                        .withMethod("POST")
                        .withBody(fromJsonResource("/json/postUser_Request.json", StandardCharsets.UTF_8)))
                .respond(response()
                        .withBody(fromResource("/json/postUser_Response.json")));

        User user = new User();
        user.setUsername("john.connor");
        User createdUser = apiClient.postUser(user);

        assertThat(createdUser, is(notNullValue()));
        assertThat(createdUser, matchesUser("vU345m15", "john.connor", 1678492860));
    }

    private BinaryBody fromResource(String resource) throws IOException {
        return binary(this.getClass().getResourceAsStream(resource).readAllBytes());
    }

    private JsonBody fromJsonResource(String resource, Charset charset) throws IOException {
        byte[] bytes = this.getClass().getResourceAsStream(resource).readAllBytes();
        return json(new String(bytes, charset), charset);
    }

    private Matcher<User> matchesUser(String uniqueId, String username, long timestamp) {
        return new TypeSafeMatcher<User>() {
            @Override
            protected boolean matchesSafely(User item) {
                assertThat(item, is(notNullValue()));
                assertThat(item.getUniqueId(), is(uniqueId));
                assertThat(item.getUsername(), is(username));
                assertThat(item.getRegisteredSince(), is(Instant.ofEpochSecond(timestamp)));
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Expected uniqueId: ").appendValue(uniqueId)
                        .appendText(", username: ").appendValue(username)
                        .appendText(", registeredSince: ").appendValue(timestamp);
            }
        };
    }
}