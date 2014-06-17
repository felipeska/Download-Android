package com.codejump.downloadtask.task;

import com.telly.groundy.GroundyTask;
import com.telly.groundy.TaskResult;
import com.telly.groundy.util.DownloadUtils;
import java.io.File;


/**
 * Created by felipecalderonbarragan on 6/13/14.
 */
public class DownloadGroundyTask extends GroundyTask {

    public static final String PARAM_URL = "com.codejump.downloadtest.URL";
    public static final String PARAM_URL_FILE = "com.codejump.downloadtest.URL_FILE";

    @Override
    protected TaskResult doInBackground() {
        try {
            String url = getStringArg(PARAM_URL);
            File dest = new File(getContext().getFilesDir(), new File(url).getName());
            DownloadUtils.downloadFile(getContext(), url, dest,
                    DownloadUtils.getDownloadListenerForTask(this), new DownloadUtils.DownloadCancelListener(){
                        @Override
                        public boolean shouldCancelDownload() {
                            return isQuitting();
                        }
                    });

            if (isQuitting()) {
                return cancelled();
            }
            return succeeded().add(PARAM_URL_FILE,dest.getAbsolutePath());
        } catch (Exception e) {
            return failed();
        }
    }

}
