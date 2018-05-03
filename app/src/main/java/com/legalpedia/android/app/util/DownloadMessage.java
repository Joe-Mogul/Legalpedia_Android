package com.legalpedia.android.app.util;

/**
 * Created by adebayoolabode on 11/5/16.
 */

public class DownloadMessage {
    private Integer progress;
    private String message;

    public DownloadMessage(Integer progress, String message) {
        this.progress = progress;
        this.message = message;
    }


    public DownloadMessage(Integer progress) {
        this.progress = progress;
        this.message = "";
    }


    public DownloadMessage(String message) {
        this.progress = 0;
        this.message = message;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return "DownloadMessage{" +
                "progress=" + progress +
                ", message='" + message + '\'' +
                '}';
    }
}
