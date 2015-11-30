package com.company;

import com.mathworks.toolbox.javabuilder.MWNumericArray;
import matlabModule.Indexer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Alexander on 21/11/15.
 */
public final class AudioManager {

    /**
     * Pseudo static class.
     */
    private AudioManager() {}

    /**
     * Matlab index function wrapper
     * @param fileName file to index
     * @return feature vector
     * @throws Exception
     */
    private static float[] IndexRawFile(String fileName) throws Exception {
        Indexer c = new Indexer();
        MWNumericArray res_mw = (MWNumericArray) c.analyse(1, fileName)[0];

        float[][] res = (float[][])res_mw.toFloatArray();

        res_mw.dispose();
        c.dispose();

        return res[0];
    }

    /**
     * Run indexation algorithm on file.
     * @param id of file record
     * @throws SQLException
     * @throws AudioManagerException
     */
    public static void IndexFileInStorage(long id) throws SQLException, AudioManagerException {
        AudioFile audioFile = GetAudioFile(id);
        UpdateAudioFileStatus(id, "in progress");

        float[] features;
        try {
            System.out.printf("Indexation started. File id: %d.\n", id);
            features = IndexRawFile(Settings.STORAGE_PATH + audioFile.getStorage_name());
            UpdateAudioFileStatusFeaturesFinished(id, "success", features);
            System.out.println("Indexation success.");
        } catch (Exception e) {
            UpdateAudioFileStatusFeaturesFinished(id, "indexation error", new float[] { 0, 0, 0, 0, 0, 0 });
            System.out.println("Indexation error.");
            throw new AudioManagerException("Indexation error.");
        }
    }

    /**
     * @param file_id id of record
     * @return file record as class
     * @throws SQLException
     */
    public static AudioFile GetAudioFile(long file_id) throws SQLException {
        Connection connection = DriverManager.getConnection(Settings.connectionString());
        Statement statement = connection.createStatement();
        statement.executeQuery("SET NAMES utf8");
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

    /**
     * @param id of task
     * @param status new status
     * @throws SQLException
     */
    private static void UpdateAudioFileStatus(long id, String status) throws SQLException {
        Connection connection = DriverManager.getConnection(Settings.connectionString());
        Statement statement = connection.createStatement();

        String sql = String.format("UPDATE `music_project`.`audiofiles` SET `status` = '%s' WHERE `audiofiles`.`id` = %d",
                status, id);

        statement.execute(sql);

        statement.close();
        connection.close();
    }

    /**
     * Set finished timestamp, update status and features.
     * @param id
     * @param status
     * @param features
     * @throws SQLException
     */
    private static void UpdateAudioFileStatusFeaturesFinished(long id, String status, float[] features) throws SQLException {
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

    /**
     * @param id of track recommend for
     * @return array of recommended ids
     * @throws SQLException
     */
    public static Long[] GetRecommendations(long id) throws SQLException {
        AudioFile toRecommend = GetAudioFile(id);

        Connection connection = DriverManager.getConnection(Settings.connectionString());
        Statement statement = connection.createStatement();
        String sql = String.format(Locale.US,
                "SELECT id,\n" +
                        " (ABS(p1 / %f - 1) * %f" +
                        "+ ABS(p2 / %f - 1) * %f" +
                        "+ ABS(p3 / %f - 1) * %f" +
                        "+ ABS(p4 / %f - 1) * %f" +
                        "+ ABS(p5 / %f - 1) * %f" +
                        "+ ABS(p6 / %f - 1) * %f) as distance,\n" +
                "(p1 + p2 + p3 + p4 + p5 + p6) as hash\n" +
                "FROM audiofiles WHERE status = 'success' GROUP BY hash ORDER by distance LIMIT %d",
                toRecommend.getFeatures()[0],
                Settings.FEATURES_WEIGHT_VECTOR[0],
                toRecommend.getFeatures()[1],
                Settings.FEATURES_WEIGHT_VECTOR[1],
                toRecommend.getFeatures()[2],
                Settings.FEATURES_WEIGHT_VECTOR[2],
                toRecommend.getFeatures()[3],
                Settings.FEATURES_WEIGHT_VECTOR[3],
                toRecommend.getFeatures()[4],
                Settings.FEATURES_WEIGHT_VECTOR[4],
                toRecommend.getFeatures()[5],
                Settings.FEATURES_WEIGHT_VECTOR[5],
                Settings.RECOMMENDATION_FILES_COUNT + 1 // First record - original file
                );

        ResultSet resultSet = statement.executeQuery(sql);

        List<Long> recommendations = new LinkedList<>();

        while (resultSet.next()) {
            long tmp = resultSet.getLong("id");
            recommendations.add(tmp);
        }

        resultSet.close();
        statement.close();
        connection.close();

        return recommendations.toArray(new Long[recommendations.size()]);
    }

    private static String GetRandomStorageName() {
        String allowedChars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String result = "";
        Random r = new Random();

        for (int i = 0; i < Settings.STORAGE_NAME_LENGTH; i++) {
            result += allowedChars.charAt(r.nextInt(allowedChars.length()));
        }

        return result + ".mp3";
    }

    public static void AddFileToStorage(File file) throws AudioManagerException, SQLException {
        // Copy file to storage
        String storageName = GetRandomStorageName();

        try {
            Files.copy(file.toPath(), new File(Settings.STORAGE_PATH + storageName).toPath());
        } catch (IOException e) {
            throw new AudioManagerException("Error copy file.");
        }

        // Add db record
        Connection connection = DriverManager.getConnection(Settings.connectionString());
        Statement statement = connection.createStatement();
        statement.executeQuery("SET NAMES utf8");
        String sql = String.format("INSERT INTO `audiofiles` (`storage_name`, `real_name`)" +
                "VALUES ('%s', '%s')", storageName, file.getName());

        statement.execute(sql);

        statement.close();
        connection.close();

    }

    public static void IndexAllInQueueFiles() throws SQLException {
        Connection connection = DriverManager.getConnection(Settings.connectionString());
        Statement statement = connection.createStatement();
        String sql = String.format("SELECT * FROM audiofiles WHERE status = 'in queue'");
        ResultSet resultSet = statement.executeQuery(sql);

        List<Long> ids = new LinkedList<>();

        while (resultSet.next()) {
            long id = resultSet.getLong("id");
            ids.add(id);
        }

        resultSet.close();
        statement.close();
        connection.close();

        for (int i = 0; i < ids.size(); i++) {
            System.out.printf("Indexing file: %d / %d.\n", i + 1, ids.size());
            try {
                IndexFileInStorage(ids.get(i));
            } catch (AudioManagerException e) {

            }
        }
    }

    public static void InfuseFolderToStorage(String path) {
        List<File> files = null;
        try {
            files = Files.walk(new File(path).toPath())
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Error building file list.");
        }

        for (int i = 0; i < files.size(); i++) {
            System.out.printf("Copying file: %d / %d.\n", i + 1, files.size());
            try {
                AddFileToStorage(files.get(i));
            } catch (SQLException e) {
                System.out.println("Error adding file. Db error.");
            } catch (AudioManagerException e) {
                System.out.println("Error adding file. Copy error.");
            }
        }

        try {
            IndexAllInQueueFiles();
        } catch (SQLException e) {
            System.out.println("Error indexing file. Can not access database.");
        }
    }

    public static void ReindexStorage() {
        try {
            Connection connection = DriverManager.getConnection(Settings.connectionString());
            Statement statement = connection.createStatement();
            String sql = "UPDATE `music_project`.`audiofiles` SET `status` = 'in queue'";

            statement.execute(sql);

            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error reset records status.");
        }

        try {
            IndexAllInQueueFiles();
        } catch (SQLException e) {
            System.out.println("Error indexing file. Can not access database.");
        }
    }
}
