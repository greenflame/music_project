package com.company;

import java.sql.Timestamp;

/**
 * Created by Alexander on 21/11/15.
 */
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
