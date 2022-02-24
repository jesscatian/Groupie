package csci310.servlets;

import csci310.models.Event;
import csci310.models.Proposal;
import csci310.models.Response;
import csci310.utilities.HelperFunctions;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SendProposalServletTest {

    private SendProposalServlet servlet;

    @Before
    public void setUp() {
        servlet = new SendProposalServlet();
    }

    @Test
    public void testDoPost() throws IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        Event event1 = new Event("eventName1","2000-01-01","01:00:00","abc.com","movie");
        Proposal proposal = new Proposal("proposalsendertest",
                "senderNametester",
                new ArrayList<>(Arrays.asList("inviteeNametester1")),
                new ArrayList<>(Arrays.asList(event1)));
        BufferedReader bufferedReader = new BufferedReader(new StringReader(HelperFunctions.shared().toJson(proposal,Proposal.class)));
        when(req.getReader()).thenReturn(bufferedReader);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(printWriter);
        servlet.doPost(req,res);
        printWriter.flush();
        Response response = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        proposal.setFinalizedEventID("1");
        proposal.getFinalizedEventID();
        assertTrue(response.getStatus());
    }
}