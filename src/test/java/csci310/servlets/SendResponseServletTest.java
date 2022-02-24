package csci310.servlets;

import csci310.models.Event;
import csci310.models.EventResponse;
import csci310.models.Response;
import csci310.utilities.DatabaseManager;
import csci310.utilities.HelperFunctions;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SendResponseServletTest {

    @Test
    public void testDoPost() throws IOException {
        String eventID = "junitevent123456qwerty";
        SendResponseServlet servlet = new SendResponseServlet();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        Event event = new Event("event 1","2021-01-01","19:00:00","abc.com","music",eventID);
        DatabaseManager.object().insertEvent(event,"proposalID");
        EventResponse eventResponse = new EventResponse(eventID,1,5,"name");
        eventResponse.setEventID(eventID);
        eventResponse.setAvailability(1);
        eventResponse.setReceiverUsername("name");
        BufferedReader bufferedReader = new BufferedReader(new StringReader(HelperFunctions.shared().toJson(eventResponse, EventResponse.class)));
        when(req.getReader()).thenReturn(bufferedReader);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(printWriter);
        servlet.doPost(req,res);
        printWriter.flush();
        Response response = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertTrue(response.getStatus());
    }
}