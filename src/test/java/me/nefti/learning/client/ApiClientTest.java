package me.nefti.learning.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerExtension;

@ExtendWith(MockServerExtension.class)
// @MockServerSettings(ports = {8080})
class ApiClientTest {

    private final ClientAndServer mockServerClient;

    public ApiClientTest(ClientAndServer mockServerClient) {
        this.mockServerClient = mockServerClient;
    }


    @Test
    void testGetUserList() throws Exception {

    }

   
}