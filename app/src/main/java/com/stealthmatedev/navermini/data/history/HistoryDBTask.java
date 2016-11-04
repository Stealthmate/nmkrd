package com.stealthmatedev.navermini.data.history;

import android.os.AsyncTask;

/**
 * Created by Stealthmate on 16/11/04 0004.
 */

public class HistoryDBTask extends AsyncTask<Object, Void, Object> {

    public interface Callback {
        Object execute(Object...params);
    }

    public interface Task extends Callback {
    }

    private Task task;
    private HistoryDBTask next;

    public HistoryDBTask(Task task) {
        this.task = task;
    }

    @Override
    protected Object doInBackground(Object... params) {
        return task.execute(params);
    }

    @Override
    protected void onPostExecute(Object result) {
        next.execute(result);
    }

    public HistoryDBTask then(Task callback){
        this.next = new HistoryDBTask(callback);
        return this;
    }



}
