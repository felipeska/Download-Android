

package com.codejump.downloadtask;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.codejump.downloadtask.task.DownloadGroundyTask;
import com.codejump.downloadtask.task.DownloadHttpRequestAsyncTask;
import com.codejump.downloadtask.task.ICallBack;
import com.codejump.downloadtest.R;
import com.telly.groundy.Groundy;
import com.telly.groundy.annotations.OnCancel;
import com.telly.groundy.annotations.OnFailure;
import com.telly.groundy.annotations.OnProgress;
import com.telly.groundy.annotations.OnSuccess;
import com.telly.groundy.annotations.Param;
import java.io.File;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DownloadActivity extends ActionBarActivity implements ICallBack{

    private final static String URL_DOWNLOAD = "http://mdm.arandasoft.com/dummyimages/Mizuu_2.8.0.3.apk";

    private ProgressDialog mProgressDialog;

    private static final int SDK_HONEYCOMB_API_LEVEL = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.inject(this);
    }


    @OnClick(R.id.groundyDownload)
    void groundyDownload() {
        Groundy.create(DownloadGroundyTask.class)
                .arg(DownloadGroundyTask.PARAM_URL, URL_DOWNLOAD)
                .callback(mCallback)
                .queueUsing(DownloadActivity.this);
        showDialog();
    }

    @OnClick(R.id.httpRequestDownload)
    void httpRequestDownload(){
        new DownloadHttpRequestAsyncTask(this).execute(URL_DOWNLOAD);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.download, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog(){
        mProgressDialog = new ProgressDialog(DownloadActivity.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    private final Object mCallback = new Object() {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @OnProgress(DownloadGroundyTask.class)
        public void onNiceProgress(@Param(Groundy.PROGRESS) int progress) {
            if (progress == Groundy.NO_SIZE_AVAILABLE) {
                mProgressDialog.setIndeterminate(true);
                if (Build.VERSION.SDK_INT >= SDK_HONEYCOMB_API_LEVEL) {
                    mProgressDialog.setProgressNumberFormat(null);
                    mProgressDialog.setProgressPercentFormat(null);
                }
                return;
            }
            mProgressDialog.setProgress(progress);
        }

        @OnSuccess(DownloadGroundyTask.class)
        public void onBeautifulSuccess(@Param(DownloadGroundyTask.PARAM_URL_FILE) String dest) {
            Toast.makeText(DownloadActivity.this, R.string.file_downloaded + dest, Toast.LENGTH_LONG).show();
            mProgressDialog.dismiss();
        }

        @OnFailure(DownloadGroundyTask.class)
        public void onTragedy(@Param(Groundy.CRASH_MESSAGE) String error) {
            Toast.makeText(DownloadActivity.this, error, Toast.LENGTH_LONG).show();
            mProgressDialog.dismiss();
        }

        @OnCancel(DownloadGroundyTask.class)
        public void onFuckedCancel(){

        }
    };

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void taskStart() {
        showDialog();
    }

    @Override
    public void taskCompleted(Object result) {
        File downloadedFile = (File) result;

        if(downloadedFile != null){
            Toast.makeText(DownloadActivity.this, R.string.file_downloaded + downloadedFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
            mProgressDialog.dismiss();
        }else{
            Toast.makeText(DownloadActivity.this, R.string.file_download_error, Toast.LENGTH_LONG).show();
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void taskProgress(int progress) {
        mProgressDialog.setProgress(progress);
    }
}
