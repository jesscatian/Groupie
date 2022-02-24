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

public class SendFinalizedProposalServletTest {

    @Test
    public void testDoPost() throws IOException, ServletException {
        SendFinalizedProposalServlet servlet = new SendFinalizedProposalServlet();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        when(req.getParameter("finalized_event_id")).thenReturn("1sf");
        when(req.getParameter("proposal_id")).thenReturn("dfrge");
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(printWriter);
        servlet.doPost(req,res);
        printWriter.flush();
        Response response = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertTrue(response.getStatus());
    }
}