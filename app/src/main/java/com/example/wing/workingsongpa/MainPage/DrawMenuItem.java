package com.example.wing.workingsongpa.MainPage;

import android.graphics.drawable.Drawable;

import com.example.wing.workingsongpa.ApplicationClass;
import com.example.wing.workingsongpa.Database.DataCenter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wing on 2017-02-11.
 */

public class DrawMenuItem implements ApplicationClass.Item {

    public final JSONObject itemData;

    private String course_title;


    public DrawMenuItem( JSONObject newData) {


        itemData = newData;
        try{
            this.course_title =  itemData.getString(DataCenter.SPOT_TITLE).toString();

        }catch (JSONException je)
        {
            course_title = "no title";

        }
    }

    public String getCourse_title() {
        return course_title;
    }

    @Override
    public boolean isSection() {
        return false;
    }
}
