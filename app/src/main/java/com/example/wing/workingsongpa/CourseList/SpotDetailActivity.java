package com.example.wing.workingsongpa.CourseList;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.wing.workingsongpa.Database.DataCenter;
import com.example.wing.workingsongpa.R;
import com.viewpagerindicator.LinePageIndicator;
import com.viewpagerindicator.PageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


import static com.example.wing.workingsongpa.CourseList.DetailCourseListActivity.SPOT_DATA;

/**
 * Created by knightjym on 2017. 2. 18..
 */

public class SpotDetailActivity extends FragmentActivity {

    ViewPager mPager;
    LinePageIndicator mIndicator;

    JSONObject spotData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spot_activity);


        ImageButton back = (ImageButton)findViewById(R.id.detail_back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Do something with the value of the button
                finish();
            }
        });


//        {
//            “id”:int
//            “title”:string
//            “sub_title:string
//            “next_spot_distan”:string
//            “msg”:string
//            “info”:string
//            “latitude”:float
//            “longitude”:float
//            “group_type:int
//            "default_img":string
//            "img_list":[string]
//        }

        Intent intent = getIntent();
        try
        {
            SpotDetailAdapter mAdapter = new SpotDetailAdapter(getSupportFragmentManager());
            //IMG Scrollview
            mPager = (ViewPager)findViewById(R.id.img_pager);


            spotData = new JSONObject(intent.getStringExtra(SPOT_DATA));
            //viewPager를 통한 이미지 스크롤뷰
            JSONArray imgList = spotData.getJSONArray(DataCenter.SPOT_IMG_LIST);
            for (int i = 0; i < imgList.length(); i++) {
                String imgUrl = imgList.getString(i).toString();
                Resources resources =  getResources();
                int resID  = getResources().getIdentifier(imgUrl  , "drawable", "com.example.wing.workingsongpa");
                Bitmap scr = DataCenter.getInstance().resizeImge(resources,resID);
                //fagment만들기
                mAdapter.addItem(scr);
            }

            mPager.setAdapter(mAdapter);
            //Bind the title indicator to the adapter
            mIndicator = (LinePageIndicator)findViewById(R.id.indicator);
            mIndicator.setViewPager(mPager);


            //subtitle
            String s_title_str =  spotData.getString(DataCenter.SPOT_SUB_TITLE);
            TextView subTitle = (TextView)findViewById(R.id.sub_title);
            if (s_title_str.length() > 0 )
            {
                subTitle.setText(s_title_str);
            }else
            {
                subTitle.setVisibility(View.GONE);
            }

            //title
            String title_str =  spotData.getString(DataCenter.SPOT_TITLE);
            TextView title = (TextView)findViewById(R.id.title);
            if (title_str .length() > 0 )
            {
                title.setText(title_str);
            }else
            {
                title.setVisibility(View.GONE);
            }

            //info
            String info_str =  spotData.getString(DataCenter.SPOT_INFO);
            TextView intoText = (TextView)findViewById(R.id.info_text);
            if (info_str .length() > 0 )
            {
                intoText .setText(info_str);
            }else
            {
                intoText.setVisibility(View.GONE);
            }

            //msg
            String msg_str =  spotData.getString(DataCenter.SPOT_MSG);
            TextView msgText = (TextView)findViewById(R.id.msg_text);
            if (msg_str .length() > 0 )
            {
                msgText.setText(msg_str);
            }else
            {
                msgText.setVisibility(View.GONE);
            }

        }catch (JSONException je)
        {
            Log.e("jsonErr", "json에러입니당~", je);
            spotData = null;
        }
    }

}