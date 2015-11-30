package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.*;

/**
 * Created by Alexander on 10/11/15.
 * Main computing server class
 */
public class TaskManager {
    /**
     * @return oldest task, that has 'in queue' status.
     * If tasks with this status don't exist, return null.
     * @throws SQLException
     */
    private Task GetNextTask() throws SQLException {
        Connection connection = DriverManager.getConnection(Settings.connectionString());
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM `tasks` WHERE `status` = 'in queue' ORDER BY `created` LIMIT 1";
        ResultSet resultSet = statement.executeQuery(sql);

        Task task = null;

        if (resultSet.next()) {
            long id = resultSet.getLong("id");
            String input = resultSet.getString("input");
            String output = resultSet.getString("output");
            Timestamp created = resultSet.getTimestamp("created");
            Timestamp finished = resultSet.getTimestamp("finished");
            String status = resultSet.getString("status");

            task = new Task(id, input, output, created, finished, status);
        }

        resultSet.close();
        statement.close();
        connection.close();

        return task;
    }

    /**
     * @param id id of task to update
     * @param status new status
     * @throws SQLException
     */
    private void UpdateTaskStatus(long id, String status) throws SQLException {
        Connection connection = DriverManager.getConnection(Settings.connectionString());
        Statement statement = connection.createStatement();

        String sql = String.format("UPDATE tasks SET status = '%s' WHERE tasks.id = %d",
                status, id);

        statement.execute(sql);

        statement.close();
        connection.close();
    }

    /**
     * @param id id of task to update
     * @param status new status
     * @param output computing engine output
     * @throws SQLException
     */
    private void UpdateTaskStatusOutputFinished(long id, String status, String output) throws SQLException {
        Connection connection = DriverManager.getConnection(Settings.connectionString());
        Statement statement = connection.createStatement();

        statement.executeQuery("SET NAMES utf8");
        String sql = String.format("UPDATE `tasks` SET `output` = '%s', `finished` = CURRENT_TIMESTAMP," +
                "`status` = '%s' WHERE `tasks`.`id` = %d",
                output, status, id);

        statement.execute(sql);

        statement.close();
        connection.close();
    }

    private JSONObject AudioFileToJson(long file_id) throws SQLException {  // todo move to AFile class
        AudioFile file = AudioManager.GetAudioFile(file_id);
        JSONObject file_j = new JSONObject();

        file_j.put("name", file.getReal_name().replaceFirst("[.][^.]+$", ""));
        file_j.put("url", Settings.RELATIVE_STORAGE_PATH + file.getStorage_name());

        JSONArray features_j = new JSONArray();
        for (int i = 0; i < file.getFeatures().length; i++) {
            features_j.add(file.getFeatures()[i]);
        }

        file_j.put("features", features_j);

        return file_j;
    }

    public String ComputeOutput(String input_s) throws TaskManagerException, SQLException {
        JSONParser parser = new JSONParser();
        JSONObject input;
        try {
            input = (JSONObject) parser.parse(input_s);
        } catch (ParseException e) {
            throw new TaskManagerException("Can not parse input json.");
        }

        if (input.containsKey("task") &&
                input.get("task").equals("analyse") &&
                input.containsKey("file_id")) {
            long fileId = (long) input.get("file_id");

            try {
                AudioManager.IndexFileInStorage(fileId);
            } catch (AudioManagerException e) {
                throw new TaskManagerException("Indexation error.");
            }


            Long[] recommendations = AudioManager.GetRecommendations(fileId);

            JSONObject output = new JSONObject();
            output.put("original_track", AudioFileToJson(recommendations[0]));

            JSONArray files_j = new JSONArray();
            for (int i = 1; i < recommendations.length; i++) {
                files_j.add(AudioFileToJson(recommendations[i]));
            }
            output.put("similar_tracks", files_j);

            return output.toJSONString();
        } else {
            throw new TaskManagerException("Unknown task");
        }
    }

    public void ProcessNextTask() throws SQLException {

        // Checking for next task
        Task task = GetNextTask();

        // Task exists
        if (task != null) {
            System.out.printf("Task captured. id: %d.\n", task.getId());

            // Updating status
            UpdateTaskStatus(task.getId(), "in progress");

            try {
                String output = ComputeOutput(task.getInput());

                UpdateTaskStatusOutputFinished(task.getId(), "success", output);
                System.out.printf("Task committed. Success.\n");
            } catch (TaskManagerException e) {
                UpdateTaskStatusOutputFinished(task.getId(), "error", "");
                System.out.printf("Task committed. Error.\n");
            }

        }   // Task exists
    }

    public void Start()
    {
        System.out.println("Server started");

        // Main loop
        while (true) {
            try {
                ProcessNextTask();
            } catch (SQLException e) {
                System.out.println("SQL Exception: " + e.getMessage());
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Task manager tread interrupted. " + e.getMessage());
            }
        }
    }

}

