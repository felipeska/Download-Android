package com.codejump.downloadtask.task;

import android.content.Context;

/**
 * Created by felipecalderonbarragan on 6/16/14.
 */
public interface ICallBack {

    Context getContext();
    void taskStart();
    void taskCompleted(Object result);
    void taskProgress(Long progress);
}
