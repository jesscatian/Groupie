package csci310.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResponseTest {

    @Test
    public void testToString() {
        User user = new User("user1","12345678");
        new User("a","123","qwer");
        user.setUsername("user2");
        user.getUuid();
        Response<User> response = new Response<>(true);
        response.setMessage("new message");
        response.setData(user);
        response.setStatus(false);
        User retrievedUser = response.getData();
        assertEquals("user2",retrievedUser.getUsername());
        assertEquals("Response{" +
                "status=" + false +
                ", message='" + "new message" + '\'' +
                ", data=" + retrievedUser +
                '}',response.toString());
    }
}