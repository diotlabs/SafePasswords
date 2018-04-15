package com.diot.safepasswords;

public class ListDataModal {
    String username, website;

    public ListDataModal(String username, String website) {
        this.username = username;
        this.website = website;
    }

    public String getUsername() {
        return username;
    }

    public String getWebsite() {
        return website;
    }
}
