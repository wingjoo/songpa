package com.example.wing.workingsongpa.CourseList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wing.workingsongpa.Database.DataCenter;
import com.example.wing.workingsongpa.R;

import org.json.JSONObject;

/**
 * Created by knightjym on 2017. 3. 6..
 */

public class CourseListHeader extends FrameLayout{

    private ImageView backgroundImage;


    public CourseListHeader(Context context) {
        super(context);
        init();
    }


    private void init() {
        inflate(getContext(), R.layout.course_detail_list_header, this);
        this.backgroundImage = (ImageView) findViewById(R.id.detail_titleView);
    }

    public void setImage(Bitmap bgImg)
    {
        backgroundImage.setImageBitmap(bgImg);
    }

}