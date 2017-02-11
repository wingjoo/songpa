package com.example.wing.workingsongpa.CourseList;

import android.graphics.drawable.Drawable;

import com.example.wing.workingsongpa.Database.DataCenter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by knightjym on 2017. 1. 29..
 */

public class DetailCourseListItem {

    private Drawable iconDrawable;
    private JSONObject itemData;

    private  String title;
    private  String subTitle;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }

    public Drawable getIcon()
    {
        return this.iconDrawable ;

    }

    public String getTitle()
    {
        return title;
    }

    public String getSubTitle()
    {
        return subTitle;
    }

    public void setItemData(JSONObject newData)
    {
        itemData = newData;
        setData();
    }

    public JSONObject getItemData()
    {
        return  itemData;
    }

    private void setData()
    {
        try{
            title =  itemData.getString(DataCenter.SPOT_TITLE).toString();
            subTitle = itemData.getString(DataCenter.SPOT_SUB_TITLE).toString();
        }catch (JSONException je)
        {
            title = "no title";
            subTitle = "no subtitle";
        }
    }

}
