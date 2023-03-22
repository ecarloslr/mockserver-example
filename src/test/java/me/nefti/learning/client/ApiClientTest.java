package me.nefti.learning.client;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerExtension;

@ExtendWith(MockServerExtension.class)
// @MockServerSettings(ports = {8080})
class ApiClientTest {

    private final ClientAndServer client;

    public ApiClientTest(ClientAndServer client) {
        this.client = client;
    }


}