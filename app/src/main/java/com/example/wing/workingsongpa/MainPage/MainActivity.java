package com.example.wing.workingsongpa.MainPage;


import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.AppBarLayout;

import android.support.design.widget.TabLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.wing.workingsongpa.CourseList.AreaCourseListFlagment;
import com.example.wing.workingsongpa.CourseList.DetailCourseListActivity;
import com.example.wing.workingsongpa.CourseList.RecommandCourseListFlagment;
import com.example.wing.workingsongpa.Database.DataCenter;

import com.example.wing.workingsongpa.MapTab.MapActivity;
import com.example.wing.workingsongpa.R;
import com.example.wing.workingsongpa.SplashActivity;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.github.rahatarmanahmed.cpv.CircularProgressViewAdapter;
import com.tsengvn.typekit.TypekitContextWrapper;

import static com.example.wing.workingsongpa.CourseList.DetailCourseListActivity.SPOT_DATA;
import static com.example.wing.workingsongpa.MapTab.MapActivity.MAP_ACTIVITY_INTENT;


public class MainActivity extends AppCompatActivity {

    private NonSwipeableViewPager mViewPager;

    private TabLayout tabLayout;

    private ImageButton mapBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(this, SplashActivity.class));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // ****************************데이터*********************** //

        //courseList
        new Thread(new Runnable() {
            public void run() {
                DataCenter.getInstance().loadDataWithContext(getApplicationContext());
            }
        }).start();


//        isSelectedCourse = false;
        // ****************************Map*********************** //
        mapBtn = (ImageButton) findViewById(R.id.map_button);

        mapBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                final ProgressDialog pd = ProgressDialog.show(MainActivity.this,
                        "", "Loading...", true);

                new Thread(new Runnable(){
                    public void run(){
                        //지도 뷰 띄우기
                        Intent intent = new Intent(MainActivity.this , MapActivity.class);

                        String sendStr = null;
                        //intent를 통해서 json객체 전송(string으로 변환
                        intent.putExtra(MAP_ACTIVITY_INTENT, sendStr);
                        startActivity(intent);

                        pd.dismiss();
                    }
                }).start();
            }
        });


        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(0);  // clear all scroll flags

        // ****************************뷰페이져*********************** //
        mViewPager = (NonSwipeableViewPager) findViewById(R.id.container);
        createViewPager(mViewPager);

        // ****************************탭레이아웃*********************** //
        //탭레이아웃
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
//        TabLayout.Tab listTab = tabLayout.newTab();
        //start 상태
        tabLayout.getTabAt(0).setText("추천테마길");
        tabLayout.getTabAt(1).setText("테마관광거점");
    }

    private void createViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        RecommandCourseListFlagment recommandList = new RecommandCourseListFlagment();
        AreaCourseListFlagment areaList = new AreaCourseListFlagment();

        adapter.addFrag(recommandList, "Tab 1");
        adapter.addFrag(areaList, "Tab 2");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (resultCode == RESULT_OK){
//            try {
//                //map fragment 이동
//                isSelectedCourse = true;
//                TabLayout.Tab tab = tabLayout.getTabAt(1);
//                tab.select();
//
//                JSONObject selectedCourseData = new JSONObject(data.getStringExtra(DetailCourseListActivity.SELECTED_COURSE));
//                mapFlagment.showCourse(selectedCourseData);
//
//            }catch (JSONException e)
//            {
//                Log.e("jsonErr", "Activity Error", e);
//            }
//
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }



}

