package com.example.wing.workingsongpa.MainPage;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.wing.workingsongpa.ApplicationClass;
import com.example.wing.workingsongpa.CourseList.CourseListFlagment;
import com.example.wing.workingsongpa.CourseList.CourseListItem;
import com.example.wing.workingsongpa.CourseList.DetailCourseListActivity;
import com.example.wing.workingsongpa.Database.DataCenter;
import com.example.wing.workingsongpa.MapTab.MapFlagment;
import com.example.wing.workingsongpa.R;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.wing.workingsongpa.CourseList.CourseListFlagment.EXTRA_MESSAGE;
//import android.view.ViewGroup;
//
//import android.widget.TextView;



public class MainActivity extends AppCompatActivity {

    private NonSwipeableViewPager mViewPager;
    private TabLayout tabLayout;

    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private String[] navItems = {"Brown", "Cadet Blue", "Dark Olive Green",
            "Dark Orange", "Golden Rod"};
    private ListView lvNavList;

    private boolean isSelectedCourse;
    private MapFlagment mapFlagment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_map_manu_n);

        // ****************************데이터*********************** //

        //courseList
        DataCenter.getInstance().loadCourseListJSON(this);
        //spotList
        DataCenter.getInstance().loadSpotListJSON(this);

        DataCenter.getInstance().loadPathJSON(this);
        isSelectedCourse = false;

        // ****************************드로우 메뉴*********************** //

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawer.addDrawerListener(toggle);
        } else {
            drawer.setDrawerListener(toggle);
        }
        // toggle.setDrawerIndicatorEnabled(false);
        ArrayList<ApplicationClass.Item> items = new ArrayList<ApplicationClass.Item>();
        //기본 코스만 해결 하는걸로
        ArrayList<JSONObject> dataList = DataCenter.getInstance().allCourseList();
        for (JSONObject data: dataList) {
            items.add(new DrawMenuItem(data));
        }


        //adapt
        DrawMenuAdapter adapter = new DrawMenuAdapter(this,items);
        lvNavList = (ListView)findViewById(R.id.main_nav_menu);
        lvNavList.setAdapter(adapter);
        lvNavList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                //drawMenu Item
                DrawMenuItem item = (DrawMenuItem)parent.getItemAtPosition(position) ;
                //item.itemData
                JSONObject data =  item.itemData;
                mapFlagment.showCourse(data);
                isSelectedCourse = true;

                drawer.closeDrawer(lvNavList); // 추가됨
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
        tabLayout.getTabAt(0).setIcon(R.drawable.icon_list_p);
        tabLayout.getTabAt(1).setIcon(R.drawable.icon_map_n);
        //createTabIcons();
        //탭 클릭시 행동
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    tab.setIcon(R.drawable.icon_list_p);
                } else {
                    tab.setIcon(R.drawable.icon_map_p);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    tab.setIcon(R.drawable.icon_list_n);
                } else {
                    tab.setIcon(R.drawable.icon_map_n);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        //페이지 전환
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("TedPark", "로그 내용" + tab.getPosition());
                if (tab.getPosition() == 0) {//리스트탭 선택 메뉴버튼 숨김, 오른쪽 버튼

                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setHomeButtonEnabled(false);
                } else {//맵 탭 선택, 메뉴 버튼 show, add버튼 추가
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeButtonEnabled(true);

                    if (!isSelectedCourse)
                    {
                        //if 아무런 코스가 선택되지 않았다면..
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.openDrawer(GravityCompat.START);
                    }
                }
                //mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


    private void createViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        CourseListFlagment courseList = new CourseListFlagment();
        mapFlagment = new MapFlagment();
        mapFlagment.setArguments(new Bundle());

        adapter.addFrag(courseList, "Tab 1");
        adapter.addFrag(mapFlagment, "Tab 2");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK){
            try {
                //map fragment 이동
                isSelectedCourse = true;
                TabLayout.Tab tab = tabLayout.getTabAt(1);
                tab.select();

                JSONObject selectedCourseData = new JSONObject(data.getStringExtra(DetailCourseListActivity.SELECTED_COURSE));
                mapFlagment.showCourse(selectedCourseData);

            }catch (JSONException e)
            {
                Log.e("jsonErr", "Activity Error", e);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }

}

