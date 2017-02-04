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

import static com.example.wing.workingsongpa.R.drawable.t_road_n_01;

public class CourseListFlagment extends Fragment {

    String[] imageNames = {"t_road_n_01","t_road_n_02","t_road_n_03","t_road_n_02","t_road_n_03","t_road_n_02","t_road_n_03","t_road_n_02","t_road_n_03"};

    CourseListViewAdapter adapter;
    public final static String EXTRA_MESSAGE = "com.example.wing.SENDCELLDATA";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        // Adapter 생성
        adapter = new CourseListViewAdapter() ;

        //아답터를 이용해서 데이터 바인딩
        //ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST_MENU) ;

        //리스트 뷰 가져와서 아답터 셋
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listview = (ListView) rootView.findViewById(R.id.course_listView);
        listview.setAdapter(adapter) ;

        JSONArray courseList = DataCenter.getInstance().getCourseList();

        for(int i = 0; i < courseList .length(); i++){

            try
            {
                JSONObject itemData = courseList .getJSONObject(i);
                //이미지 리소스 아이디
                int resID  = getResources().getIdentifier(imageNames[i] , "drawable", "com.example.wing.workingsongpa");
                adapter.addItem(ContextCompat.getDrawable(getActivity(), resID),itemData);

            }catch (JSONException je)
            {
                Log.e("jsonErr", "json에러입니당~", je);
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

                //intent를 통해서 json객체 전송(string으로 변환
                intent.putExtra(EXTRA_MESSAGE, item.getItemData().toString());
                startActivity(intent);
            }
        }) ;

        return rootView;
    }

}
