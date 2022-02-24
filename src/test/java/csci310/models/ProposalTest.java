package csci310.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ProposalTest {

    private String proposalID = "proposalid";
    private String eventID = "eventid";

    @Test
    public void testGenerateID() {
        Proposal proposal = new Proposal("a",
                "a",
                null,
                null,
                "id",
                null);
        proposal.setProposalTitle("title");
        proposal.setSenderUsername("user");
        proposal.setReceiverUsernames(new ArrayList<>(Arrays.asList("user")));
        proposal.setEvents(new ArrayList<>(Arrays.asList(new Event("a","a","a","a","a"))));
        proposal.setProposalID(proposalID);
        proposal.generateID();
        assertNotEquals(proposalID,proposal.getProposalID());
    }

    @Test
    public void testGenerateIDForProposalAndEvents() {
        Proposal proposal = new Proposal("a",
                "a",
                new ArrayList<>(Arrays.asList("a","b")),
                new ArrayList<>(Arrays.asList(new Event("a","a","a","a","a",eventID))),
                proposalID,
                null);
        proposal.generateIDForProposalAndEvents();
        assertNotEquals(proposalID,proposal.getProposalID());
        assertNotEquals(eventID,proposal.getEvents().get(0).getEventID());
    }
}