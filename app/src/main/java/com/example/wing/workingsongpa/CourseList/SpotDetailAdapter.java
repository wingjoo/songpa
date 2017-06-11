package com.example.wing.workingsongpa.CourseList;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.wing.workingsongpa.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by knightjym on 2017. 2. 19..
 */

public class SpotDetailAdapter extends FragmentPagerAdapter {


    private final List<SpotImage> spotImgList = new ArrayList<>();


    public SpotDetailAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        //return TestFragment.newInstance(CONTENT[position % CONTENT.length]);
        return spotImgList.get(position);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return spotImgList .size();
    }



    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }


//    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        final Context context = parent.getContext();
//
//        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
//        if (convertView == null) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.spot_item_img, parent, false);
//        }
//
//        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
//        SpotImage item = spotImgList.get(position);
//
//        //*************UI구성**************//
//        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
//        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.img) ;
//
//        // 아이템 내 각 위젯에 데이터 반영
//        iconImageView.setImageDrawable(item.getIcon());
//
//        return convertView;
//    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
//    public void addItem(Bitmap data) {
//        SpotImage item = SpotImage.newInstance(data);
//        spotImgList .add(item);
//    }

    public void addItem(Integer imgID) {
        SpotImage item = SpotImage.newInstance(imgID);
        spotImgList .add(item);
    }
}
