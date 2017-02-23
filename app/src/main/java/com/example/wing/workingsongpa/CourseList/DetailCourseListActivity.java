package com.example.wing.workingsongpa.CourseList;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.wing.workingsongpa.ApplicationClass;
import com.example.wing.workingsongpa.Database.DataCenter;
import com.example.wing.workingsongpa.R;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.wing.workingsongpa.CourseList.CourseListFlagment.COURSE_DATA;

public class DetailCourseListActivity extends AppCompatActivity {


    public final static String SPOT_DATA = "com.example.wing.SENDSPOTDATA";
    JSONObject courseData;
    ArrayList<ApplicationClass.Item> items = new ArrayList<ApplicationClass.Item>();
    public static final String SELECTED_COURSE = "selected_course";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_course_list);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        try
        {
            courseData = new JSONObject(intent.getStringExtra(COURSE_DATA));
        }catch (JSONException je)
        {
            Log.e("jsonErr", "json에러입니당~", je);
            courseData = null;
        }

        //풀스크린 안됨
       // fullScreencall();


        ImageButton back = (ImageButton)findViewById(R.id.detail_back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Do something with the value of the button
                finish();
            }
        });

        ImageButton goToMap = (ImageButton)findViewById(R.id.detail_goToMap);
        goToMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Do something with the value of the button
                Intent intent = new Intent();
                String sendStr = courseData.toString();
                intent.putExtra(SELECTED_COURSE,sendStr);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        //리스트 뷰 가져와서 아답터 셋
        ListView listview = (ListView)findViewById(R.id.detail_course_listView);

        if (courseData != null)
        {

            try {
                ImageView titleView = (ImageView)findViewById(R.id.detail_titleView);
                String res_url = courseData.getString(DataCenter.COURSE_IMG_URL).toString();

                Resources resources =  getResources();
                int courseID  = getResources().getIdentifier(res_url, "drawable", "com.example.wing.workingsongpa");
                Bitmap courseScr = DataCenter.getInstance().resizeImge(resources,courseID);

                titleView.setImageBitmap(courseScr);


                JSONArray courseList =  courseData.getJSONArray("course");
                //구간
                for(int c = 0; c < courseList .length(); c++) {
                    JSONObject courseObject = courseList.getJSONObject(c);
                    String c_info = courseObject.getString("course_info");
                    JSONArray spotList =  courseObject.getJSONArray("spot");
                    //[1,2,3,4,]
                    //스팟
                    items.add(new SectionItem(c_info));
                    for(int i = 0; i < spotList.length(); i++){
                        try
                        {

                            String spotIndex = spotList.getString(i).toString();
                            int position = Integer.parseInt(spotIndex);
                            JSONObject itemData = DataCenter.getInstance().getSpotItem(position);
                            /**********************************/
                            String img_url = itemData.getString(DataCenter.SPOT_MAIN_IMG).toString();
                            if (img_url == null || img_url.length() == 0)
                            {
                                img_url = "sample";
                            }
                            //이미지 리소스 아이디

                            int resID  = getResources().getIdentifier(img_url  , "drawable", "com.example.wing.workingsongpa");
                            Bitmap bScr = DataCenter.getInstance().resizeImge(resources,resID);

                            items.add(new EntryItem(bScr, itemData));
                        }catch (JSONException je)
                        {
                            Log.e("jsonErr", "json에러입니당~", je);
                            break;
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            EntryAdapter adapter = new EntryAdapter(this, items);
            listview.setAdapter(adapter);


        }


        //스팟 선택시 행동
        //디테일 화면으로 이동
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                EntryItem item = (EntryItem)parent.getItemAtPosition(position);

                // TODO : use item data.
                //next activity
                Intent intent = new Intent(DetailCourseListActivity.this , SpotDetailActivity.class);

                String sendStr = item.itemData.toString();
                //intent를 통해서 json객체 전송(string으로 변환
                intent.putExtra(SPOT_DATA, sendStr);


                startActivity(intent);
            }
        }) ;

    }




    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }



    public void fullScreencall() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
