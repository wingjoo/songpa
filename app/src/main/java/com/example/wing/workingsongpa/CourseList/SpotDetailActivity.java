package com.example.wing.workingsongpa.CourseList;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;


import com.example.wing.workingsongpa.Database.DataCenter;
import com.example.wing.workingsongpa.R;
import com.viewpagerindicator.LinePageIndicator;
import com.viewpagerindicator.PageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import static com.example.wing.workingsongpa.CourseList.DetailCourseListActivity.SPOT_DATA;

/**
 * Created by knightjym on 2017. 2. 18..
 */

public class SpotDetailActivity extends FragmentActivity {


    ViewPager mPager;
    PageIndicator mIndicator;

    JSONObject spotData;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        setContentView(R.layout.spot_activity);

        //Set the pager with an adapter
        mPager = (ViewPager)findViewById(R.id.img_pager);
        SpotDetailAdapter mAdapter = new SpotDetailAdapter(getSupportFragmentManager());

        //Bind the title indicator to the adapter
        mIndicator = (LinePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

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
            spotData = new JSONObject(intent.getStringExtra(SPOT_DATA));
            JSONArray imgList = spotData.getJSONArray(DataCenter.SPOT_IMG_LIST);
            for (int i = 0; i < imgList.length(); i++) {
                String imgUrl = imgList.getString(i).toString();
                int resID  = getResources().getIdentifier(imgUrl  , "drawable", "com.example.wing.workingsongpa");
                mAdapter.addItem(ContextCompat.getDrawable(this, resID));
                mPager.setAdapter(mAdapter);
            }


        }catch (JSONException je)
        {
            Log.e("jsonErr", "json에러입니당~", je);
            spotData = null;
        }


    }


}