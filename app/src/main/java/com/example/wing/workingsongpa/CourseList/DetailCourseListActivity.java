package com.example.wing.workingsongpa.CourseList;

import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wing.workingsongpa.R;

public class DetailCourseListActivity extends AppCompatActivity {

    DetailCourseListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_course_list);

        Intent intent = getIntent();
        //String msg = intent.getStringExtra(CourseListFlagment.EXTRA_MESSAGE);
        //full screen bug
        fullScreencall();

        // Adapter 생성
        adapter = new DetailCourseListAdapter() ;

        //아답터를 이용해서 데이터 바인딩
        //ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU) ;

        //리스트 뷰 가져와서 아답터 셋
        ListView listview = (ListView)findViewById(R.id.detail_course_listView);
        listview.setAdapter(adapter) ;

        //        for (String imageName:imageNames) {
//            int resID  = getResources().getIdentifier(imageName , "drawable", "com.example.wing.workingsongpa");
//            adapter.addItem(ContextCompat.getDrawable(getActivity(), resID));
//        }


        //리스트 선택시 행동
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
