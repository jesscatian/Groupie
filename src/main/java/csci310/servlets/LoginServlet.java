package csci310.servlets;

import csci310.models.Response;
import csci310.models.User;
import csci310.utilities.DatabaseManager;
import csci310.utilities.HelperFunctions;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        String username = req.getParameter("username");
        String psw = req.getParameter("psw");
        User dbResponse = DatabaseManager.object().verifyUser(new User(username, psw));
        if (dbResponse != null) {
            Response response = new Response(true,null,dbResponse);
            resp.getWriter().println(HelperFunctions.shared().toJson(response));

        } else {
            Response response = new Response<>(false, "Either username or password is wrong.");
            resp.getWriter().println(HelperFunctions.shared().toJson(response));
        }
    }
}
