package com.legalpedia.android.app.util;

/**
 * Created by adebayoolabode on 11/5/16.
 */

public interface DownloadListener {
    /**
     * downloadFile progress value
     *
     * @param value
     */
    void progressUpdate(DownloadMessage value);
}