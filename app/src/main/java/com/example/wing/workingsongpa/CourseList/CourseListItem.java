package com.example.wing.workingsongpa.CourseList;

/**
 * Created by wing on 2016-12-12.
 */
import android.graphics.drawable.Drawable;


public class CourseListItem {
    private Drawable iconDrawable ;
    private String titleStr ;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
//    public void setTitle(String title) {
//        titleStr = title ;
//    }


    public Drawable getIcon() {
        return this.iconDrawable ;
    }
//    public String getTitle() {
//        return this.titleStr ;
//    }


}
