package csci310.servlets;

import csci310.models.Response;
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

public class SendFinalResponseServletTest {

    @Test
    public void testDoPost_accept() throws IOException, ServletException {
        SendFinalResponseServlet servlet = new SendFinalResponseServlet();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        when(req.getParameter("final_response")).thenReturn("1");
        when(req.getParameter("username")).thenReturn("123");
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(printWriter);
        servlet.doPost(req,res);
        printWriter.flush();
        Response response = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertTrue(response.getStatus());

        when(req.getParameter("final_response")).thenReturn("0");
        when(req.getParameter("username")).thenReturn("123");
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(printWriter);
        servlet.doPost(req,res);
    }

    @Test
    public void testDoPost_decline() throws IOException, ServletException {
        SendFinalResponseServlet servlet = new SendFinalResponseServlet();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        when(req.getParameter("final_response")).thenReturn("0");
        when(req.getParameter("username")).thenReturn("user1");
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(printWriter);
        servlet.doPost(req,res);
        printWriter.flush();
        Response response = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertTrue(response.getStatus());
    }
}