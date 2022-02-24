package csci310.servlets;

import csci310.models.Event;
import csci310.models.Proposal;
import csci310.models.Response;
import csci310.utilities.DatabaseManager;
import csci310.utilities.HelperFunctions;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class GetProposalServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");

        ArrayList<Proposal> proposals = new ArrayList<>();
        Boolean isSent = req.getParameter("type").equals("sent");
        String username = req.getParameter("username");
        ArrayList<String> proposalIDs = DatabaseManager.object().getProposalIDs(username,isSent);

        for (String proposalID: proposalIDs) {
            ArrayList<String> receiverUsernames = DatabaseManager.object().getProposalReciever(proposalID);
            ArrayList<Event> events = DatabaseManager.object().getProposalEvents(proposalID);
            String proposalTitle = DatabaseManager.object().getProposalTitle(proposalID);
            String finalizedEventID = DatabaseManager.object().getFinalizedProposal(proposalID);
            if (isSent) {
                proposals.add(new Proposal(proposalTitle,username,receiverUsernames,events,proposalID,finalizedEventID));
            } else {
                String senderUsername = DatabaseManager.object().getProposalSender(proposalID);
                proposals.add(new Proposal(proposalTitle,senderUsername,receiverUsernames,events,proposalID,finalizedEventID));
            }
        }

        Response response = new Response(true,null,proposals);
        resp.getWriter().println(HelperFunctions.shared().toJson(response));
    }
}
