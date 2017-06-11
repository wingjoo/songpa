package com.example.wing.workingsongpa.CourseList;

/**
 * Created by wing on 2016-12-12.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.opengl.GLES20;


import org.json.JSONObject;

import static android.R.attr.bitmap;


public class CourseListItem {

    private JSONObject itemData;
    private Integer imageID;

    private Drawable iconDrawable ;
    private  Bitmap imgScr;

    public void setImageID(Integer id)
    {
        imageID = id;
    }

    public Integer getImageID()
    {
        return imageID;
    }

//    public void setImgScr(Bitmap scr)
//    {
//        imgScr = scr;
//    }

//    public Bitmap getImgScr()
//    {
//        return imgScr;
//    }


    public void setIcon(Drawable icon) {

        iconDrawable = icon ;
    }

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
//    public String getTitle() {
//        return this.titleStr ;
//    }

    public void setItemData(JSONObject newData)
    {
        itemData = newData;
    }

    public JSONObject getItemData()
    {
        return  itemData;
    }

}
