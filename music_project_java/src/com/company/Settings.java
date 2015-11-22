package com.company;

/**
 * Created by Serge on 17.11.2015.
 */
public class Settings {
    // Connection info
    static final String SERVERNAME = "localhost";
    static final String USERNAME = "root";
    static final String PASSWORD = "";
    static final String DBNAME = "music_project";

    // Storage info
    static final String STORAGE_PATH = "g:\\http\\ask\\ms\\alpha\\storage\\";
    static final String RELATIVE_STORAGE_PATH = "storage/";

    // Recommendation system settings
    static final int RECOMMENDATION_FILES_COUNT = 3;

    // Useful functions
    static public String connectionString() {
        return String.format("jdbc:mysql://%s/%s?user=%s&password=%s",
                Settings.SERVERNAME, Settings.DBNAME, Settings.USERNAME, Settings.PASSWORD);
    }
}
