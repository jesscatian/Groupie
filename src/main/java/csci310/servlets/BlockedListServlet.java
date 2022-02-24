package csci310.servlets;

import csci310.models.Response;
import csci310.utilities.BlockedListDatabase;
import csci310.utilities.HelperFunctions;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BlockedListServlet extends HttpServlet {
    private static final long serialVersionUID = 1;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String type = request.getParameter("type");
        String blocker = request.getParameter("blocker");
        String blockee = request.getParameter("blockee");

        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();

        Response res = new Response(true, "request fulfilled");

        try
        {
            ArrayList<String> tempBlockedList = BlockedListDatabase.GetBlockedList("test");
        } catch (SQLException e) {
            try {
                BlockedListDatabase.CreateDatabase();
                throw new SQLException();
            } catch (SQLException ex) {
            }
        }

        try
        {
            try
            {
                if (request.getParameter("throw").equalsIgnoreCase("true"))
                {
                    throw new SQLException();
                }
            }
            catch (NullPointerException e)
            {}

            if (type.equalsIgnoreCase("getBlocked"))
            {
                res.setData(String.valueOf(BlockedListDatabase.IsBlocking(blocker,blockee)));
                printWriter.print(HelperFunctions.shared().toJson(res,Response.class));
            }
            else if (type.equalsIgnoreCase("removeBlock"))
            {
                BlockedListDatabase.DeleteBlock(blocker, blockee);
                printWriter.print(HelperFunctions.shared().toJson(res,Response.class));
            }
            else if (type.equalsIgnoreCase("addBlock"))
            {
                BlockedListDatabase.AddBlock(blocker, blockee);
                printWriter.print(HelperFunctions.shared().toJson(res,Response.class));
            }
            else if (type.equalsIgnoreCase("getBlockList"))
            {
                ArrayList<String> blockedList = BlockedListDatabase.GetBlockedList(blocker);
                res.setData(blockedList);
                printWriter.print(HelperFunctions.shared().toJson(res,Response.class));
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
