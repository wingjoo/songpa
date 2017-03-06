package com.example.wing.workingsongpa.MapTab;

import com.example.wing.workingsongpa.ApplicationClass;
import com.example.wing.workingsongpa.Database.DataCenter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wing on 2017-02-11.
 */

public class DrawMenuItem implements ApplicationClass.Item {

    public final JSONObject itemData;

    private int color;
    private String course_title;
    private String sub_title;

    private int section;

    public DrawMenuItem(JSONObject newData,int section) {

        itemData = newData;
        this.section = section;

        try{
            this.course_title =  itemData.getString(DataCenter.TITLE_KEY).toString();
            this.sub_title = itemData.getString(DataCenter.SUB_TITLE_KEY).toString();
            DataCenter.CourseType type = DataCenter.getInstance().getCourseTypeWithID(itemData.getInt(DataCenter.ID));
            color = DataCenter.getInstance().getColorWithType(type);

        }catch (JSONException je)
        {
            course_title = "no title";

        }
    }

    public String getCourse_title() {
        return course_title;
    }

    public int getCourseColor()
    {
        return color;
    }

    public String getSub_title()
    {
        return sub_title;
    }

    public int getSection(){return section;}


    @Override
    public boolean isSection() {
        return false;
    }
}
