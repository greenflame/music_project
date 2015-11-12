package com.company;

/**
 * Created by Alexander on 10/11/15.
 */
public class Task {
    private long id;
    private String input;
    private String output;

    public Task(long id, String input) {
        this.id = id;
        this.input = input;
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

    public void setOutput(String output) {
        this.output = output;
    }
}
