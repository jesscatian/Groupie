package csci310.models;

import java.util.ArrayList;

// class for parsing api response

public class RawResult {
    EventList _embedded;
}

class EventList {
    ArrayList<aEvent> events;
}

class aEvent {
    String name;
    String url;
    Dates dates;
    ArrayList<Classification> classifications;
}

class Dates {
    Start start;
}

class Start {
    String localDate;
    String localTime;
}

class Classification {
    Genre genre;
}

class Genre {
    String name;
}