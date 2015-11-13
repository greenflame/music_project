package com.company;

import java.sql.*;

// todo check dependencies?
/**
 * Created by Alexander on 10/11/15.
 */
public class ComputingServer {
    private static String CONN_STR = "jdbc:mysql://localhost/music_project?user=root&password=";

    /**
     * @return oldest task, that has 'in queue' status.
     * If tasks with this status don't exist, return null.
     * @throws SQLException
     */
    private Task GetNextTask() throws SQLException {
        Connection connection = DriverManager.getConnection(CONN_STR);
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM tasks WHERE tasks.created =" +
                "(SELECT min(tasks.created) FROM tasks WHERE tasks.status = 'in queue')";
        ResultSet resultSet = statement.executeQuery(sql);

        Task task = null;

        if (resultSet.next()) {
            long id = resultSet.getLong("id");
            String input = resultSet.getString("input");

            task = new Task(id, input);
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
        Connection connection = DriverManager.getConnection(CONN_STR);
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
        Connection connection = DriverManager.getConnection(CONN_STR);
        Statement statement = connection.createStatement();

        String sql = String.format("UPDATE `tasks` SET `output` = '%s', `finished` = CURRENT_TIMESTAMP," +
                "`status` = '%s' WHERE `tasks`.`id` = %d",
                output, status, id);

        statement.execute(sql);

        statement.close();
        connection.close();
    }

    public void Start()
    {
        try {
            while (true) {
                Task task = GetNextTask();

                if (task != null) {
                    System.out.printf("Task captured. id: %d.\n", task.getId(), task.getInput());
                    UpdateTaskStatus(task.getId(), "in progress");

                    String output = TaskExecutor.ExecuteTask(task.getInput());

                    FinishTask(task.getId(), "success", output);
                    System.out.printf("Task committed.\n");
                }

                Thread.sleep(1000);
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
