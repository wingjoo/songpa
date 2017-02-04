package com.example.wing.workingsongpa.CourseList;

import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wing.workingsongpa.Database.DataCenter;
import com.example.wing.workingsongpa.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.wing.workingsongpa.CourseList.CourseListFlagment.EXTRA_MESSAGE;


public class DetailCourseListActivity extends AppCompatActivity {

    DetailCourseListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_course_list);

        //풀스크린 안됨
        fullScreencall();

        JSONObject jsonObj;
        Intent intent = getIntent();
        try
        {
            jsonObj = new JSONObject(intent.getStringExtra(EXTRA_MESSAGE));
        }catch (JSONException je)
        {
            Log.e("jsonErr", "json에러입니당~", je);
            jsonObj = null;
        }

        if (jsonObj != null)
        {
            // Adapter 생성
            adapter = new DetailCourseListAdapter() ;

            //리스트 뷰 가져와서 아답터 셋
            ListView listview = (ListView)findViewById(R.id.detail_course_listView);
            listview.setAdapter(adapter) ;

            //!!!!!!!!!!!!!!section으로 나눠서 진행으로 변경해야됨 >> 구글링 필요
            try {
                JSONArray courseList =  jsonObj.getJSONArray("course");
                //구간
                for(int c = 0; c < courseList .length(); c++) {
                    JSONObject courseObject = courseList.getJSONObject(c);
                    String c_info = courseObject.getString("course_info");
                    JSONArray spotList =  courseObject.getJSONArray("spot");
                    //스팟
                    for(int i = 0; i < spotList.length(); i++){
                        try
                        {
                            /**********************************/
                            JSONObject itemData = spotList.getJSONObject(i);
                            String img_url = itemData.getString("default_img").toString();
                            //이미지 리소스 아이디
                            int resID  = getResources().getIdentifier(img_url  , "drawable", "com.example.wing.workingsongpa");
                            adapter.addItem(ContextCompat.getDrawable(this, resID),itemData);

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


            //스팟 선택시 행동
            //디테일 화면으로 이동
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    // get item
                    DetailCourseListItem item = (DetailCourseListItem)parent.getItemAtPosition(position) ;

                    // TODO : use item data.
                    //next activity
//                Intent intent = new Intent(this, DetailCourseListActivity.class);
//
//                startActivity(intent);
                }
            }) ;
        }

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
