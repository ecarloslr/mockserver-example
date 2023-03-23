package me.nefti.learning.client.model;

import java.time.Instant;

/**
 * Represents a single User in the system.
 */
public class User {

    private String uniqueId;

    private String username;

    private Instant registeredSince;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Instant getRegisteredSince() {
        return registeredSince;
    }

    public void setRegisteredSince(Instant registeredSince) {
        this.registeredSince = registeredSince;
    }
}
