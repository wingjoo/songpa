package com.example.wing.workingsongpa;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.wing.workingsongpa.CourseList.DetailCourseListActivity;

/**
 * Created by knightjym on 2017. 3. 9..
 */

public class ProgressTask extends AsyncTask <Void, Void, Void>{

    ProgressDialog asyncDialog;

    public ProgressTask(Context context)
    {
        asyncDialog = new ProgressDialog(context);
    }


    @Override
    protected void onPreExecute() {
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        asyncDialog.setMessage("로딩중입니다..");

        // show dialog
        asyncDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        try {
            for (int i = 0; i < 5; i++) {
                //asyncDialog.setProgress(i * 30);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        asyncDialog.dismiss();
        super.onPostExecute(result);
    }
}
