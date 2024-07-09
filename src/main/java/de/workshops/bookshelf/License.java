package de.workshops.bookshelf;

import java.net.URI;
import java.net.URL;

public class License {
    private String name;
    private URL link;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getLink() {
        return link;
    }


    public void setLink(URL link) {
        this.link = link;
    }
}
