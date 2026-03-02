package com.adson.staymanager.exception;

import java.time.Instant;
import java.util.List;

public class ValidationError {

    private Instant timestamp;
    private Integer status;
    private String error;
    private String path;
    private List<FieldMessage> fields;

    public ValidationError() {}

    public ValidationError(Instant timestamp, Integer status, String error, String path, List<FieldMessage> fields) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
        this.fields = fields;
    }

    public Instant getTimestamp() { return timestamp; }
    public Integer getStatus() { return status; }
    public String getError() { return error; }
    public String getPath() { return path; }
    public List<FieldMessage> getFields() { return fields; }
}