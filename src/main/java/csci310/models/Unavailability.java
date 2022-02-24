package csci310.models;

public class Unavailability {
    private int id;
    private String start;
    private String end;
    private String username;

    public Unavailability(int id, String start, String end, String username)
    {
        this.id=id;
        this.start=start;
        this.end=end;
        this.username=username;
    }

    public int getId()
    {
        return id;
    }

    public String getStart()
    {
        return start;
    }

    public String getEnd()
    {
        return end;
    }

    public String getUsername()
    {
        return username;
    }
}
