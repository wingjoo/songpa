package com.example.wing.workingsongpa.MapTab;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

//import com.example.wing.workingsongpa.R;
import com.example.wing.workingsongpa.R;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;

/**
 * Created by wing on 2016-12-23.
 */

//핀 클릭시 나오는 뷰
public class CustomOverlaView extends OverlayView {

    private View mCalloutView;
    private TextView mCalloutText;
    //private View mRightArrow;


    public CustomOverlaView(Context context, NMapOverlay itemOverlay, NMapOverlayItem item, Rect itemBounds) {
        super(context, itemOverlay, item, itemBounds);

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater)getContext().getSystemService(infService);
        li.inflate(R.layout.overay_view, this, true);

        mCalloutView = findViewById(R.id.callout_overlay);
        mCalloutText = (TextView)mCalloutView.findViewById(R.id.callout_text);
        //mRightArrow = findViewById(R.id.callout_rightArrow);

        mCalloutView.setOnClickListener(callOutClickListener);

        mCalloutText.setText(item.getTitle());

//        if (item instanceof NMapPOIitem) {
//            if (((NMapPOIitem)item).hasRightAccessory() == false) {
//                mRightArrow.setVisibility(View.GONE);
//            }
//        }
    }

    private final View.OnClickListener callOutClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(null, mItemOverlay, mOverlayItem);
            }
        }
    };

}