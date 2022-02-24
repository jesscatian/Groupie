package csci310.utilities;

import csci310.models.Event;
import csci310.models.EventResponse;
import csci310.models.Proposal;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.*;

public class EventRecommenderTest {

    @Test
    public void testGetRecommendedEvent() {
        new EventRecommender();

        //Invalid proposalid throws -1
        assertEquals("-1", EventRecommender.GetRecommendedEvent("-1"));

        Event event1 = new Event("event 1","2021-01-01","19:00:00","abc.com","music",UUID.randomUUID().toString());
        Event event2 = new Event("event 2","2021-01-01","19:00:00","abc.com","music", UUID.randomUUID().toString());

        ArrayList<Event> events = new ArrayList<Event>();
        events.add(event1);
        events.add(event2);

        ArrayList<String> recievers = new ArrayList<String>();
        recievers.add("testReceiver1");
        recievers.add("testReceiver2");

        Proposal prop = new Proposal("testProp", "testUser", recievers, events);
        DatabaseManager.object().insertSentProposal(prop.getProposalID(), prop.getProposalTitle(), prop.getSenderUsername());
        DatabaseManager.object().insertReceivedProposal("testReceiver1", prop.getProposalID());
        DatabaseManager.object().insertReceivedProposal("testReceiver2", prop.getProposalID());

        DatabaseManager.object().insertEvent(event1, prop.getProposalID());
        DatabaseManager.object().insertEvent(event2, prop.getProposalID());

        //unavailability test
        EventResponse receiver1event1response = new EventResponse(event1.getEventID(), 1, 5, "testReceiver1");
        EventResponse receiver2event1response = new EventResponse(event1.getEventID(), 1, 5, "testReceiver2");
        EventResponse receiver1event2response = new EventResponse(event2.getEventID(), 1, 5, "testReceiver1");
        EventResponse receiver2event2response = new EventResponse(event2.getEventID(), 0, 5, "testReceiver2");

        DatabaseManager.object().insertRespondedEvent(receiver1event1response);
        DatabaseManager.object().insertRespondedEvent(receiver2event1response);
        DatabaseManager.object().insertRespondedEvent(receiver1event2response);
        DatabaseManager.object().insertRespondedEvent(receiver2event2response);

        assertEquals(event1.getEventID(), EventRecommender.GetRecommendedEvent(prop.getProposalID()));

        //less excitement equal availability test
        receiver1event1response = new EventResponse(event1.getEventID(), 1, 5, "testReceiver1");
        receiver2event1response = new EventResponse(event1.getEventID(), 1, 5, "testReceiver2");
        receiver1event2response = new EventResponse(event2.getEventID(), 1, 5, "testReceiver1");
        receiver2event2response = new EventResponse(event2.getEventID(), 1, 4, "testReceiver2");

        DatabaseManager.object().insertRespondedEvent(receiver1event1response);
        DatabaseManager.object().insertRespondedEvent(receiver2event1response);
        DatabaseManager.object().insertRespondedEvent(receiver1event2response);
        DatabaseManager.object().insertRespondedEvent(receiver2event2response);

        assertEquals(event1.getEventID(), EventRecommender.GetRecommendedEvent(prop.getProposalID()));

        //greater excitement equal availability test
        receiver1event1response = new EventResponse(event1.getEventID(), 1, 5, "testReceiver1");
        receiver2event1response = new EventResponse(event1.getEventID(), 1, 4, "testReceiver2");
        receiver1event2response = new EventResponse(event2.getEventID(), 1, 5, "testReceiver1");
        receiver2event2response = new EventResponse(event2.getEventID(), 1, 5, "testReceiver2");

        DatabaseManager.object().insertRespondedEvent(receiver1event1response);
        DatabaseManager.object().insertRespondedEvent(receiver2event1response);
        DatabaseManager.object().insertRespondedEvent(receiver1event2response);
        DatabaseManager.object().insertRespondedEvent(receiver2event2response);

        assertEquals(event2.getEventID(), EventRecommender.GetRecommendedEvent(prop.getProposalID()));

        //no all availabilities test
        receiver1event1response = new EventResponse(event1.getEventID(), 0, 5, "testReceiver1");
        receiver2event1response = new EventResponse(event1.getEventID(), 0, 5, "testReceiver2");
        receiver1event2response = new EventResponse(event2.getEventID(), 0, 5, "testReceiver1");
        receiver2event2response = new EventResponse(event2.getEventID(), 1, 4, "testReceiver2");

        DatabaseManager.object().insertRespondedEvent(receiver1event1response);
        DatabaseManager.object().insertRespondedEvent(receiver2event1response);
        DatabaseManager.object().insertRespondedEvent(receiver1event2response);
        DatabaseManager.object().insertRespondedEvent(receiver2event2response);

        assertEquals(event2.getEventID(), EventRecommender.GetRecommendedEvent(prop.getProposalID()));

        //no availabilities test
        receiver1event1response = new EventResponse(event1.getEventID(), 0, 5, "testReceiver1");
        receiver2event1response = new EventResponse(event1.getEventID(), 0, 5, "testReceiver2");
        receiver1event2response = new EventResponse(event2.getEventID(), 0, 5, "testReceiver1");
        receiver2event2response = new EventResponse(event2.getEventID(), 0, 4, "testReceiver2");

        DatabaseManager.object().insertRespondedEvent(receiver1event1response);
        DatabaseManager.object().insertRespondedEvent(receiver2event1response);
        DatabaseManager.object().insertRespondedEvent(receiver1event2response);
        DatabaseManager.object().insertRespondedEvent(receiver2event2response);

        assertEquals("-1", EventRecommender.GetRecommendedEvent(prop.getProposalID()));

    }
}