package com.example.wing.workingsongpa.CourseList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wing.workingsongpa.Database.DataCenter;
import com.example.wing.workingsongpa.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by knightjym on 2017. 3. 6..
 */

public class DetailCourseListHeader extends FrameLayout{

    private ImageView backgroundImage;
    private TextView storyTV;
    TextView specialTV;

    TextView infoTV;

    public DetailCourseListHeader(Context context) {
        super(context);
        init();
    }


    private void init() {
        inflate(getContext(), R.layout.course_detail_list_header, this);
        this.backgroundImage = (ImageView) findViewById(R.id.detail_titleView);

        this.storyTV = (TextView) findViewById(R.id.story_text);

        this.specialTV = (TextView) findViewById(R.id.specialty_text);

        this.infoTV = (TextView) findViewById(R.id.course_info_text);

    }

    public void setData(Integer resID, JSONObject data)
    {
        //setImageBitmap(bgImg);
        backgroundImage.setImageResource(resID);
        try {

            String story = data.getString(DataCenter.COURSE_STORY).toString();
            if (story != null && story.length() > 0)
            {
                storyTV.setText(story);
            }else
            {
                TextView sTV = (TextView) findViewById(R.id.story_title);
                sTV.setVisibility(View.GONE);
            }

            String special = data.getString(DataCenter.COURSE_SPECIALTY).toString();
            if (special != null && special.length() > 0)
            {
                specialTV.setText(special);
            }else
            {
                TextView sTV = (TextView) findViewById(R.id.speictial_title);
                sTV.setVisibility(View.GONE);
            }
            String infouse = data.getString(DataCenter.COURSE_INFORUSE).toString();
            if (infouse != null && infouse.length() > 0)
            {
                infoTV.setText(infouse);
            }

        }catch (JSONException e)
        {

        }

    }

}