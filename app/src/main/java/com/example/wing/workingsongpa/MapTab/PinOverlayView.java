package com.example.wing.workingsongpa.MapTab;

import android.content.Context;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wing.workingsongpa.Database.DataCenter;
import com.example.wing.workingsongpa.R;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by knightjym on 2017. 2. 20..
 */

public class PinOverlayView extends LinearLayout {

    private ImageView mainImgView;
    private TextView titleView;
    private TextView subTitleView;

    public PinOverlayView(Context context) {
        super(context);
        initView();
    }


    private void initView()
    {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.pin_overlay_view, this, false);
        addView(v);


        mainImgView = (ImageView) findViewById(R.id.main_img_view);

        titleView = (TextView) findViewById(R.id.pin_title);
        subTitleView = (TextView) findViewById(R.id.pin_sub_title);

    }

//    {
//        “id”:int
//        “title”:string
//        “sub_title:string
//        “next_spot_distan”:string
//        “msg”:string
//        “info”:string
//        “latitude”:float
//        “longitude”:float
//        “group_type:int
//        "default_img":string
//        "img_list":[string]
//    }

    public void setData(JSONObject data)
    {
        try {
            String res_url = data.getString(DataCenter.SPOT_MAIN_IMG).toString();
            Resources resources =  getResources();
            int resID  = getResources().getIdentifier(res_url, "drawable", "com.example.wing.workingsongpa");
            Bitmap bScr = DataCenter.getInstance().resizeImge(resources,resID);
            mainImgView.setImageBitmap(bScr);
            titleView.setText(data.getString(DataCenter.SPOT_TITLE).toString());
            subTitleView.setText(data.getString(DataCenter.SPOT_SUB_TITLE).toString());
        }catch (JSONException e)
        {

        }

    }


}
