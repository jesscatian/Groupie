package csci310.servlets;

import csci310.models.Response;
import csci310.utilities.DatabaseManager;
import csci310.utilities.HelperFunctions;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetFinalResponseServletTest {

    @Test
    public void testDoGet_exists() throws IOException, ServletException {
        DatabaseManager.object().insertReceivedProposal("receiver","id233333");

        GetFinalResponseServlet servlet = new GetFinalResponseServlet();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        when(req.getParameter("username")).thenReturn("receiver");
        when(req.getParameter("proposal_id")).thenReturn("id233333");
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(printWriter);
        servlet.doGet(req,res);
        printWriter.flush();
        Response response = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertTrue(response.getStatus() || (response.getStatus() == false && response.getMessage().equalsIgnoreCase("The user has not made a final response")));

        when(req.getParameter("username")).thenReturn("receiver");
        when(req.getParameter("proposal_id")).thenReturn("123456");
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(printWriter);
        servlet.doGet(req,res);
        printWriter.flush();
        response = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertFalse(response.getStatus());
    }

    @Test
    public void testDoGet_notExists() throws IOException, ServletException {
        GetFinalResponseServlet servlet = new GetFinalResponseServlet();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        when(req.getParameter("username")).thenReturn("receiver2");
        when(req.getParameter("proposal_id")).thenReturn("123456");
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(printWriter);
        servlet.doGet(req,res);
        printWriter.flush();
        Response response = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertFalse(response.getStatus());
    }
}