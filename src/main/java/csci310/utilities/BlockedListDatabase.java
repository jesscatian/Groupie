package csci310.utilities;

import java.sql.*;
import java.util.ArrayList;

public class BlockedListDatabase {
    final static String url = "jdbc:sqlite:src/main/resources/db.sqlite";
    public static void CreateDatabase() throws SQLException
    {
        Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement();
        stmt.execute("DROP TABLE IF EXISTS blockList");
        stmt.execute("CREATE TABLE IF NOT EXISTS blockList (\n" +
                "   blockerUsername TEXT NOT NULL,\n" +
                "   blockedUsername TEXT NOT NULL\n" +
                ");");
        stmt.close();
        conn.close();
    }

    public static void DeleteDatabase() throws SQLException
    {
        Connection conn = DriverManager.getConnection(url);

        Statement stmt = conn.createStatement();
        stmt.execute("DROP TABLE IF EXISTS blockList");
        stmt.close();

        conn.close();
    }

    public static void AddBlock(String blockerUsername, String blockedUsername)  throws SQLException
    {
        if (blockerUsername.length() > 2 && blockedUsername.length() > 2) {
            Connection conn = DriverManager.getConnection(url);
            String s = "INSERT INTO blockList(blockerUsername, blockedUsername) VALUES( ?, ? );";
            PreparedStatement stmt = conn.prepareStatement(s);
            stmt.setString(1, blockerUsername);
            stmt.setString(2, blockedUsername);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        }
    }
    public static void DeleteBlock(String blockerUsername, String blockedUsername) throws SQLException
    {
        Connection conn = DriverManager.getConnection(url);
        String s = "DELETE FROM blockList WHERE blockerUsername= ? AND blockedUsername = ? ;";
        PreparedStatement stmt = conn.prepareStatement(s);
        stmt.setString(1, blockerUsername);
        stmt.setString(2, blockedUsername);
        stmt.executeUpdate();

        stmt.close();
        conn.close();
    }

    public static ArrayList<String> GetBlockedList(String blockerUsername)  throws SQLException
    {
        Connection conn = DriverManager.getConnection(url);

        String s = "SELECT blockedUsername FROM blockList where blockerUsername= ? ;";
        PreparedStatement stmt = conn.prepareStatement(s);
        stmt.setString(1, blockerUsername);
        ResultSet res = stmt.executeQuery();

        ArrayList<String> blockedList = new ArrayList<String>();

        while (res.next())
        {
            blockedList.add(res.getString("blockedUsername"));
        }
        stmt.close();
        res.close();
        conn.close();
        return blockedList;
    }

    public static boolean IsBlocking(String blockerUsername, String blockedUsername) throws SQLException
    {
        try {
            Connection conn = DriverManager.getConnection(url);

            String s2 = "SELECT * FROM blockList WHERE blockerUsername= ? AND blockedUsername = ? ;";
            PreparedStatement stmt = conn.prepareStatement(s2);
            stmt.setString(1, blockerUsername);
            stmt.setString(2, blockedUsername);

            boolean returnVal = false;
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                returnVal = true;
            }
            stmt.close();
            res.close();
            conn.close();
            return returnVal;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}