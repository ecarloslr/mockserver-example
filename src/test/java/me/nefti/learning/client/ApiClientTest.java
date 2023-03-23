package me.nefti.learning.client;

import me.nefti.learning.client.model.User;
import me.nefti.learning.client.model.UserList;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.contains;
import static org.mockserver.model.BinaryBody.binary;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.JsonBody.json;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.model.BinaryBody;

import java.io.IOException;
import java.time.Instant;

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

    private BinaryBody fromResource(String resource) throws IOException {
        return binary(this.getClass().getResourceAsStream(resource).readAllBytes());
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