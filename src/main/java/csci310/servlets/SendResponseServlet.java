package csci310.servlets;

import csci310.models.EventResponse;
import csci310.models.Response;
import csci310.utilities.DatabaseManager;
import csci310.utilities.HelperFunctions;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SendResponseServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        EventResponse eventResponse = HelperFunctions.shared().fromJson(req.getReader(),EventResponse.class);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");

        DatabaseManager.object().insertRespondedEvent(eventResponse);
        Response response = new Response(true);
        resp.getWriter().println(HelperFunctions.shared().toJson(response));
    }
}
