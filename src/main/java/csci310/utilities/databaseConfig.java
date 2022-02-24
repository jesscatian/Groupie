package csci310.utilities;

import java.time.format.DateTimeFormatter;

// constants
public class databaseConfig {

    public static String sqliteUrl = "jdbc:sqlite:groupie.db";

    public static String rootUrl = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=s2fLKhgGgWbFn03ukLklx4J491GKcQWp";

    public static DateTimeFormatter dateFormat = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
}
