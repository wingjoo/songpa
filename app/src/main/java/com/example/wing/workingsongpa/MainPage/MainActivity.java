package com.example.wing.workingsongpa.MainPage;


import android.content.Context;

import android.content.Intent;
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
import com.example.wing.workingsongpa.CourseList.RecommandCourseListFlagment;
import com.example.wing.workingsongpa.Database.DataCenter;

import com.example.wing.workingsongpa.MapTab.MapActivity;
import com.example.wing.workingsongpa.R;
import com.tsengvn.typekit.TypekitContextWrapper;

import static com.example.wing.workingsongpa.CourseList.DetailCourseListActivity.SPOT_DATA;
import static com.example.wing.workingsongpa.MapTab.MapActivity.MAP_ACTIVITY_INTENT;


public class MainActivity extends AppCompatActivity {

    private NonSwipeableViewPager mViewPager;

    private TabLayout tabLayout;


    private boolean isSelectedCourse;

    private ImageButton mapBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (android.os.Build.VERSION.SDK_INT >= 21) {
//            Window window = this.getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
//        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        // ****************************데이터*********************** //

        //courseList
        DataCenter.getInstance().loadDataWithContext(this);

        isSelectedCourse = false;
        // ****************************Map*********************** //
        mapBtn = (ImageButton) findViewById(R.id.map_icon);

        mapBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //지도 뷰 띄우기

                Intent intent = new Intent(MainActivity.this , MapActivity.class);

                String sendStr = null;
                //intent를 통해서 json객체 전송(string으로 변환
                intent.putExtra(MAP_ACTIVITY_INTENT, sendStr);
                startActivity(intent);

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
        tabLayout.getTabAt(0).setText("추천코스");
        tabLayout.getTabAt(1).setText("관광영역");
//        tabLayout.getTabAt(0).setIcon(R.drawable.icon_list_p);
//        tabLayout.getTabAt(1).setIcon(R.drawable.icon_map_n);
        //createTabIcons();
//
//        //탭 클릭시 행동
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
////                if (tab.getPosition() == 0) {
////                    tab.setIcon(R.drawable.icon_list_p);
////                } else {
////                    tab.setIcon(R.drawable.icon_map_p);
////                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
////                if (tab.getPosition() == 0) {
////                    tab.setIcon(R.drawable.icon_list_n);
////                } else {
////                    tab.setIcon(R.drawable.icon_map_n);
////                }
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });


//        //페이지 전환
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//                if (tab.getPosition() == 0) {//리스트탭 선택 메뉴버튼 숨김, 오른쪽 버튼
//
////                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
////                    getSupportActionBar().setHomeButtonEnabled(false);
//                } else {//맵 탭 선택, 메뉴 버튼 show, add버튼 추가
////                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
////                    getSupportActionBar().setHomeButtonEnabled(true);
//
////                    if (!isSelectedCourse)
////                    {
////                        //if 아무런 코스가 선택되지 않았다면..
////                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
////                        drawer.openDrawer(GravityCompat.START);
////                    }
//                }
//                //mViewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });
    }


    private void createViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        RecommandCourseListFlagment recommandList = new RecommandCourseListFlagment();
        AreaCourseListFlagment areaList = new AreaCourseListFlagment();


        adapter.addFrag(recommandList, "Tab 1");
        adapter.addFrag(areaList, "Tab 2");
        viewPager.setAdapter(adapter);
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

