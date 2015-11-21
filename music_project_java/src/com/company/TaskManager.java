package com.company;

import com.mathworks.toolbox.javabuilder.MWNumericArray;
import matlabModule.Indexer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.*;
import java.util.Arrays;
import java.util.Locale;

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
    private void FinishTask(long id, String status, String output) throws SQLException {
        Connection connection = DriverManager.getConnection(Settings.connectionString());
        Statement statement = connection.createStatement();

        String sql = String.format("UPDATE `tasks` SET `output` = '%s', `finished` = CURRENT_TIMESTAMP," +
                "`status` = '%s' WHERE `tasks`.`id` = %d",
                output, status, id);

        statement.execute(sql);

        statement.close();
        connection.close();
    }

    /**
     * Matlab index function wrapper
     * @param fileName file to index
     * @return feature vector
     * @throws Exception
     */
    private float[] Index(String fileName) throws Exception {
        Indexer c = new Indexer();
        MWNumericArray res_mw = (MWNumericArray) c.analyse(1, fileName)[0];

        float[][] res = (float[][])res_mw.toFloatArray();

        res_mw.dispose();
        c.dispose();

        return res[0];
    }

    /**
     * @param file_id id of record
     * @return file record as class
     * @throws SQLException
     */
    private AudioFile GetAudioFile(long file_id) throws SQLException {
        Connection connection = DriverManager.getConnection(Settings.connectionString());
        Statement statement = connection.createStatement();
        String sql = String.format("SELECT * FROM `audiofiles` WHERE `id` = %d", file_id);
        ResultSet resultSet = statement.executeQuery(sql);

        AudioFile audioFile = null;

        if (resultSet.next()) {
            long id = resultSet.getLong("id");
            String storage_name = resultSet.getString("storage_name");
            String real_name = resultSet.getString("real_name");
            String status = resultSet.getString("status");

            float p1 = resultSet.getFloat("p1");
            float p2 = resultSet.getFloat("p2");
            float p3 = resultSet.getFloat("p3");
            float p4 = resultSet.getFloat("p4");
            float p5 = resultSet.getFloat("p5");
            float p6 = resultSet.getFloat("p6");

            audioFile = new AudioFile(id, storage_name, real_name, status,
                    new float[] {p1, p2, p3, p4, p5, p6});
        }

        resultSet.close();
        statement.close();
        connection.close();

        return audioFile;
    }

    private void UpdateAudioFileStatus(long id, String status) throws SQLException {
        Connection connection = DriverManager.getConnection(Settings.connectionString());
        Statement statement = connection.createStatement();

        String sql = String.format("UPDATE `music_project`.`audiofiles` SET `status` = '%s' WHERE `audiofiles`.`id` = %d",
                status, id);

        statement.execute(sql);

        statement.close();
        connection.close();
    }

    private void FinishAudioFile(long id, String status, float[] features) throws SQLException {
        Connection connection = DriverManager.getConnection(Settings.connectionString());
        Statement statement = connection.createStatement();
        String sql = String.format(Locale.US, "UPDATE `audiofiles`" +
                "SET `status` = '%s', `finished` = CURRENT_TIMESTAMP," +
                "`p1` = '%f', `p2` = '%f', `p3` = '%f', `p4` = '%f', `p5` = '%f', `p6` = '%f'" +
                "WHERE `id` = %d",
                status, features[0], features[1], features[2], features[3], features[4], features[5], id);

        statement.execute(sql);

        statement.close();
        connection.close();
    }

    private void Index(long id) throws SQLException, TaskManagerException {
        AudioFile audioFile = GetAudioFile(id);
        UpdateAudioFileStatus(id, "in progress");

        float[] features;
        try {
            System.out.printf("Indexation started. File id: %d.\n", id);
            features = Index(Settings.STORAGE_PATH + audioFile.getStorage_name());
            FinishAudioFile(id, "success", features);
            System.out.println("Indexation success.");
        } catch (Exception e) {
            FinishAudioFile(id, "indexation error", new float[] { 0, 0, 0, 0, 0, 0 });
            System.out.println("Indexation error.");
            throw new TaskManagerException("Indexation error.");
        }
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

            Index(fileId);
            float[] features = GetAudioFile(fileId).getFeatures();

            JSONObject output = new JSONObject();
            JSONArray features_j = new JSONArray();
            for (int i = 0; i < features.length; i++) {
                features_j.add(features[i]);
            }

            output.put("features", features_j);

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

                FinishTask(task.getId(), "success", output);
                System.out.printf("Task committed. Success.\n");
            } catch (TaskManagerException e) {
                FinishTask(task.getId(), "error", "");
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

class Task {
    private long id;
    private String input;
    private String output;
    private Timestamp created;
    private Timestamp finished;
    private String status;

    public Task(long id, String input, String output, Timestamp created, Timestamp finished, String status) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.created = created;
        this.finished = finished;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    public Timestamp getCreated() {
        return created;
    }

    public Timestamp getFinished() {
        return finished;
    }

    public String getStatus() {
        return status;
    }
}

class AudioFile {
    private long id;
    private String storage_name;
    private String real_name;
    private String status;
    private float[] features;

    public AudioFile(long id, String storage_name, String real_name, String status, float[] features) {
        this.id = id;
        this.storage_name = storage_name;
        this.real_name = real_name;
        this.status = status;
        this.features = features;
    }

    public long getId() {
        return id;
    }

    public String getStorage_name() {
        return storage_name;
    }

    public String getReal_name() {
        return real_name;
    }

    public String getStatus() {
        return status;
    }

    public float[] getFeatures() {
        return Arrays.copyOf(features, features.length);
    }
}

class TaskManagerException extends Exception {
    public TaskManagerException() {}

    public TaskManagerException(String message) {
        super(message);
    }
}
