package com.company;

import java.sql.*;

/**
 * Created by Alexander on 10/11/15.
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

    public void Start()
    {
        System.out.println("Server started");

        try {
            while (true) {
                Task task = GetNextTask();

                if (task != null) { // Task exists
                    System.out.printf("Task captured. id: %d.\n", task.getId());
                    UpdateTaskStatus(task.getId(), "in progress");

                    String output = "";
                    boolean success = true;

                    try {
                        output = TaskExecutor.ExecuteTask(task.getInput());
                    } catch (TaskExecutorException ex) {
                        System.out.println("Task executor exception: " + ex.getMessage());
                        success = false;
                    }

                    if (success) {
                        FinishTask(task.getId(), "success", output);
                        System.out.printf("Task committed.\n");
                    } else {
                        FinishTask(task.getId(), "error", "");
                        System.out.printf("Task processed with errors.\n");
                    }

                }

                Thread.sleep(1000);
            }
        } catch (Exception ex) {
            System.out.println("Task manager exception: " + ex.getMessage());
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
