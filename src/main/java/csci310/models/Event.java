package csci310.models;

import java.util.UUID;

public class Event {
    private String name;
    private String date;
    private String time;
    private String url;
    private String genre;
    private String eventID;

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }

    public String getGenre() {
        return genre;
    }

    public String getEventID() {
        return eventID;
    }

    public Event(String name, String date, String time, String url, String genre) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.url = url;
        this.genre = genre;
        eventID = UUID.randomUUID().toString();
    }

    public Event(String name, String date, String time, String url, String genre, String eventID) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.url = url;
        this.genre = genre;
        this.eventID = eventID;
    }

    public void generateID() {
        eventID = UUID.randomUUID().toString();
    }
}