package csci310.servlets;

import csci310.models.EventResponse;
import csci310.models.Response;
import csci310.utilities.DatabaseManager;
import csci310.utilities.HelperFunctions;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetResponseServletTest {

    @Test
    public void testDoGet() throws IOException {
        GetResponseServlet servlet = new GetResponseServlet();
        EventResponse eventResponse = new EventResponse("junitevent123456qwerty",0,5,"name");
        DatabaseManager.object().insertRespondedEvent(eventResponse);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        when(req.getParameter("event_id")).thenReturn(eventResponse.getEventID());
        when(req.getParameter("username")).thenReturn(eventResponse.getReceiverUsername());
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(printWriter);
        servlet.doGet(req,res);
        printWriter.flush();
        Response response = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertTrue(response.getStatus());

        when(req.getParameter("event_id")).thenReturn("notexists");
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(printWriter);
        servlet.doGet(req,res);
        printWriter.flush();
        Response response1 = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertFalse(response1.getStatus());
    }
}