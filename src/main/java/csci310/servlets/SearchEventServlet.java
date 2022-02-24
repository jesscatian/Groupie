package csci310.servlets;

import csci310.models.Events;
import csci310.models.RawResult;
import csci310.models.Response;
import csci310.utilities.HelperFunctions;
import csci310.utilities.databaseConfig;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SearchEventServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");

        String keyword = req.getParameter("keyword");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String countryCode = req.getParameter("countryCode");
        String genre = req.getParameter("genre");

        String urlString = databaseConfig.rootUrl + "&size=30"
                + (keyword != null ? "&keyword=" + keyword : "")
                + (countryCode != null ? "&countryCode=" + countryCode.toUpperCase() : "")
                + (genre != null ? "&classificationName=" + genre : "")
                + (startDate != null && endDate != null ? "&localStartDateTime=" + startDate + "T00:00:00" + "," + endDate + "T00:00:00" :
                    (startDate != null ? "&localStartDateTime=" + startDate + "T00:00:00" + ",*" :
                        (endDate != null ? "&localStartDateTime=*," + endDate + "T00:00:00" : "")));
        String apiResponse;

        Response errResponse = new Response(false,"No results returned for this query");

        try {
            apiResponse = HelperFunctions.get(urlString);
        } catch (IOException e) {
            resp.getWriter().println(HelperFunctions.shared().toJson(errResponse));
            return;
        }

        RawResult rawResult = HelperFunctions.shared().fromJson(apiResponse,RawResult.class);
        Events events;

        try {
            events = new Events(rawResult);
        } catch (Exception e) {
            resp.getWriter().println(HelperFunctions.shared().toJson(errResponse));
            return;
        }

        Response response = new Response(true,null,events);
        resp.getWriter().println(HelperFunctions.shared().toJson(response));
    }
}
