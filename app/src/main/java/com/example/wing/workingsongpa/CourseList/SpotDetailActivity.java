package com.example.wing.workingsongpa.CourseList;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.wing.workingsongpa.Database.DataCenter;
import com.example.wing.workingsongpa.R;
import com.tsengvn.typekit.TypekitContextWrapper;
import com.viewpagerindicator.LinePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import static com.example.wing.workingsongpa.CourseList.DetailCourseListActivity.SPOT_DATA;

/**
 * Created by knightjym on 2017. 2. 18..
 */

public class SpotDetailActivity extends AppCompatActivity {

    ViewPager mPager;
    LinePageIndicator mIndicator;

    JSONObject spotData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spot_activity);
        getSupportActionBar().hide();

        ImageButton back = (ImageButton)findViewById(R.id.detail_back);
        back.setVisibility(View.GONE);
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
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
//        int height = size.y;

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
                //int reWidth = width - (int)((double)width * 0.2);
                //Bitmap scr = DataCenter.getInstance().resizeImge(resources,resID,reWidth);

                //fagment만들기
                mAdapter.addItem(resID);
            }

            mPager.setAdapter(mAdapter);
            //Bind the title indicator to the adapter
            mIndicator = (LinePageIndicator)findViewById(R.id.indicator);
            mIndicator.setViewPager(mPager);


            //subtitle
            String s_title_str =  spotData.getString(DataCenter.SUB_TITLE_KEY);
            TextView subTitle = (TextView)findViewById(R.id.sub_title);
            if (s_title_str.length() > 0 )
            {
                subTitle.setText(s_title_str);
            }else
            {
                subTitle.setVisibility(View.GONE);
            }

            /////////////////////title/////////////////////
            String title_str =  spotData.getString(DataCenter.TITLE_KEY);
            TextView title = (TextView)findViewById(R.id.title);
            if (title_str .length() > 0 )
            {

                title.setText("["+title_str+"]");
            }else
            {
                title.setVisibility(View.GONE);
            }

            /////////////////////info/////////////////////
            String info_str =  spotData.getString(DataCenter.SPOT_INFO);
            TextView intoText = (TextView)findViewById(R.id.info_text);
            if (info_str .length() > 0 )
            {
                intoText .setText(info_str);
            }else
            {
                intoText.setVisibility(View.GONE);
            }

            /////////////////////MSG/////////////////////
            String msg_str =  spotData.getString(DataCenter.SPOT_MSG);
            TextView msgText = (TextView)findViewById(R.id.msg_text);
            if (msg_str .length() > 0 )
            {
                msgText.setText(msg_str);
            }else
            {
                msgText.setVisibility(View.GONE);
            }

            /////////////////////sub MSG/////////////////////
            LinearLayout layout = (LinearLayout)findViewById(R.id.contents_layout);
            float d = getResources().getDisplayMetrics().density;

            JSONArray sub_msgs = spotData.getJSONArray(DataCenter.SPOT_SUBMSG);
            if (sub_msgs != null && sub_msgs.length() > 0)
            {
                Typeface type = Typeface
                        .createFromAsset(this.getAssets(), "NanumGothic.ttf");

                for (int i = 0; i<sub_msgs.length(); i++)
                {
                    JSONObject sub_msg_dic = sub_msgs.getJSONObject(i);
                    String sub_msg_title = sub_msg_dic.getString(DataCenter.TITLE_KEY);
                    String sub_msg_text = sub_msg_dic.getString(DataCenter.TEXT_KEY);

                    //msg title
                    TextView titleTV = new TextView(getApplicationContext());
                    titleTV.setText(sub_msg_title);
                    titleTV.setTextSize(TypedValue.COMPLEX_UNIT_SP,11);
                    titleTV.setTextColor(Color.parseColor("#000000"));
                    LinearLayout.LayoutParams titleParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    titleParam.setMargins((int)(10 * d),(int)(6 * d),(int)(10 * d),0);
                    titleTV.setLayoutParams(titleParam);
                    titleTV.setTypeface(type);
                    layout.addView(titleTV);

                    //msg text
                    TextView textTV = new TextView(getApplicationContext());
                    textTV.setText(sub_msg_text);
                    textTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    textTV.setTextColor(Color.parseColor("#777777"));//b6b6b6
                    textTV.setLineSpacing(7,1);

//                    textTV.setPadding(10, 6, 10, 0);
                    LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    textParam.setMargins((int)(10 * d),(int)(6 * d),(int)(10 * d),(int)(10 * d));
                    textTV.setLayoutParams(textParam);
                    textTV.setTypeface(type);
                    layout.addView(textTV);

                }

            }

        }catch (JSONException je)
        {
            Log.e("jsonErr", "json에러입니당~", je);
            spotData = null;
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }

}