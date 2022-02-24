package csci310.models;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {
    private String username;
    private String psw;
    private String uuid;

    public User(String username, String psw) {
        this.username = username;
        this.psw = psw;
        this.uuid = UUID.randomUUID().toString();
    }

    public User(String username, String psw, String uuid) {
        this.username = username;
        this.psw = psw;
        this.uuid = uuid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getPsw() {
        return psw;
    }
}
