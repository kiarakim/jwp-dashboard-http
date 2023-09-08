package org.apache.coyote.http11.response;

public enum Header {

    LOCATION("Location"),
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    ACCEPT("Accept"),
    SET_COOKIE("Set-Cookie"),
    COOKIE("Cookie"),
    ;

    private final String name;

    Header(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
