package com.diot.safepasswords;

public class ListDataModal {
    int image, dataid;
    String title, website;

    public ListDataModal( int dataid, String title, String website) {
        //this.image = image;
        this.dataid = dataid;
        this.title = title;
        this.website = website;
    }
    public int getImage() { return image;    }
    public String getTitle() {
        return title;
    }
    public int getId() {
        return dataid;
    }

    public String getWebsite() {
        return website;
    }
}
