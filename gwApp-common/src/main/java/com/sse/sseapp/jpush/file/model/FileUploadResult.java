package com.sse.sseapp.jpush.file.model;

import lombok.Data;

@Data
public class FileUploadResult {
    private String file_id;
    private Error error;

    @Data
    public static class Error {
        private String message;
        private int code;

    }
}