package csci310.servlets;

import csci310.models.EventResponse;
import csci310.models.Response;
import csci310.utilities.DatabaseManager;
import csci310.utilities.HelperFunctions;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetResponseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        String username = req.getParameter("username");
        String eventID = req.getParameter("event_id");
        EventResponse eventResponse = DatabaseManager.object().getRespondedEvent(username,eventID);
        if (eventResponse != null) {
            Response response = new Response(true, null, eventResponse);
            resp.getWriter().print(HelperFunctions.shared().toJson(response));
        } else {
            Response response = new Response(false,"Either the event or the response to the event does not exist.");
            resp.getWriter().print(HelperFunctions.shared().toJson(response));
        }
    }
}
