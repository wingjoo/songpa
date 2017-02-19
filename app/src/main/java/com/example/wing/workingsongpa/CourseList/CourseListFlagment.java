package com.example.wing.workingsongpa.CourseList;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wing.workingsongpa.Database.DataCenter;
import com.example.wing.workingsongpa.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CourseListFlagment extends Fragment {

    CourseListViewAdapter adapter;
    public final static String COURSE_DATA = "com.example.wing.SENDCOURSEDATA";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        adapter = new CourseListViewAdapter() ;
        // Adapter 생성

        //아답터를 이용해서 데이터 바인딩
        //ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU) ;

        //리스트 뷰 가져와서 아답터 셋
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listview = (ListView) rootView.findViewById(R.id.course_listView);
        listview.setAdapter(adapter) ;

        //JSONArray courseList = DataCenter.getInstance().getCourseList();
        ArrayList<JSONObject> courseList = DataCenter.getInstance().getCourseList();
        for (JSONObject data:courseList) {
            try {
                String res_url = data.getString(DataCenter.COURSE_IMG_URL).toString();
                int resID  = getResources().getIdentifier(res_url, "drawable", "com.example.wing.workingsongpa");
                adapter.addItem(ContextCompat.getDrawable(getActivity(), resID),data);
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
                CourseListItem item = (CourseListItem)parent.getItemAtPosition(position) ;

                // TODO : use item data.
                //next activity
                Intent intent = new Intent(getActivity(), DetailCourseListActivity.class);

                String sendStr = item.getItemData().toString();
                //intent를 통해서 json객체 전송(string으로 변환
                intent.putExtra(COURSE_DATA, sendStr);

                int requestCode = 1;
                startActivityForResult(intent, requestCode);
            }
        }) ;

        return rootView;
    }

}
