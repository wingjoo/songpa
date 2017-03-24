package com.example.wing.workingsongpa.MapTab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.wing.workingsongpa.ApplicationClass;
import com.example.wing.workingsongpa.CourseList.SectionItem;
import com.example.wing.workingsongpa.R;

import java.util.ArrayList;

/**
 * Created by wing on 2017-02-11.
 */

public class DrawMenuAdapter extends ArrayAdapter<ApplicationClass.Item> {

    private Context context;
    private ArrayList<ApplicationClass.Item> items;
    private LayoutInflater vi;

    public DrawMenuAdapter(Context context,ArrayList<ApplicationClass.Item> items) {
        super(context,0, items);
        this.context = context;
        this.items = items;
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        //View v = convertView;

        final ApplicationClass.Item i = items.get(position);
        if (i != null) {
            if(i.isSection()){
                SectionItem si = (SectionItem)i;
                convertView = vi.inflate(R.layout.detail_course_list_header, null);

                convertView.setOnClickListener(null);
                convertView.setOnLongClickListener(null);
                convertView.setLongClickable(false);

                final TextView sectionView = (TextView) convertView.findViewById(R.id.title);
                //test
                if (position == 0)
                {
                    sectionView.setText("추천테마길");
                }else
                {
                    sectionView.setText("테마관광거점");
                }

            }else{
                // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
                DrawMenuItem item = (DrawMenuItem)i;
                //view
                convertView = vi.inflate(R.layout.drawmenu_item, parent, false);

                //*************UI구성**************//
                TextView titleTextView = (TextView) convertView.findViewById(R.id.title) ;
                // 아이템 내 각 위젯에 데이터 반영
                String title = item.getCourse_title();

                if (title.length() > 0)
                {
                    titleTextView.setText(title);
                }else
                {
                    titleTextView.setVisibility(View.GONE);
                }

                TextView subTitleView = (TextView) convertView.findViewById(R.id.sub_title) ;
                // 아이템 내 각 위젯에 데이터 반영
                String subTitle = item.getSub_title();

                if (subTitle.length() > 0)
                {
                    subTitleView.setText(subTitle);
                    subTitleView.setTextColor(item.getCourseColor());
                }else
                {
                    subTitleView.setVisibility(View.GONE);
                }
            }
        }
        return convertView;
    }

}
