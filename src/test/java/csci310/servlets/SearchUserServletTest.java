package csci310.servlets;

import csci310.models.Response;
import csci310.models.User;
import csci310.utilities.DatabaseManager;
import csci310.utilities.HelperFunctions;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchUserServletTest {
    private SearchUserServlet servlet;

    @Before
    public void setUp() {
        servlet = new SearchUserServlet();
    }

    @Test
    public void testDoGet_success() throws IOException {
        DatabaseManager.object().insertUser(new User("user1","123"));
        DatabaseManager.object().insertUser(new User("user2","123"));
        DatabaseManager.object().insertUser(new User("3user","123"));
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        when(req.getParameter("q")).thenReturn("user");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(writer);
        servlet.doGet(req,res);
        writer.flush();
        Response response = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertTrue(response.getStatus());
        assertNull(response.getMessage());
        assertNotNull(response.getData());
    }

    @Test
    public void testDoGet_fail() throws IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        when(req.getParameter("q")).thenReturn("somerandomnonexistentuser");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(writer);
        servlet.doGet(req,res);
        writer.flush();
        Response response = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertFalse(response.getStatus());
        assertEquals("No results returned for this query",response.getMessage());
        assertNull(response.getData());
    }
}