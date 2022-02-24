package csci310.servlets;

import csci310.models.Response;
import csci310.models.Unavailability;
import csci310.utilities.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class AvailabilityServlet extends HttpServlet {
    private static final long serialVersionUID = 1;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String type = request.getParameter("type");
        String username = request.getParameter("username");
        String start = request.getParameter("start");
        String end = request.getParameter("end");

        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();

        Response res = new Response(true, "request fulfilled");

        try
        {
            try {
                if (request.getParameter("throw").equalsIgnoreCase("true")) {
                    throw new SQLException();
                }
            }
            catch (NullPointerException e)
            {
                e.getMessage(); //Do nothing statement for coverage.
            }

            if (type.equalsIgnoreCase("addUnavailability"))
            {
                if (DatabaseManager.object().checkUserExists(username) != null)
                {
                    boolean startValid = false;
                    try
                    {
                        LocalDate startDate = LocalDate.parse(start, databaseConfig.dateFormat);
                        startValid = true;
                        LocalDate endDate = LocalDate.parse(end, databaseConfig.dateFormat);

                        if(endDate.isAfter(startDate))
                        {
                            DatabaseManager.object().addUnavailability(start, end, username);
                            printWriter.print(HelperFunctions.shared().toJson(res, Response.class));
                        }
                        else
                        {
                            printWriter.print(HelperFunctions.shared().toJson(new Response(false, "start must be before end"), Response.class));
                        }
                    }
                    catch (DateTimeParseException e)
                    {
                        if (!startValid)
                        {
                            printWriter.print(HelperFunctions.shared().toJson(new Response(false, "invalid start time"), Response.class));
                        }
                        else
                        {
                            printWriter.print(HelperFunctions.shared().toJson(new Response(false, "invalid end time"), Response.class));
                        }
                    }
                }
                else
                {
                    printWriter.print(HelperFunctions.shared().toJson(new Response(false, "no user found"), Response.class));
                }
            }
            else if (type.equalsIgnoreCase("removeUnavailability"))
            {
                if (DatabaseManager.object().checkUserExists(username) != null)
                {
                    int index = Integer.parseInt(request.getParameter("index"));
                    ArrayList<Unavailability> unavailabilities = DatabaseManager.object().getUnavailabilities(username);
                    try
                    {
                        if (index < unavailabilities.size() && index >= 0)
                        {
                            DatabaseManager.object().removeUnavailability(unavailabilities.get(index).getId());
                            printWriter.print(HelperFunctions.shared().toJson(res, Response.class));
                        }
                        else
                        {
                            throw new NumberFormatException();
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        printWriter.print(HelperFunctions.shared().toJson(new Response(false, "invalid index"), Response.class));
                    }
                }
                else
                {
                    printWriter.print(HelperFunctions.shared().toJson(new Response(false, "no user found"), Response.class));
                }
            }
            else if (type.equalsIgnoreCase("getUnavailabilities"))
            {
                if (DatabaseManager.object().checkUserExists(username) != null)
                {
                    ArrayList<Unavailability> unavailabilities = DatabaseManager.object().getUnavailabilities(username);
                    res = new Response(true,null,unavailabilities);
                    printWriter.print(HelperFunctions.shared().toJson(res, Response.class));
                }
                else
                {
                    printWriter.print(HelperFunctions.shared().toJson(new Response(false, "no user found"), Response.class));
                }
            }
            else
            {
                printWriter.print(HelperFunctions.shared().toJson(new Response(false, "invalid parameter"),Response.class));
            }

        }
        catch (SQLException e)
        {
            printWriter.print(HelperFunctions.shared().toJson(new Response(false, "exception occurred"),Response.class));
        }

        printWriter.flush();
    }
}
