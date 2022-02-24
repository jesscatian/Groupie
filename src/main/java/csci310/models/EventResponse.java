package csci310.models;

public class EventResponse {
    private String eventID;
    private int availability;
    private int excitement;
    private String receiverUsername;

    public String getEventID() {
        return eventID;
    }

    public int getAvailability() {
        return availability;
    }

    public int getExcitement() {
        return excitement;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public void setExcitement(int excitement) {
        this.excitement = excitement;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public EventResponse(String eventID, int availability, int excitement, String receiverUsername) {
        this.eventID = eventID;
        this.availability = availability;
        this.excitement = excitement;
        this.receiverUsername = receiverUsername;
    }
}
