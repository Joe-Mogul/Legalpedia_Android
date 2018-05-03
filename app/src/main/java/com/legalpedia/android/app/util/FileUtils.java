package com.legalpedia.android.app.util;

/**
 * Created by adebayoolabode on 4/18/17.
 */

import android.content.Context;

import java.io.File;

public class FileUtils {

    public static File getDataDir(Context context) {

        String path = context.getFilesDir().getAbsolutePath() + "/SampleZip";

        File file = new File(path);

        if(!file.exists()) {

            file.mkdirs();
        }

        return file;
    }

    public static File getDataDir(Context context, String folder) {

        String path = context.getFilesDir().getAbsolutePath() + "/" + folder;

        File file = new File(path);

        if(!file.exists()) {

            file.mkdirs();
        }

        return file;
    }
}