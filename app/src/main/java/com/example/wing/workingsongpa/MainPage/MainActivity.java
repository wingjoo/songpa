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

import com.example.wing.workingsongpa.CourseList.CourseListFlagment;
import com.example.wing.workingsongpa.CourseList.CourseListItem;
import com.example.wing.workingsongpa.CourseList.DetailCourseListActivity;
import com.example.wing.workingsongpa.Database.DataCenter;
import com.example.wing.workingsongpa.MapTab.MapFlagment;
import com.example.wing.workingsongpa.R;
import com.tsengvn.typekit.TypekitContextWrapper;
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
//    private FrameLayout flContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        TextView title =  (TextView)toolbar.findViewById(R.id.toolbar_title);
//        title.setText("걷고싶은 거리 송파");


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_map_manu_n);

        // ****************************데이터*********************** //

        //courseList
        DataCenter.getInstance().loadCourseListJSON(this);
        //spotList
        DataCenter.getInstance().loadSpotListJSON(this);

        DataCenter.getInstance().loadPathJSON(this);


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

        lvNavList = (ListView)findViewById(R.id.main_nav_menu);
//        flContainer = (FrameLayout)findViewById(R.id.main_nav_menu);

        lvNavList.setAdapter(
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));
//        lvNavList.setOnItemClickListener(new DrawerItemClickListener());

        //터치 안먹음
        lvNavList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                switch (position) {
                    case 0:
                        Log.d("click","LoadCourseJson에러입니당~");
                        break;
                    case 1:
                        Log.d("click","LoadCourseJson에러입니당~");
                        break;
                    case 2:
                        Log.d("click","LoadCourseJson에러입니당~");
                        break;
                    case 3:
                        Log.d("click","LoadCourseJson에러입니당~");
                        break;
                    case 4:
                        Log.d("click","LoadCourseJson에러입니당~");
                        break;

                }
                drawer.closeDrawer(lvNavList); // 추가됨
            }
        }) ;



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

                    //if 아무런 코스가 선택되지 않았다면..
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.openDrawer(GravityCompat.START);
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


    ////////////////////////드로우 메뉴 테스트
    /*
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position,
                                long id) {
            switch (position) {
                case 0:

                    break;
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:

                    break;

            }
            drawer.closeDrawer(lvNavList); // 추가됨
        }
    }
*/
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);


//
//        //플로팅 버튼
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//
//        //온클릭 리스너가 터치시 응답
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


    private void createViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        CourseListFlagment courseList = new CourseListFlagment();
        MapFlagment mapFlagment = new MapFlagment();
        mapFlagment.setArguments(new Bundle());

        adapter.addFrag(courseList, "Tab 1");
        adapter.addFrag(mapFlagment, "Tab 2");
        viewPager.setAdapter(adapter);
    }

    /*
    //메뉴 구성시 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        return true;
    }
    //추가 메뉴


    //Android 3.0 이상에서는 메뉴 항목이 앱 바에 표시되어 있는 경우 항상 옵션 메뉴가 열려 있는 것으로 간주됩니다. 이벤트가 발생하고 메뉴 업데이트를 수행하고자 하는 경우,
    // invalidateOptionsMenu()를 호출하여 시스템이 onPrepareOptionsMenu()를 호출하도록 요청해야 합니다.
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        return super.onPrepareOptionsMenu(menu);
    }



        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            return super.onOptionsItemSelected(item);
        }

    //메뉴가 눌렸을때 실행
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        //메뉴 닫기
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_camera:
            case R.id.nav_gallery:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
*/
    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }

}

