package com.diot.safepasswords;

public class ListDataModal {
    int image;
    String title, website;

    public ListDataModal( String title, String website) {
        //this.image = image;
        this.title = title;
        this.website = website;
    }
    public int getImage() { return image;    }
    public String getTitle() {
        return title;
    }

    public String getWebsite() {
        return website;
    }
}
