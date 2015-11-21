package com.company;

import java.util.Arrays;

/**
 * Created by Alexander on 21/11/15.
 */
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
