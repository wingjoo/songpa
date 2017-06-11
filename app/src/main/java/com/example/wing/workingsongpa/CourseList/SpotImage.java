package com.example.wing.workingsongpa.CourseList;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.wing.workingsongpa.R;

import org.json.JSONObject;

/**
 * Created by knightjym on 2017. 2. 19..
 */

public class SpotImage  extends Fragment {


    //public Bitmap imgScr;
    public Integer imgID;
//    public void setIcon(Drawable icon) {
//        iconDrawable = icon ;
//    }
//
//    public Drawable getIcon() {
//        return this.iconDrawable ;
//    }

//    public SpotImage()
//    {
//
//    }


    public static SpotImage newInstance(Integer imgID) {
        SpotImage fragment = new SpotImage();

        fragment.imgID = imgID;

        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.spot_item_img, container, false);


       // SpotImage item = spotImgList.get(position);

        //*************UI구성**************//
        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconImageView = (ImageView) rootView.findViewById(R.id.img) ;

        // 아이템 내 각 위젯에 데이터 반영

        //미완성
        iconImageView.setImageResource(imgID);

        return rootView;
    }
}
