package com.codejump.downloadtask.task;

import android.os.AsyncTask;
import android.util.Log;
import com.github.kevinsawicki.http.HttpRequest;
import java.io.File;


/**
 * Created by felipecalderonbarragan on 6/16/14.
 */
public class DownloadHttpRequestAsyncTask extends AsyncTask<String, Integer, File> {

    private final static String LOG_HTTP_ASYNC_TASK = "downloadByHttpRequest";
    private ICallBack callBack;

    public DownloadHttpRequestAsyncTask(ICallBack mCallBack) {
        callBack = mCallBack;
    }

    protected File doInBackground(String... urls) {
        try {
            HttpRequest request = HttpRequest.get(urls[0]);
            int fileLength  = request.getConnection().getContentLength();
            File file = null;
            if (request.ok()) {
                file = new File(callBack.getContext().getFilesDir(), new File(urls[0]).getName());
                request.receive(file);
                publishProgress((int)(file.length() * 100 / fileLength));
            }
            return file;
        } catch (HttpRequest.HttpRequestException exception) {
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callBack.taskStart();
    }

    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate();
        Log.d(LOG_HTTP_ASYNC_TASK, "Downloaded bytes: " + progress[0].intValue());
        callBack.taskProgress(progress[0].intValue());
    }

    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        callBack.taskCompleted(file);
    }
}