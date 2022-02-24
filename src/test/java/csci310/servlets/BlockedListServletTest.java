package csci310.servlets;

import csci310.models.Response;
import csci310.utilities.BlockedListDatabase;
import csci310.utilities.HelperFunctions;
import junit.framework.TestCase;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class BlockedListServletTest extends TestCase {

    @Test
    public void testDoGet() throws IOException, SQLException {
        BlockedListDatabase.DeleteDatabase();

        //default not blocked
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("getBlocked");
        when(request.getParameter("blocker")).thenReturn("userBlocker");
        when(request.getParameter("blockee")).thenReturn("userBlockee");
        when(request.getParameter("throw")).thenReturn("false");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new BlockedListServlet().doGet(request, response);

        writer.flush();
        Response res = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertTrue(res.getStatus());

        //Test correctly getting a block
        BlockedListDatabase.AddBlock("userBlocker", "userBlockee");

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("getBlocked");
        when(request.getParameter("blocker")).thenReturn("userBlocker");
        when(request.getParameter("blockee")).thenReturn("userBlockee");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new BlockedListServlet().doGet(request, response);

        writer.flush();
        res = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertTrue(res.getStatus());

        //Test removing block
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("removeBlock");
        when(request.getParameter("blocker")).thenReturn("userBlocker");
        when(request.getParameter("blockee")).thenReturn("userBlockee");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new BlockedListServlet().doGet(request, response);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("getBlocked");
        when(request.getParameter("blocker")).thenReturn("userBlocker");
        when(request.getParameter("blockee")).thenReturn("userBlockee");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new BlockedListServlet().doGet(request, response);

        writer.flush();
        res = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertEquals("false", res.getData());

        //Test adding block
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("addBlock");
        when(request.getParameter("blocker")).thenReturn("userBlocker");
        when(request.getParameter("blockee")).thenReturn("userBlockee");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new BlockedListServlet().doGet(request, response);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("getBlocked");
        when(request.getParameter("blocker")).thenReturn("userBlocker");
        when(request.getParameter("blockee")).thenReturn("userBlockee");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new BlockedListServlet().doGet(request, response);

        writer.flush();
        res = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertEquals("true",res.getData());

        //Test getting block list
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("addBlock");
        when(request.getParameter("blocker")).thenReturn("userBlocker");
        when(request.getParameter("blockee")).thenReturn("userBlockee2");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new BlockedListServlet().doGet(request, response);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("getBlockList");
        when(request.getParameter("blocker")).thenReturn("userBlocker");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new BlockedListServlet().doGet(request, response);

        writer.flush();
        Response<ArrayList<String>> res1 = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertTrue(res.getStatus());
        assertEquals("userBlockee", res1.getData().get(0));

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("hello");
        when(request.getParameter("blocker")).thenReturn("userBlocker");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new BlockedListServlet().doGet(request, response);

        writer.flush();
        res = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertFalse(res.getStatus());
        assertEquals("invalid parameter", res.getMessage());

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("throw")).thenReturn("true");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new BlockedListServlet().doGet(request, response);

        writer.flush();
        res = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertFalse(res.getStatus());
        assertEquals("exception occurred", res.getMessage());
    }
}