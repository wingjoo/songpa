package com.example.wing.workingsongpa.CourseList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;

import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;

import android.widget.AdapterView;

import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;


import com.example.wing.workingsongpa.AppBarHidden.FadingActionBarHelper;
import com.example.wing.workingsongpa.ApplicationClass;
import com.example.wing.workingsongpa.Database.DataCenter;

import com.example.wing.workingsongpa.MapTab.MapActivity;
import com.example.wing.workingsongpa.ProgressTask;
import com.example.wing.workingsongpa.R;


import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.wing.workingsongpa.CourseList.RecommandCourseListFlagment.COURSE_DATA;
import static com.example.wing.workingsongpa.MapTab.MapActivity.MAP_ACTIVITY_INTENT;



public class DetailCourseListActivity extends AppCompatActivity {

    public enum PageType
    {
        PageTypeRecommand,
        PageTypeArea
    }


    public final static String SPOT_DATA = "com.example.wing.SENDSPOTDATA";

    private PageType pageCoureType;
    JSONObject courseData;

    ArrayList<ApplicationClass.Item> items = new ArrayList<ApplicationClass.Item>();

    private int imageWidth;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_course_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        ColorDrawable clear = new ColorDrawable(0xFF0000FF);
//        getSupportActionBar().setBackgroundDrawable(clear);
        //getSupportActionBar().setElevation(0);

        FrameLayout layout = (FrameLayout)findViewById(R.id.content_layout);

//        toolbar.setBackgroundDrawable(cd);
//
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        imageWidth = size.x;
        resources =  getResources();


        Intent intent = getIntent();
        String[] extraList = intent.getStringExtra(COURSE_DATA).split("///");
        String flagStr =  extraList[0];
        String extraStr =  extraList[1];

        if (flagStr.equals("area"))
        {
            pageCoureType = PageType.PageTypeArea;
        }else
        {
            pageCoureType = PageType.PageTypeRecommand;
        }

        try
        {
            courseData = new JSONObject(extraStr);

            //titleView
            DetailCourseListHeader header = new DetailCourseListHeader(this);
//                ImageView titleView = (ImageView)findViewById(R.id.detail_titleView);
            String res_url = courseData.getString(DataCenter.COURSE_IMG_URL).toString();


            int courseID  = getResources().getIdentifier(res_url, "drawable", "com.example.wing.workingsongpa");
            //resizing
            Bitmap courseScr = DataCenter.getInstance().resizeImge(resources, courseID, imageWidth);
//                Bitmap courseScr = BitmapFactory.decodeResource(resources,courseID);
            header.setData(courseScr,courseData);
//
//                titleView.setImageBitmap(courseScr);

            int colorNum;
            if (pageCoureType == PageType.PageTypeRecommand)
            {
                DataCenter.CourseType courseType = DataCenter.getInstance().getCourseTypeWithID(courseData.getInt(DataCenter.ID));
                colorNum = DataCenter.getInstance().getColorDrawableNumWithType(courseType);
            }else
            {
                colorNum = 0xfbb829;
            }

            //#fbb829

            ColorDrawable cd = new ColorDrawable(colorNum);

            FadingActionBarHelper helper = new FadingActionBarHelper()
                    .actionBarBackground(cd)
                    .headerView(header)
                    .contentLayout(R.layout.course_list_view);

            View helperView = helper.createView(this);
            helper.initActionBar(this);
            layout.addView(helperView);

            //리스트 뷰 가져와서 아답터 셋
//                ListView listview = (ListView)findViewById(R.id.cours);
            ListView listView = (ListView) findViewById(android.R.id.list);

            JSONArray courseList;
            //데이트길 처리
            if (pageCoureType == PageType.PageTypeRecommand && courseData.getInt(DataCenter.ID) == 4)
            {
                JSONObject dateCourseData = courseData.getJSONObject(DataCenter.COURSE_COURSELIST);
                JSONArray dayCourseList = dateCourseData.getJSONArray("day");
                JSONArray nightCourseList = dateCourseData.getJSONArray("night");
                JSONArray result = new JSONArray();
                for (int i = 0; i < dayCourseList.length(); i++) {
                    result.put(dayCourseList.get(i));
                }
                for (int i = 0; i < nightCourseList.length(); i++) {
                    result.put(nightCourseList.get(i));
                }
                courseList = result;
            }


            courseList =  courseData.getJSONArray(DataCenter.COURSE_COURSELIST);

            EntryAdapter adapter = new EntryAdapter(this, items);
            listView.setAdapter(adapter);

            //////
            if (pageCoureType == PageType.PageTypeRecommand)
            {
                addAllRecommandCouseData(courseList);
            }else
            {
                addAllAreaCouseData(courseList);
            }

            //스팟 선택시 행동
            //디테일 화면으로 이동
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    final EntryItem item = (EntryItem)parent.getItemAtPosition(position);
                    // get item
                    final ProgressDialog pd = ProgressDialog.show(DetailCourseListActivity.this,
                            "", "Loading...", true);

                    new Thread(new Runnable(){
                        public void run(){

                            //next activity
                            Intent intent = new Intent(DetailCourseListActivity.this , SpotDetailActivity.class);

                            String sendStr = item.itemData.toString();
                            //intent를 통해서 json객체 전송(string으로 변환
                            intent.putExtra(SPOT_DATA, sendStr);

                            startActivity(intent);

                            pd.dismiss();
                        }
                    }).start();
                }
            });
        }catch (JSONException je)
        {
            Log.e("jsonErr", "json에러입니당~", je);
            courseData = null;
        }


//        ImageButton back = (ImageButton)findViewById(R.id.detail_back);
//        back.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                // Do something with the value of the button
//                finish();
//            }
//        });
//
        ImageButton goToMap = (ImageButton)findViewById(R.id.detail_goToMap);
        goToMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                final ProgressDialog pd = ProgressDialog.show(DetailCourseListActivity.this,
                        "", "Loading...", true);

                new Thread(new Runnable(){
                    public void run(){
                        // Do something with the value of the button
                        Intent intent = new Intent(DetailCourseListActivity.this , MapActivity.class);

                        String sendStr;
                        if (pageCoureType == PageType.PageTypeRecommand)
                        {
                            sendStr = "recommand///" + courseData.toString();
                        }else
                        {
                            sendStr = "area///" + courseData.toString();
                        }

                        //intent를 통해서 json객체 전송(string으로 변환
                        intent.putExtra(MAP_ACTIVITY_INTENT, sendStr);
                        startActivity(intent);
                        pd.dismiss();
                    }
                }).start();



            }
        });



    }



    private void addAllRecommandCouseData(JSONArray courseList )
    {
        try
        {
            //구간
            for(int c = 0; c < courseList .length(); c++) {
                JSONObject courseObject = courseList.getJSONObject(c);
                String c_info = courseObject.getString("course_info");
                JSONArray spotList = courseObject.getJSONArray("spot");
                //[1,2,3,4,]
                //스팟
                items.add(new SectionItem(c_info));
                for (int i = 0; i < spotList.length(); i++) {


                    String spotIndex = spotList.getString(i).toString();
                    int position = Integer.parseInt(spotIndex);
                    JSONObject itemData = DataCenter.getInstance().getSpotItem(position);
                    /**********************************/
                    String img_url = itemData.getString(DataCenter.SPOT_MAIN_IMG).toString();
                    if (img_url == null || img_url.length() == 0) {
                        img_url = "list_img";
                    }
                    //이미지 리소스 아이디

                    int resID = getResources().getIdentifier(img_url, "drawable", "com.example.wing.workingsongpa");
                    Bitmap bScr = DataCenter.getInstance().resizeImge(resources, resID, imageWidth / 4);

                    items.add(new EntryItem(bScr, itemData));

                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void addAllAreaCouseData(JSONArray spotList )
    {
        try
        {
            //String c_info = courseObject.getString("course_info");
            //items.add(new SectionItem(c_info));
            //구간
            for (int i = 0; i < spotList.length(); i++) {
                String spotIndex = spotList.getString(i).toString();
                int position = Integer.parseInt(spotIndex);
                JSONObject itemData = DataCenter.getInstance().getSpotItem(position);
                /**********************************/
                String img_url = itemData.getString(DataCenter.SPOT_MAIN_IMG).toString();
                if (img_url == null || img_url.length() == 0) {
                    img_url = "list_img";
                }
                //이미지 리소스 아이디

                int resID = getResources().getIdentifier(img_url, "drawable", "com.example.wing.workingsongpa");
                Bitmap bScr = DataCenter.getInstance().resizeImge(resources, resID, imageWidth / 4);

                items.add(new EntryItem(bScr, itemData));
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }





}