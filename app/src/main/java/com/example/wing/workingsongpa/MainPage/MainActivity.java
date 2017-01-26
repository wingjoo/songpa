package com.example.wing.workingsongpa.MainPage;

import android.content.res.Configuration;
import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.wing.workingsongpa.CourseList.CourseListFlagment;
import com.example.wing.workingsongpa.Database.DBmanager;
import com.example.wing.workingsongpa.MapTab.MapFlagment;
import com.example.wing.workingsongpa.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//import android.view.ViewGroup;
//
//import android.widget.TextView;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPagerAdapter mSectionsPagerAdapter;
    private NonSwipeableViewPager mViewPager;
    private TabLayout tabLayout;
    private DBmanager dbmanager;

    private ActionBarDrawerToggle toggle;

    DrawerLayout dlDrawer;
    ActionBarDrawerToggle dtToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title =  (TextView)toolbar.findViewById(R.id.toolbar_title);
        title.setText("걷고싶은 거리 송파");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        // ****************************데이터*********************** //
        //최초 실행시 뻑남
        //카피 하면서 이슈 있는듯 함
        //DB
        dbmanager = new DBmanager(this);
        try{
            dbmanager.createDataBase();
        }catch (IOException ioe)
        {
            throw new Error("unable to create dababase");
        }

        try{
            dbmanager.openDataBase();

        }catch (SQLException sqle)
        {
            throw sqle;
        }

//        String testData = dbmanager.getResult();


        // ****************************드로우 메뉴*********************** //

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(0);  // clear all scroll flags

        // ****************************뷰페이져*********************** //
        mViewPager = (NonSwipeableViewPager) findViewById(R.id.container);
        createViewPager(mViewPager);

        // ****************************탭레이아웃*********************** //
        //탭레이아웃
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        createTabIcons();

        //페이지 전환
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("TedPark","로그 내용" + tab.getPosition());
                if (tab.getPosition() == 0)
                {//리스트탭 선택 메뉴버튼 숨김, 오른쪽 버튼

                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setHomeButtonEnabled(false);
                }else
                {//맵 탭 선택, 메뉴 버튼 show, add버튼 추가
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeButtonEnabled(true);
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
    }


    private void createViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        CourseListFlagment courseList = new CourseListFlagment();
        MapFlagment mapFlagment = new MapFlagment();
        mapFlagment.setArguments(new Bundle());

        adapter.addFrag(courseList, "Tab 1");
        adapter.addFrag(mapFlagment, "Tab 2");
        viewPager.setAdapter(adapter);
    }

    //탭 아이콘 셋팅
    private void createTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
//        tabOne.setText("Tab 1");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_map_no_01, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
//        tabTwo.setText("Tab 2");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_map_no_02, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
