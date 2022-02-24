package csci310.servlets;

import csci310.models.Response;
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

public class SearchEventServletTest {
    private SearchEventServlet servlet;

    @Before
    public void setUp() {
        servlet = new SearchEventServlet();
    }

    @Test
    public void testDoGet_success() throws IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        when(req.getParameter("keyword")).thenReturn("music");
        when(req.getParameter("startDate")).thenReturn("2020-03-01");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(writer);
        servlet.doGet(req,res);
        writer.flush();
        Response response = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertNotNull(response);
    }

    @Test
    public void testDoGet_failEmptyResult() throws IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        when(req.getParameter("keyword")).thenReturn("nosuchkeyword");
        when(req.getParameter("genre")).thenReturn("nosuchgenre");
        when(req.getParameter("startDate")).thenReturn("2021-01-03");
        when(req.getParameter("endDate")).thenReturn("2021-01-04");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(writer);
        servlet.doGet(req,res);
        writer.flush();
        Response response = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertNotNull(response);
    }

    @Test
    public void testDoGet_failInvalidDateRange() throws IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        when(req.getParameter("endDate")).thenReturn("0000");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(writer);
        servlet.doGet(req,res);
        writer.flush();
        Response response = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertNotNull(response);
    }

    @Test
    public void testDoGet_successEndDateAndCountryCode() throws IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        when(req.getParameter("countryCode")).thenReturn("US");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(writer);
        servlet.doGet(req,res);
        writer.flush();
        Response response = HelperFunctions.shared().fromJson(stringWriter.toString(),Response.class);
        assertNotNull(response);
    }
}