package com.example.wing.workingsongpa.CourseList;

import android.graphics.drawable.Drawable;

import org.json.JSONObject;

/**
 * Created by knightjym on 2017. 1. 29..
 */

public class DetailCourseListItem {

    private Drawable iconDrawable;
    private JSONObject itemData;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }

    public Drawable getIcon()
    {
        return this.iconDrawable ;

    }




    public void setItemData(JSONObject newData)
    {
        itemData = newData;
    }

    public JSONObject getItemData()
    {
        return  itemData;
    }
}
