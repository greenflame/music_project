package com.company;

import java.sql.SQLException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
//        TaskManager computingServer = new TaskManager();
//        computingServer.Start();
        try {
            System.out.println(Arrays.toString(AudioManager.RecommendAudioFiles(2)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
