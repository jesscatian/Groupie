package csci310.servlets;

import csci310.models.Event;
import csci310.models.EventResponse;
import csci310.models.Proposal;
import csci310.models.Response;
import csci310.utilities.DatabaseManager;
import csci310.utilities.HelperFunctions;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventRecommenderServletTest {

    @Test
    public void testDoGet() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new EventRecommenderServlet().doGet(request, response);


        Event event1 = new Event("event 1","2021-01-01","19:00:00","abc.com","music", UUID.randomUUID().toString());
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

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("proposalID")).thenReturn(prop.getProposalID());

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new EventRecommenderServlet().doGet(request, response);

        writer.flush();
        Response res = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertEquals(event1.getEventID(), res.getData());

        //No proposal id
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new EventRecommenderServlet().doGet(request, response);

        writer.flush();
        res = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertFalse(res.getStatus());
    }
}