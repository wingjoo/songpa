package com.example.wing.workingsongpa.CourseList;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wing.workingsongpa.R;

public class CourseListFlagment extends Fragment {

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

        //리스트 선택시 행동
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                CourseListItem item = (CourseListItem) parent.getItemAtPosition(position) ;

                // TODO : use item data.
                //next activity
                Intent intent = new Intent(getActivity(), DetailCourseListActivity.class);

                intent.putExtra(EXTRA_MESSAGE, item.getTitle());
                startActivity(intent);
            }
        }) ;

        // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.sample),
                "Box") ;
        // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.sample),
                "Circle") ;
        // 세 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.sample),
                "Ind") ;


        return rootView;
    }




}
