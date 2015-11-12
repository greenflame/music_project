package com.company;

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.sql.*;

// CURRENT_TIMESTAMP
// todo check dependencies?
/**
 * Created by Alexander on 10/11/15.
 */
public class ComputionServer {
    private static String CONN_STR = "jdbc:mysql://localhost/music_project?user=root&password=";

    /**
     * @return Return oldest task, that has 'in queue' status.
     * If tasks with this status don't exist, return null.
     * @throws SQLException
     */
    private Task GetNextTask() throws SQLException {
        Connection connection = DriverManager.getConnection(CONN_STR);
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM tasks WHERE tasks.created =" +
                "(SELECT min(tasks.created) FROM tasks WHERE tasks.status != NULL)";
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
     * Update database, set output by task id.
     * @param task Processed task.
     * @throws SQLException
     */
    private void CommitTask(Task task) throws SQLException {
        Connection connection = DriverManager.getConnection(CONN_STR);
        Statement statement = connection.createStatement();

        String sql = String.format("UPDATE tasks SET output = '%s' WHERE tasks.id = %d",
                task.getOutput(), task.getId());

        statement.execute(sql);

        statement.close();
        connection.close();
    }

    private void ProcessTask(Task task)
    {
        task.setOutput(task.getInput().toUpperCase());
    }

    public void Start()
    {
        try {
            while (true) {
                Task task = GetNextTask();

                if (task != null) {
                    System.out.printf("Task captured. id: %d, input: '%s'.\n", task.getId(), task.getInput());

                    ProcessTask(task);
                    System.out.printf("Task processed. Output: '%s'.\n", task.getOutput());

                    CommitTask(task);
                    System.out.printf("Task committed.\n");
                }

                Thread.sleep(5000);
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
