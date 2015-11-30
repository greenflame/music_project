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

    // Storage infose
    static final String STORAGE_PATH = "g:\\http\\ask\\musicsense\\storage\\";
    static final String RELATIVE_STORAGE_PATH = "storage/";
    static final int STORAGE_NAME_LENGTH = 10;

    // Recommendation system settings
    static final int RECOMMENDATION_FILES_COUNT = 5;
    static final float[] FEATURES_WEIGHT_VECTOR = { 0.01f, 2, 2, 1, 1, 1 };

    // Useful functions
    static public String connectionString() {
        return String.format("jdbc:mysql://%s/%s?useUnicode=true&characterEncoding=utf-8&user=%s&password=%s",
                Settings.SERVERNAME, Settings.DBNAME, Settings.USERNAME, Settings.PASSWORD);
    }
}
