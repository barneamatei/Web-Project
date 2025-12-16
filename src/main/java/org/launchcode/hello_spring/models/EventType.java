package org.launchcode.hello_spring.models;

public enum EventType {
    CONFERENCE("Conference"),
    MEETIUP("Meetup"),
    WORKSHOP("Workshop"),
    SOCIAL("Social"),;

    private final String displayName;

    public String getDisplayName() {
        return displayName;
    }

    EventType(String displayName) {
        this.displayName = displayName;
    }
}
