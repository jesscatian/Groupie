package csci310.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class EventTest {

    @Test
    public void testGenerateID() {
        Events events = new Events();
        events.setEvents(null);
        events.getEvents();
        Event event = new Event("a","a","a","a","a");
        event.setName("b");
        event.setDate("b");
        event.setUrl("b");
        event.setTime("b");
        event.setGenre("b");
        event.generateID();
        assertNotEquals("a",event.getEventID());
        event.setEventID("a");
        assertEquals("a", event.getEventID());
    }
}