package csci310.utilities;

import csci310.models.Event;
import csci310.models.EventResponse;
import csci310.models.Unavailability;
import csci310.models.User;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {

    private static DatabaseManager databaseManager;
    private Connection con;

    private PreparedStatement checkUserExistsPs;
    private PreparedStatement insertUserPs;
    private PreparedStatement verifyUserPs;
    private PreparedStatement deleteUserPs;

    private PreparedStatement searchUsersPs;

    private PreparedStatement insertSentProposalPs;
    private PreparedStatement insertReceivedProposalPs;
    private PreparedStatement insertEventPs;

    private PreparedStatement updateFinalizedProposalPs;
    private PreparedStatement getFinalizedProposalPs;
    private PreparedStatement updateFinalResponsePs;
    private PreparedStatement getFinalResponsePs;

    private PreparedStatement getSentProposalIDsPs;
    private PreparedStatement getReceivedProposalIDsPs;
    private PreparedStatement getProposalTitlePs;
    private PreparedStatement getProposalSenderPs;
    private PreparedStatement getProposalReceiversPs;
    private PreparedStatement getProposalEventsPs;

    private PreparedStatement insertRespondedEventPs;
    private PreparedStatement getRespondedEventPs;
    private PreparedStatement updateRespondedEventPs;

    private PreparedStatement getUnavailabilitiesPs;
    private PreparedStatement addUnavailabilityPs;
    private PreparedStatement removeUnavailabilityPs;

    private DatabaseManager() {
        try {
            con = DriverManager.getConnection(databaseConfig.sqliteUrl);

            createTablesIfNotExist();
            checkUserExistsPs = con.prepareStatement("select Salt from Users where Username = ?");
            insertUserPs = con.prepareStatement("insert into Users (Username,Salt,Hash) values (?,?,?)");
            verifyUserPs = con.prepareStatement("select Username from Users where Username = ? and Hash = ?");
            deleteUserPs = con.prepareStatement("delete from Users where Username = ?");

            searchUsersPs = con.prepareStatement("select Username from Users where Username like ?");

            insertSentProposalPs = con.prepareStatement("insert into SentProposals (ProposalID, ProposalTitle, SenderUsername) values (?,?,?)");
            insertReceivedProposalPs = con.prepareStatement("insert into ReceivedProposals (ReceiverUsername, ProposalID) values (?,?)");
            insertEventPs = con.prepareStatement("insert into Events (EventID, EventName, EventDate, EventTime, EventUrl, EventGenre, ProposalID) values (?,?,?,?,?,?,?)");

            updateFinalizedProposalPs = con.prepareStatement("update SentProposals set FinalizedEventID = ? where ProposalID = ?");
            getFinalizedProposalPs = con.prepareStatement("select FinalizedEventID from SentProposals where ProposalID = ?");
            updateFinalResponsePs = con.prepareStatement("update ReceivedProposals set FinalResponse = ? where ProposalID = ? and ReceiverUsername = ?");
            getFinalResponsePs = con.prepareStatement("select FinalResponse from ReceivedProposals where ProposalID = ? and ReceiverUsername = ?");

            getSentProposalIDsPs = con.prepareStatement("select ProposalID from SentProposals where SenderUsername = ?");
            getReceivedProposalIDsPs = con.prepareStatement("select ProposalID from ReceivedProposals where ReceiverUsername = ?");
            getProposalTitlePs = con.prepareStatement("select ProposalTitle from SentProposals where ProposalID = ?");
            getProposalSenderPs = con.prepareStatement("select SenderUsername from SentProposals where ProposalID = ?");
            getProposalReceiversPs = con.prepareStatement("select ReceiverUsername from ReceivedProposals where ProposalID = ?");
            getProposalEventsPs = con.prepareStatement("select EventID, EventName, EventDate, EventTime, EventUrl, EventGenre from Events where ProposalID = ?");

            insertRespondedEventPs = con.prepareStatement("insert into RespondedEvents (ReceiverUsername,Excitement,Availability,EventID) values (?,?,?,?)");
            getRespondedEventPs = con.prepareStatement("select Excitement,Availability from RespondedEvents where ReceiverUsername = ? and EventID = ?");
            updateRespondedEventPs = con.prepareStatement("update RespondedEvents set Excitement = ?, Availability = ? where ReceiverUsername = ? and EventID = ?");

            getUnavailabilitiesPs = con.prepareStatement("select UnavailabilityID, Start, End from Unavailabilities where Username = ?");
            addUnavailabilityPs = con.prepareStatement("insert into Unavailabilities(Start, End, Username) values (?, ?, ?)");
            removeUnavailabilityPs = con.prepareStatement("delete from Unavailabilities where UnavailabilityID=?");

            throw new SQLException();
        } catch (SQLException e) {}
    }

    private void createTablesIfNotExist() throws SQLException {
        Statement st = con.createStatement();
        st.execute("create table if not exists Users (" +
                "Username text primary key," +
                "Salt text not null," +
                "Hash text not null" +
                ");");
        st.execute("create table if not exists SentProposals (" +
                "ProposalID text primary key," +
                "ProposalTitle text not null," +
                "SenderUsername text not null," +
                "FinalizedEventID text" +
                ");");
        st.execute("create table if not exists ReceivedProposals (" +        // each receiver has a copy of the proposal
                "ReceiverUsername text not null," +
                "ProposalID text not null," +
                "FinalResponse integer," +                               // 0 means no, 1 means yes
                "foreign key(ProposalID) references SentProposals(ProposalID)," +
                "primary key(ReceiverUsername, ProposalID)" +
                ");");
        st.execute("create table if not exists Events (" +               // a list of event contained in a proposal
                "EventID text primary key," +
                "EventName text not null," +
                "EventDate text not null," +
                "EventTime text not null," +
                "EventUrl text not null," +
                "EventGenre text not null," +
                "ProposalID text not null," +
                "foreign key(ProposalID) references SentProposals(ProposalID)" +
                ");");
        st.execute("create table if not exists RespondedEvents (" +        // each receiver has a copy of the event to respond, only insert/update when someone posts/modifies rating
                "ReceiverUsername text not null," +
                "Excitement integer not null," +                        // 1-5 scale
                "Availability integer not null," +           // 1 for yes; 0 for no
                "EventID text not null," +
                "foreign key(EventID) references Events(EventID)," +
                "primary key(ReceiverUsername, EventID)" +
                ");");
        st.execute("create table if not exists Unavailabilities (" +
                "UnavailabilityID integer not null primary key," +
                "Start text not null," +
                "End text not null," +
                "Username text not null," +
                "foreign key(Username) references Users(Username)" +
                ");");
    }

    public static DatabaseManager object() {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }
        return databaseManager;
    }

    // Authentication

    public String checkUserExists(String username) {
        try {
            checkUserExistsPs.setString(1,username);
            ResultSet rs = checkUserExistsPs.executeQuery();
            if (rs.next())
                return rs.getString(1);
            throw new SQLException();
        } catch (SQLException e) {}
        return null;
    }

    public void insertUser(User user) {
        String salt = HelperFunctions.getSalt();
        String hash = HelperFunctions.getSecurePassword(user.getPsw(),salt,"SHA-512");
        try {
            insertUserPs.setString(1,user.getUsername());
            insertUserPs.setString(2,salt);
            insertUserPs.setString(3,hash);
            insertUserPs.executeUpdate();

        } catch (SQLException e) {}
    }

    public User verifyUser(User user) {
        try {
            checkUserExistsPs.setString(1,user.getUsername());
            ResultSet rs = checkUserExistsPs.executeQuery();
            if (rs.next()) {
                verifyUserPs.setString(1,user.getUsername());
                String salt = rs.getString(1);
                String hash = HelperFunctions.getSecurePassword(user.getPsw(),salt,"SHA-512");
                verifyUserPs.setString(2,hash);
                ResultSet rs2 = verifyUserPs.executeQuery();
                if (rs2.next())
                    return user;
            }
            throw new SQLException();
        } catch (SQLException e) {}
        return null;
    }

    public void deleteUser(User user) {
        try {
            if (checkUserExists(user.getUsername()) != null) {
                deleteUserPs.setString(1,user.getUsername());
                deleteUserPs.executeUpdate();
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {}
    }

    // Search User

    public ArrayList<String> searchUsers(String usernameSubstring) {
        ArrayList<String> users = new ArrayList<>();
        try {
            searchUsersPs.setString(1,"%"+usernameSubstring+"%");
            ResultSet rs = searchUsersPs.executeQuery();
            while (rs.next())
                users.add(rs.getString(1));
            if (!users.isEmpty())
                return users;
            throw new SQLException();
        } catch (SQLException e) {}
        return null;
    }

    // Send Proposal

    public void insertSentProposal(String proposalID, String proposalTitle, String senderUsername) {
        try {
            insertSentProposalPs.setString(1,proposalID);
            insertSentProposalPs.setString(2,proposalTitle);
            insertSentProposalPs.setString(3,senderUsername);
            insertSentProposalPs.executeUpdate();
        } catch (SQLException e) {}
    }

    public void insertReceivedProposal(String receiverUsername, String proposalID) {
        try {
            insertReceivedProposalPs.setString(1,receiverUsername);
            insertReceivedProposalPs.setString(2,proposalID);
            insertReceivedProposalPs.executeUpdate();
        } catch (SQLException e) {}
    }

    public void insertEvent(Event event, String proposalID) {
        try {
            insertEventPs.setString(1,event.getEventID());
            insertEventPs.setString(2,event.getName());
            insertEventPs.setString(3,event.getDate());
            insertEventPs.setString(4,event.getTime());
            insertEventPs.setString(5,event.getUrl());
            insertEventPs.setString(6,event.getGenre());
            insertEventPs.setString(7,proposalID);
            insertEventPs.executeUpdate();
        } catch (SQLException e) {}
    }

    // Get Sent and Received Proposal

    public ArrayList<String> getProposalIDs(String username, Boolean isSent) {
        ArrayList<String> proposalIDs = new ArrayList<>();
        try {
            ResultSet rs;
            if (isSent) {
                getSentProposalIDsPs.setString(1, username);
                rs = getSentProposalIDsPs.executeQuery();
            } else {
                getReceivedProposalIDsPs.setString(1, username);
                rs = getReceivedProposalIDsPs.executeQuery();
            }
            while (rs.next())
                proposalIDs.add(rs.getString(1));
            throw new SQLException();
        } catch (SQLException e) {}
        return proposalIDs;
    }

    public String getProposalTitle(String proposalID) {
        String proposalTitle = null;
        try {
            getProposalTitlePs.setString(1,proposalID);
            ResultSet rs = getProposalTitlePs.executeQuery();
            proposalTitle = rs.getString(1);
            throw new SQLException();
        } catch (SQLException e) {}
        return proposalTitle;
    }

    public String getProposalSender(String proposalID) {
        String senderUsername = null;
        try {
            getProposalSenderPs.setString(1, proposalID);
            ResultSet rs = getProposalSenderPs.executeQuery();
            senderUsername = rs.getString(1);
            throw new SQLException();
        } catch (SQLException e) {}
        return senderUsername;
    }

    public ArrayList<String> getProposalReciever(String proposalID) {
        ArrayList<String> usernames = new ArrayList<>();
        try {
            getProposalReceiversPs.setString(1,proposalID);
            ResultSet rs = getProposalReceiversPs.executeQuery();
            while (rs.next())
                usernames.add(rs.getString(1));
            throw new SQLException();
        } catch (SQLException e) {}
        return usernames;
    }

    public ArrayList<Event> getProposalEvents(String proposalID) {
        ArrayList<Event> events = new ArrayList<>();
        try {
            getProposalEventsPs.setString(1,proposalID);
            ResultSet rs = getProposalEventsPs.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);
                String name = rs.getString(2);
                String date = rs.getString(3);
                String time = rs.getString(4);
                String url = rs.getString(5);
                String genre = rs.getString(6);
                events.add(new Event(name,date,time,url,genre,id));
            }
            throw new SQLException();
        } catch (SQLException e) {}
        return events;
    }

    // invitee's response to event

    public void insertRespondedEvent(EventResponse eventResponse) {
        try {
            if (getRespondedEvent(eventResponse.getReceiverUsername(), eventResponse.getEventID()) == null) {
                insertRespondedEventPs.setString(1,eventResponse.getReceiverUsername());
                insertRespondedEventPs.setInt(2,eventResponse.getExcitement());
                insertRespondedEventPs.setInt(3,eventResponse.getAvailability());
                insertRespondedEventPs.setString(4,eventResponse.getEventID());
                insertRespondedEventPs.executeUpdate();
            } else {
                updateRespondedEventPs.setInt(1,eventResponse.getExcitement());
                updateRespondedEventPs.setInt(2,eventResponse.getAvailability());
                updateRespondedEventPs.setString(3,eventResponse.getReceiverUsername());
                updateRespondedEventPs.setString(4,eventResponse.getEventID());
                updateRespondedEventPs.executeUpdate();
            }
            throw new SQLException();
        } catch (SQLException e) {}
    }

    public EventResponse getRespondedEvent(String receiverUsername, String eventID) {
        try {
            getRespondedEventPs.setString(1,receiverUsername);
            getRespondedEventPs.setString(2,eventID);
            ResultSet rs = getRespondedEventPs.executeQuery();
            if (rs.next()) {
                int excitement = rs.getInt(1);
                int availability = rs.getInt(2);
                return new EventResponse(eventID,availability,excitement,receiverUsername);
            }
            throw new SQLException();
        } catch (SQLException e) {}
        return null;
    }

    // finalized propsoal

    public void updateFinalizedProposal(String finalizedEventID, String proposalID) {
        try {
            updateFinalizedProposalPs.setString(1,finalizedEventID);
            updateFinalizedProposalPs.setString(2,proposalID);
            updateFinalizedProposalPs.executeUpdate();
            throw new SQLException();
        } catch (SQLException e) {}
    }

    public String getFinalizedProposal(String proposalID) {
        try {
            getFinalizedProposalPs.setString(1,proposalID);
            ResultSet rs = getFinalizedProposalPs.executeQuery();
            if (rs.next()) {
                String finalizedEventID = rs.getString("FinalizedEventID");
                return finalizedEventID;
            }
            throw new SQLException();
        } catch (SQLException e) {}
        return null;
    }

    public void updateFinalResponse(int finalResponse, String proposalID, String receiverUsername) {
        try {
            updateFinalResponsePs.setInt(1,finalResponse);
            updateFinalResponsePs.setString(2,proposalID);
            updateFinalResponsePs.setString(3,receiverUsername);
            updateFinalResponsePs.executeUpdate();
            throw new SQLException();
        } catch (SQLException e) {}
    }

    public Boolean getFinalResponse(String proposalID, String receiverUsername) {
        try {
            getFinalResponsePs.setString(1,proposalID);
            getFinalResponsePs.setString(2,receiverUsername);
            ResultSet rs = getFinalResponsePs.executeQuery();
            while (rs.next()) {
                int response = rs.getInt(1);
                if (rs.wasNull())
                {
                    return null;
                }
                else {
                    return response == 1;
                }
            }
            throw new SQLException();
        } catch (SQLException e) {}
        return null;
    }

    public ArrayList<Unavailability> getUnavailabilities(String username)
    {
        ArrayList<Unavailability> unavailabilities = new ArrayList<Unavailability>();
        try {
            getUnavailabilitiesPs.setString(1, username);
            ResultSet rs = getUnavailabilitiesPs.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("UnavailabilityID");
                String start = rs.getString("Start");
                String end = rs.getString("End");
                unavailabilities.add(new Unavailability(id, start, end, username));
            }
            throw new SQLException();
        } catch (SQLException e) {}
        return unavailabilities;
    }

    public boolean addUnavailability(String start, String end, String username)
    {
        try {
            addUnavailabilityPs.setString(1, start);
            addUnavailabilityPs.setString(2, end);
            addUnavailabilityPs.setString(3, username);
            addUnavailabilityPs.executeUpdate();
            throw new SQLException();
        } catch (SQLException e) {}
        return true;
    }

    public void removeUnavailability(int id)
    {
        try {
            removeUnavailabilityPs.setInt(1, id);
            removeUnavailabilityPs.executeUpdate();
            throw new SQLException();
        }
        catch (SQLException e) {}
    }

    public Boolean close() {
        try
        {
            con.close();
            databaseManager = null;

            throw new SQLException();
        }
        catch (Exception e)
        {
            return true;
        }
    }
}