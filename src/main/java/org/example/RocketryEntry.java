package org.example;

public class RocketryEntry {
    private final String timeStamp;
    private final String name;
    private final String discordUsername;

    private final String color;

    public RocketryEntry(String timestamp, String name, String discordUsername, String color) {
        this.timeStamp = timestamp;
        this.name = name;
        this.discordUsername = discordUsername;
        this.color = color;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getName() {
        return name;
    }

    public String getDiscordUsername() {
        return discordUsername;
    }

    public String getColor() {
        return color;
    }
}
