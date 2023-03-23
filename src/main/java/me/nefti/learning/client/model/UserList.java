package me.nefti.learning.client.model;

import java.util.List;

/**
 * Contains a list of Users returned from a search.
 */
public class UserList {

    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
