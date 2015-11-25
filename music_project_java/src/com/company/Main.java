package com.company;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("Command[server, infuse, reindex]:");

        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();

        switch (command) {
            case "server":
                TaskManager computingServer = new TaskManager();
                computingServer.Start();
                break;
            case "infuse":
                System.out.println("Folder path:");
                String path = scanner.nextLine();
                AudioManager.InfuseFolderToStorage(path);
                break;
            case "reindex":
                AudioManager.ReindexStorage();
                break;
        }
    }
}
