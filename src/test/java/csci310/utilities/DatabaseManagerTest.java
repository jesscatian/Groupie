package csci310.utilities;

import csci310.models.Event;
import csci310.models.EventResponse;
import csci310.models.Unavailability;
import csci310.models.User;
import io.cucumber.java.lv.Un;
import org.junit.Before;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.*;

public class DatabaseManagerTest {

    private User user;
    private String id;
    private String title;
    private String senderUsername;
    private String receiverUsername;

    @Before
    public void setUp() throws Exception {
        user = new User("user1","13579qwerty");
        id = "123456qwerty";
        title = "title";
        senderUsername = "senderJunitTester";
        receiverUsername = "receiverJunitTester";
        new databaseConfig();
    }

    @Test
    public void testObject() {
        assertNotNull(DatabaseManager.object());
    }

    // authentication

    @Test
    public void testCheckUserExists_doesExist() {
        DatabaseManager.object().insertUser(user);
        assertNotNull(DatabaseManager.object().checkUserExists(user.getUsername()));
        DatabaseManager.object().deleteUser(user);
    }

    @Test
    public void testCheckUserExists_doesNotExist() {
        assertNull(DatabaseManager.object().checkUserExists("false"));
    }

    @Test
    public void testInsertUser() {
        User user2 = new User("user2","123");
        DatabaseManager.object().insertUser(user2);
        User retrievedUser = DatabaseManager.object().verifyUser(user2);
        assertEquals(retrievedUser.getUsername(),user2.getUsername());
        DatabaseManager.object().deleteUser(user2);
    }

    @Test
    public void testVerifyUser_doesExist() {
        User user3 = new User("user3","123456");
        DatabaseManager.object().insertUser(user3);
        User retrievedUser = DatabaseManager.object().verifyUser(user3);
        assertEquals(retrievedUser.getUsername(),user3.getUsername());
        DatabaseManager.object().deleteUser(user3);
    }

    @Test
    public void testVerifyUser_doesNotExist() {
        User user4 = new User("user4","123456");
        assertNull(DatabaseManager.object().verifyUser(user4));
    }

    @Test
    public void testVerifyUser_incorrectPsw() {
        DatabaseManager.object().insertUser(user);
        User newUser = new User(user.getUsername(),"false");
        assertNull(DatabaseManager.object().verifyUser(newUser));
        DatabaseManager.object().deleteUser(user);
    }

    @Test
    public void testDeleteUser_doesExist() {
        DatabaseManager.object().insertUser(user);
        DatabaseManager.object().deleteUser(user);
        assertNull(DatabaseManager.object().checkUserExists(user.getUsername()));
    }

    @Test
    public void testDeleteUser_doesNotExist() {
        User aUser = new User("aUser","123456");
        DatabaseManager.object().deleteUser(aUser);
        assertNull(DatabaseManager.object().checkUserExists(aUser.getUsername()));
    }

    // search user

    @Test
    public void testSearchUsers_success() {
        User user1 = new User("user1","123");
        User user2 = new User("user2","123");
        User user3 = new User("3user","123");
        DatabaseManager.object().insertUser(user1);
        DatabaseManager.object().insertUser(user2);
        DatabaseManager.object().insertUser(user3);
        ArrayList<String> arr = DatabaseManager.object().searchUsers("user");
        assertNotNull(arr);
        assertNotEquals(0,arr.size());
        DatabaseManager.object().deleteUser(user1);
        DatabaseManager.object().deleteUser(user2);
        DatabaseManager.object().deleteUser(user3);
    }

    @Test
    public void testSearchUsers_fail() {
        ArrayList<String> arr = DatabaseManager.object().searchUsers("somerandomnonexistentuser");
        assertNull(arr);
    }

    // Send Proposal

    @Test
    public void testInsertSentProposal() {
        DatabaseManager.object().insertSentProposal("idproposal23",title,senderUsername);
        assertTrue(DatabaseManager.object().getProposalIDs(senderUsername,true).contains("idproposal23"));
    }

    @Test
    public void testInsertReceivedProposal() {
        DatabaseManager.object().insertReceivedProposal(receiverUsername,"id233");
        assertTrue(DatabaseManager.object().getProposalIDs(receiverUsername,false).contains("id233"));
    }

    @Test
    public void testInsertEvent() {
        Event event = new Event("event 1","2021-01-01","19:00:00","abc.com","music","eventiddew");
        DatabaseManager.object().insertEvent(event,"eventproposalid12344");
        assertTrue(DatabaseManager.object().getProposalEvents("eventproposalid12344").size()>=1);
    }

    @Test
    public void testGetProposalIDs_sent() {
        DatabaseManager.object().insertSentProposal(id,title,senderUsername);
        assertTrue(DatabaseManager.object().getProposalIDs(senderUsername,true).contains(id));
    }

    @Test
    public void testGetProposalIDs_received() {
        DatabaseManager.object().insertReceivedProposal(receiverUsername,id);
        assertTrue(DatabaseManager.object().getProposalIDs(receiverUsername,false).contains(id));
    }

    @Test
    public void testGetProposalTitle() {
        DatabaseManager.object().insertSentProposal("someidsrff",title,senderUsername);
        assertEquals(title,DatabaseManager.object().getProposalTitle("someidsrff"));
    }

    @Test
    public void testGetSenderUsernameInProposal() {
        DatabaseManager.object().insertSentProposal(id,title,senderUsername);
        assertEquals(senderUsername,DatabaseManager.object().getProposalSender(id));
    }

    @Test
    public void testGetReceiverUsernamesInProposal() {
        DatabaseManager.object().insertReceivedProposal(receiverUsername,id);
        assertTrue(DatabaseManager.object().getProposalReciever(id).contains(receiverUsername));
    }

    @Test
    public void testGetEventsInProposal() {
        Event event1 = new Event("event 1","2021-01-01","19:00:00","abc.com","music", UUID.randomUUID().toString());
        Event event2 = new Event("event 2","2021-01-01","19:00:00","abc.com","music",UUID.randomUUID().toString());
        DatabaseManager.object().insertEvent(event1,id);
        DatabaseManager.object().insertEvent(event2,id);
        assertTrue(DatabaseManager.object().getProposalEvents(id).size()>=2);
    }

    @Test
    public void testInsertRespondedEvent() {
        String eventID = "junitevent123456qwerty";
        Event event = new Event("event 1","2021-01-01","19:00:00","abc.com","music",eventID);
        EventResponse eventResponse = new EventResponse(eventID,1,5,"name");
        DatabaseManager.object().insertEvent(event,id);
        DatabaseManager.object().insertRespondedEvent(eventResponse);
        assertEquals(5,DatabaseManager.object().getRespondedEvent("name",eventID).getExcitement());
        eventResponse.setExcitement(3);
        DatabaseManager.object().insertRespondedEvent(eventResponse);
        assertEquals(3,DatabaseManager.object().getRespondedEvent("name",eventID).getExcitement());
    }

    @Test
    public void testGetRespondedEvent() {
        String eventID = "junitevent12";
        Event event = new Event("event 1","2021-01-01","19:00:00","abc.com","music",eventID);
        EventResponse eventResponse = new EventResponse(eventID,0,5,"name");
        DatabaseManager.object().insertEvent(event,id);
        DatabaseManager.object().insertRespondedEvent(eventResponse);
        assertEquals(0,DatabaseManager.object().getRespondedEvent("name",eventID).getAvailability());
    }

    @Test
    public void testGetUnavailabilities()
    {
        User user5 = new User("user5","123");
        DatabaseManager.object().insertUser(user5);
        assertEquals(0, DatabaseManager.object().getUnavailabilities("user5").size());
        DatabaseManager.object().addUnavailability("2021-11-24T08:36", "2021-11-25T04:54", "user5");
        assertEquals("2021-11-24T08:36", DatabaseManager.object().getUnavailabilities("user5").get(0).getStart());
        assertEquals("2021-11-25T04:54", DatabaseManager.object().getUnavailabilities("user5").get(0).getEnd());
        assertEquals("user5", DatabaseManager.object().getUnavailabilities("user5").get(0).getUsername());
        DatabaseManager.object().addUnavailability("2021-11-26T05:38", "2021-11-27T11:37", "user5");
        assertEquals(2, DatabaseManager.object().getUnavailabilities("user5").size());
        assertEquals(0, DatabaseManager.object().getUnavailabilities("testUser").size());
        DatabaseManager.object().removeUnavailability(DatabaseManager.object().getUnavailabilities("user5").get(0).getId());
        assertEquals(1, DatabaseManager.object().getUnavailabilities("user5").size());
        DatabaseManager.object().removeUnavailability(DatabaseManager.object().getUnavailabilities("user5").get(0).getId());
        assertEquals(0, DatabaseManager.object().getUnavailabilities("user5").size());
        DatabaseManager.object().deleteUser(user5);
    }

    @Test
    public void testAddUnavailability()
    {
        User user5 = new User("user5","123");
        DatabaseManager.object().insertUser(user5);
        assertEquals(0, DatabaseManager.object().getUnavailabilities("user5").size());
        boolean added = DatabaseManager.object().addUnavailability("2021-11-24T08:36", "2021-11-25T04:54", "user5");
        assertTrue(added);
        assertEquals("2021-11-24T08:36", DatabaseManager.object().getUnavailabilities("user5").get(0).getStart());
        assertEquals("2021-11-25T04:54", DatabaseManager.object().getUnavailabilities("user5").get(0).getEnd());
        assertEquals("user5", DatabaseManager.object().getUnavailabilities("user5").get(0).getUsername());
        DatabaseManager.object().addUnavailability("2021-11-26T05:38", "2021-11-27T11:37", "user5");
        assertEquals(2, DatabaseManager.object().getUnavailabilities("user5").size());
        DatabaseManager.object().removeUnavailability(DatabaseManager.object().getUnavailabilities("user5").get(0).getId());
        DatabaseManager.object().removeUnavailability(DatabaseManager.object().getUnavailabilities("user5").get(0).getId());
        DatabaseManager.object().deleteUser(user5);
    }

    @Test
    public void testRemoveUnavailabilities()
    {
        User user5 = new User("user5","123");
        DatabaseManager.object().insertUser(user5);
        DatabaseManager.object().addUnavailability("2021-11-24T08:36", "2021-11-25T04:54", "user5");
        DatabaseManager.object().addUnavailability("2021-11-26T05:38", "2021-11-27T11:37", "user5");
        assertEquals(2, DatabaseManager.object().getUnavailabilities("user5").size());
        DatabaseManager.object().removeUnavailability(DatabaseManager.object().getUnavailabilities("user5").get(0).getId());
        assertEquals(1, DatabaseManager.object().getUnavailabilities("user5").size());
        DatabaseManager.object().removeUnavailability(DatabaseManager.object().getUnavailabilities("user5").get(0).getId());
        assertEquals(0, DatabaseManager.object().getUnavailabilities("user5").size());
        DatabaseManager.object().deleteUser(user5);
    }

    @Test
    public void testUpdateFinalizedProposal() {
        DatabaseManager.object().insertSentProposal("idproposal23",title,senderUsername);
        DatabaseManager.object().updateFinalizedProposal("123","idproposal23");
        assertEquals("123",DatabaseManager.object().getFinalizedProposal("idproposal23"));
    }

    @Test
    public void testGetFinalizedProposal() {
        DatabaseManager.object().insertSentProposal("idproposal23",title,senderUsername);
        DatabaseManager.object().updateFinalizedProposal("123","idproposal23");
        assertEquals("123",DatabaseManager.object().getFinalizedProposal("idproposal23"));
        assertNull(DatabaseManager.object().getFinalizedProposal("randomIDsome123"));
    }

    @Test
    public void testUpdateFinalResponse() {
        DatabaseManager.object().insertReceivedProposal(receiverUsername,"id233");
        DatabaseManager.object().updateFinalResponse(1,"id233",receiverUsername);
        assertEquals(true,DatabaseManager.object().getFinalResponse("id233",receiverUsername));
    }

    @Test
    public void testGetFinalResponse() {
        DatabaseManager.object().insertReceivedProposal(receiverUsername,"id233");
        DatabaseManager.object().updateFinalResponse(1,"id233",receiverUsername);
        assertEquals(true,DatabaseManager.object().getFinalResponse("id233",receiverUsername));
        assertNull(DatabaseManager.object().getFinalResponse("randomIDsome123",receiverUsername));
        DatabaseManager.object().updateFinalResponse(0,"id233",receiverUsername);
        assertEquals(false,DatabaseManager.object().getFinalResponse("id233",receiverUsername));
    }

    @Test
    public void testClose() {
        assertTrue(DatabaseManager.object().close());
    }
}