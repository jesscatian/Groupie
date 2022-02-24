package csci310.servlets;

import csci310.models.Response;
import csci310.utilities.DatabaseManager;
import csci310.utilities.HelperFunctions;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class SearchUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        String usernameSubstring = req.getParameter("q");

        ArrayList<String> dbResponse = DatabaseManager.object().searchUsers(usernameSubstring);
        if (dbResponse != null) {
            Response response = new Response(true,null,dbResponse);
            resp.getWriter().println(HelperFunctions.shared().toJson(response));

        } else {
            Response response = new Response<>(false, "No results returned for this query");
            resp.getWriter().println(HelperFunctions.shared().toJson(response));
        }
    }
}
