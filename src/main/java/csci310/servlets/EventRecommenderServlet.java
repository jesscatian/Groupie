package csci310.servlets;

import csci310.models.Response;
import csci310.utilities.BlockedListDatabase;
import csci310.utilities.EventRecommender;
import csci310.utilities.HelperFunctions;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

public class EventRecommenderServlet extends HttpServlet {
    private static final long serialVersionUID = 1;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String proposalID = request.getParameter("proposalID");

        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();

        Response res = new Response(true, "request fulfilled");

        if (proposalID != null)
        {
            res.setData(EventRecommender.GetRecommendedEvent(proposalID));
            printWriter.print(HelperFunctions.shared().toJson(res,Response.class));
        }
        else
        {
            printWriter.print(HelperFunctions.shared().toJson(new Response(false, "requires proposalID"),Response.class));
        }

        printWriter.flush();
    }
}
