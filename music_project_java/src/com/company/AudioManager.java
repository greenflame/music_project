package com.company;

import com.mathworks.toolbox.javabuilder.MWNumericArray;
import matlabModule.Indexer;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

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
    private static float[] IndexByName(String fileName) throws Exception {
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
    public static AudioFile GetAudioFile(long file_id) throws SQLException {
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
    private static void FinishAudioFile(long id, String status, float[] features) throws SQLException {
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
     * Run indexation algorithm on file.
     * @param id of file record
     * @throws SQLException
     * @throws AudioManagerException
     */
    public static void IndexById(long id) throws SQLException, AudioManagerException {
        AudioFile audioFile = GetAudioFile(id);
        UpdateAudioFileStatus(id, "in progress");

        float[] features;
        try {
            System.out.printf("Indexation started. File id: %d.\n", id);
            features = IndexByName(Settings.STORAGE_PATH + audioFile.getStorage_name());
            FinishAudioFile(id, "success", features);
            System.out.println("Indexation success.");
        } catch (Exception e) {
            FinishAudioFile(id, "indexation error", new float[] { 0, 0, 0, 0, 0, 0 });
            System.out.println("Indexation error.");
            throw new AudioManagerException("Indexation error.");
        }
    }

    public static Long[] RecommendAudioFiles(long id) throws SQLException {
        AudioFile toRecommend = GetAudioFile(id);

        Connection connection = DriverManager.getConnection(Settings.connectionString());
        Statement statement = connection.createStatement();
        String sql = String.format(
                "SELECT id,\n" +
                "(ABS(p1 - %f) + ABS(p2 - %f) + ABS(p3 - %f) + ABS(p4 - %f) + ABS(p5 - %f) + ABS(p6 - %f)) as distance,\n" +
                "(p1 + p2 + p3 + p4 + p5 + p6) as hash\n" +
                "FROM audiofiles WHERE status = 'success' GROUP BY hash ORDER by distance LIMIT %d",
                toRecommend.getFeatures()[0],
                toRecommend.getFeatures()[1],
                toRecommend.getFeatures()[2],
                toRecommend.getFeatures()[3],
                toRecommend.getFeatures()[4],
                toRecommend.getFeatures()[5],
                Settings.RECOMMENDATION_FILES_COUNT
                );   // todo not select file to recommend

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
}
