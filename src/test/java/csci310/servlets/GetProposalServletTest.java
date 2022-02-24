package csci310.servlets;

import csci310.models.Event;
import csci310.models.Proposal;
import csci310.models.Response;
import csci310.utilities.HelperFunctions;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetProposalServletTest {

    static GetProposalServlet servlet;
    static SendProposalServlet sendServlet;

    @BeforeClass
    public static void setUp() throws IOException {
        servlet = new GetProposalServlet();
        sendServlet = new SendProposalServlet();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        String senderUsername = "senderNametester",inviteeUsername = "inviteeNametester1";
        Event event1 = new Event("eventName1","2000-01-01","01:00:00","abc.com","movie");
        Proposal proposal = new Proposal("proposalsendertest",
                senderUsername,
                new ArrayList<>(Arrays.asList(inviteeUsername)),
                new ArrayList<>(Arrays.asList(event1)));
        BufferedReader bufferedReader = new BufferedReader(new StringReader(HelperFunctions.shared().toJson(proposal,Proposal.class)));
        when(req.getReader()).thenReturn(bufferedReader);
        when(res.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        sendServlet.doPost(req,res);
    }

    @Test
    public void testDoGet_sentProposals() throws IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        when(req.getParameter("type")).thenReturn("sent");
        when(req.getParameter("username")).thenReturn("senderNametester");
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(printWriter);
        servlet.doGet(req,res);
        printWriter.flush();
        Response response = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertTrue(response.getStatus());
        assertTrue(response.getData().toString().contains("inviteeNametester1"));
    }

    @Test
    public void testDoGet_receivedProposals() throws IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        when(req.getParameter("type")).thenReturn("received");
        when(req.getParameter("username")).thenReturn("inviteeNametester1");
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(printWriter);
        servlet.doGet(req,res);
        printWriter.flush();
        Response response = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertTrue(response.getStatus());
        assertTrue(response.getData().toString().contains("2000-01-01"));
    }
}