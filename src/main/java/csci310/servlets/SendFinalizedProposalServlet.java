package csci310.servlets;

import csci310.models.Response;
import csci310.utilities.DatabaseManager;
import csci310.utilities.HelperFunctions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SendFinalizedProposalServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        String finalizedEventID = req.getParameter("finalized_event_id");
        String proposalID = req.getParameter("proposal_id");
        DatabaseManager.object().updateFinalizedProposal(finalizedEventID,proposalID);

        Response response = new Response(true);
        resp.getWriter().print(HelperFunctions.shared().toJson(response));
    }
}
