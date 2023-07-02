package org.example;

public class RocketryEntry {

    private final String meetingCount;
    private final String timeStamp;
    private final String name;
    private final String discordUsername;

    private final String color;

    public RocketryEntry(String meetingCount,String timestamp, String name, String discordUsername, String color) {
        this.meetingCount = meetingCount;
        this.timeStamp = timestamp;
        this.name = name;
        this.discordUsername = discordUsername;
        this.color = color;
    }

    public String getMeetingCount() {return meetingCount;}
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
