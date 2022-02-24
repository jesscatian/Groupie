package csci310.servlets;

import csci310.models.Response;
import csci310.utilities.DatabaseManager;
import csci310.utilities.HelperFunctions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetFinalResponseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        System.out.println("test");
        String username = req.getParameter("username");
        String proposalID = req.getParameter("proposal_id");
        Boolean finalResponse = DatabaseManager.object().getFinalResponse(proposalID,username);
        Response response;
        if (finalResponse == null) {
            response = new Response(false,"The user has not made a final response");
        } else {
            response = new Response(true,null,finalResponse);
        }
        resp.getWriter().print(HelperFunctions.shared().toJson(response));
    }
}
