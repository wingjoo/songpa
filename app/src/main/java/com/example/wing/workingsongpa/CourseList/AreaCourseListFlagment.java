package com.example.wing.workingsongpa.CourseList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wing.workingsongpa.Database.DataCenter;
import com.example.wing.workingsongpa.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by knightjym on 2017. 2. 23..
 */

public class AreaCourseListFlagment extends Fragment {

    CourseListViewAdapter adapter;
    public final static String COURSE_DATA = "com.example.wing.SENDCOURSEDATA";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        adapter = new CourseListViewAdapter() ;
        // Adapter 생성

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        //리스트 뷰 가져와서 아답터 셋
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listview = (ListView) rootView.findViewById(R.id.course_listView);
        listview.setAdapter(adapter) ;

        ArrayList<JSONObject> courseList = DataCenter.getInstance().getAreaList();
        for (JSONObject data:courseList) {
            try {
                String res_url = data.getString(DataCenter.COURSE_IMG_URL).toString();

                Resources resources =  getResources();
                int resID  = getResources().getIdentifier(res_url, "drawable", "com.example.wing.workingsongpa");
                Bitmap src = DataCenter.getInstance().resizeImge(resources,resID,width);
//                Bitmap src = BitmapFactory.decodeResource(resources,resID);
                adapter.addItem(src,data);
//                adapter.addItem(ContextCompat.getDrawable(getActivity(), resID),data);
            }catch (JSONException e)
            {
                Log.e("jsonErr", "list image load에러입니당~", e);
            }
        }

        //리스트 선택시 행동
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
// get item
                final CourseListItem item = (CourseListItem)parent.getItemAtPosition(position) ;

                final ProgressDialog pd = ProgressDialog.show(getActivity(),
                        "", "Loading...", true);

                new Thread(new Runnable(){
                    public void run(){

                        // TODO : use item data.
                        //next activity
                        Intent intent = new Intent(getActivity(), DetailCourseListActivity.class);

                        String sendStr = "area///" + item.getItemData().toString() ;
                        //intent를 통해서 json객체 전송(string으로 변환
                        intent.putExtra(COURSE_DATA, sendStr);
                        startActivity(intent);

                        pd.dismiss();
                    }
                }).start();

            }
        }) ;

        return rootView;
    }

}
