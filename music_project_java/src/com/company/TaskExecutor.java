package com.company;

import com.mathworks.toolbox.javabuilder.MWNumericArray;
import matlabModule.Indexer;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.*;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by Serge on 13.11.2015.
 */
public class TaskExecutor {
    /**
     * @param in matlab 1d array
     * @return 1d array
     */
    private static float[] SimplifyArr(MWNumericArray in) {
        float[][] tmp = (float[][])in.toFloatArray();
        return tmp[0];
    }

    private static float[] Index(String fileName) throws Exception {
        Indexer c = new Indexer();
        MWNumericArray res_mw = (MWNumericArray) c.analyse(1, fileName)[0];

        float[] res = SimplifyArr(res_mw);

        res_mw.dispose();
        c.dispose();

        return res;
    }

    private static AudioFile GetAudioFile(long file_id) throws SQLException {
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

    private static void UpdateAudioFileFeatures(long id, float[] features) throws SQLException {
        Connection connection = DriverManager.getConnection(Settings.connectionString());
        Statement statement = connection.createStatement();
        String sql = String.format(Locale.US, "UPDATE `audiofiles`" +
                "SET `p1` = '%f', `p2` = '%f', `p3` = '%f', `p4` = '%f', `p5` = '%f', `p6` = '%f'" +
                "WHERE `id` = %d",
                features[0], features[1], features[2], features[3], features[4], features[5], id);

        statement.execute(sql);

        statement.close();
        connection.close();
    }

//    todo FindRecommendations

    public static String ExecuteTask(String input) throws TaskExecutorException {
        JSONParser parser = new JSONParser();
        JSONObject input_j = null;
        try {
            input_j = (JSONObject) parser.parse(input);
        } catch (ParseException e) {
            throw new TaskExecutorException("Can not parse input json.");
        }

        String task = (String) input_j.get("task");
        if (task.equals("analyse")) {
            long fileId = (long) input_j.get("file_id");

            AudioFile audioFile = null;
            try {
                audioFile = GetAudioFile(fileId);
            } catch (SQLException e) {
                throw new TaskExecutorException("Error reading file record.");
            }

            float[] features = null;
            try {
                features = Index(Settings.STORAGE_PATH + audioFile.getStorage_name());
            } catch (Exception e) {
                throw new TaskExecutorException("Indexation error.");
            }

            try {
                UpdateAudioFileFeatures(fileId, features);
            } catch (SQLException e) {
                throw new TaskExecutorException("Error updating file record.");
            }

            JSONObject output = new JSONObject();
            output.put("status", "success");

            JSONArray features_j = new JSONArray();
            for (int i = 0; i < features.length; i++) {
                features_j.add(features[i]);
            }

            output.put("features", features_j);

            return output.toJSONString();
        } else {
            throw new TaskExecutorException("Unknown task");
        }
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