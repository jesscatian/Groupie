package csci310.models;

import java.util.ArrayList;

public class Events {

    private ArrayList<Event> events;

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public Events() {
        events = new ArrayList<>();
    }

    public Events(RawResult rawResult) throws NullPointerException {
        events = new ArrayList<>();
        for (aEvent aEvent: rawResult._embedded.events) {
            Event event = new Event(
                    aEvent.name,
                    aEvent.dates.start.localDate,
                    aEvent.dates.start.localTime,
                    aEvent.url,
                    aEvent.classifications.get(0).genre.name,
                    null);
            events.add(event);
        }
    }
}
