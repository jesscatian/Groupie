package csci310.servlets;

import csci310.models.Response;
import csci310.models.User;
import csci310.utilities.*;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AvailabilityServletTest extends TestCase {

    private User testUser;

    @Before
    public void setUp() throws Exception {
        testUser = new User("availabilityTestUser","notApplicable");
        DatabaseManager.object().insertUser(testUser);
    }

    @Test
    public void testDoGet() throws IOException, SQLException {
        //Test adding unavailability
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("addUnavailability");
        when(request.getParameter("username")).thenReturn(testUser.getUsername());
        when(request.getParameter("start")).thenReturn("2031-12-20T14:55");
        when(request.getParameter("end")).thenReturn("2031-12-25T16:33");
        when(request.getParameter("throw")).thenReturn("false");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new AvailabilityServlet().doGet(request, response);

        writer.flush();
        Response res = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertTrue(res.getStatus());
        assertEquals("2031-12-20T14:55", DatabaseManager.object().getUnavailabilities(testUser.getUsername()).get(0).getStart());

        //Test getting unavailabilities
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("getUnavailabilities");
        when(request.getParameter("username")).thenReturn(testUser.getUsername());

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new AvailabilityServlet().doGet(request, response);

        writer.flush();
        res = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertTrue(res.getStatus());
        assertTrue(res.getData().toString().contains("2031-12-20T14:55"));

        //Test getting unavailabilities from nonexistent user
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("getUnavailabilities");
        String nonexistantUsername = "nonexistentUsername1";
        while (DatabaseManager.object().checkUserExists(nonexistantUsername) != null)
        {
            nonexistantUsername = nonexistantUsername + 1;
        }
        when(request.getParameter("username")).thenReturn(nonexistantUsername);

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new AvailabilityServlet().doGet(request, response);

        writer.flush();
        res = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertFalse(res.getStatus());


        //Test removing unavailability
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("removeUnavailability");
        when(request.getParameter("username")).thenReturn(testUser.getUsername());
        when(request.getParameter("index")).thenReturn("0");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new AvailabilityServlet().doGet(request, response);

        writer.flush();
        res = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertTrue(res.getStatus());
        assertEquals(0, DatabaseManager.object().getUnavailabilities(testUser.getUsername()).size());

        //Test removing nonexistent unavailability
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("removeUnavailability");
        when(request.getParameter("username")).thenReturn(testUser.getUsername());
        when(request.getParameter("index")).thenReturn("1");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new AvailabilityServlet().doGet(request, response);

        writer.flush();
        res = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertFalse(res.getStatus());

        //Test removing unavailability for nonexistent user
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("removeUnavailability");
        when(request.getParameter("username")).thenReturn(nonexistantUsername);
        when(request.getParameter("index")).thenReturn("1");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new AvailabilityServlet().doGet(request, response);

        writer.flush();
        res = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertFalse(res.getStatus());

        //Test removing negative index
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("removeUnavailability");
        when(request.getParameter("username")).thenReturn(testUser.getUsername());
        when(request.getParameter("index")).thenReturn("-1");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new AvailabilityServlet().doGet(request, response);

        writer.flush();
        res = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertFalse(res.getStatus());

        //Test adding bad time
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("addUnavailability");
        when(request.getParameter("username")).thenReturn(testUser.getUsername());
        when(request.getParameter("start")).thenReturn("2031-12-000000:55");
        when(request.getParameter("end")).thenReturn("2031-12-25T16:33");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new AvailabilityServlet().doGet(request, response);

        writer.flush();
        res = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertFalse(res.getStatus());
        assertEquals("invalid start time", res.getMessage());

        //Test adding bad time 2
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("addUnavailability");
        when(request.getParameter("username")).thenReturn(testUser.getUsername());
        when(request.getParameter("start")).thenReturn("2031-12-30T11:55");
        when(request.getParameter("end")).thenReturn("2031-12-25T16:33");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new AvailabilityServlet().doGet(request, response);

        writer.flush();
        res = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertFalse(res.getStatus());
        assertEquals("start must be before end", res.getMessage());

        //Test adding bad time 3
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("addUnavailability");
        when(request.getParameter("username")).thenReturn(testUser.getUsername());
        when(request.getParameter("start")).thenReturn("2031-12-30T11:55");
        when(request.getParameter("end")).thenReturn("2031-12-50T16:33");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new AvailabilityServlet().doGet(request, response);

        writer.flush();
        res = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertFalse(res.getStatus());
        assertEquals("invalid end time", res.getMessage());

        //Test adding bad time 4
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("addUnavailability");
        when(request.getParameter("username")).thenReturn(nonexistantUsername);
        when(request.getParameter("start")).thenReturn("2031-12-30T11:55");
        when(request.getParameter("end")).thenReturn("2031-12-31T16:33");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new AvailabilityServlet().doGet(request, response);

        writer.flush();
        res = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertFalse(res.getStatus());
        assertEquals("no user found", res.getMessage());


        //Exception testing
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("throw")).thenReturn("true");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new AvailabilityServlet().doGet(request, response);

        writer.flush();
        res = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertFalse(res.getStatus());
        assertEquals("exception occurred", res.getMessage());

        //invalid type
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("type")).thenReturn("hello");
        when(request.getParameter("blocker")).thenReturn("userBlocker");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new AvailabilityServlet().doGet(request, response);

        writer.flush();
        res = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertFalse(res.getStatus());
    }

    @After
    public void tearDown() throws Exception {
        DatabaseManager.object().deleteUser(testUser);
    }
}