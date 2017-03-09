package com.example.wing.workingsongpa.CourseList;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wing.workingsongpa.ApplicationClass;
import com.example.wing.workingsongpa.R;


import org.json.JSONObject;

public class EntryAdapter extends ArrayAdapter<ApplicationClass.Item> {

	private Context context;
	private ArrayList<ApplicationClass.Item> items;
	private LayoutInflater vi;

	public EntryAdapter(Context context,ArrayList<ApplicationClass.Item> items) {
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
				sectionView.setText(si.getTitle());
			}else{
				// Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
				EntryItem item = (EntryItem)i;
				//view
				convertView = vi.inflate(R.layout.detail_course_list_item, parent, false);
				//v = vi.inflate(R.layout.detail_course_list_item, null);

				//*************UI구성**************//
				// 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
				ImageView iconImageView = (ImageView) convertView.findViewById(R.id.icon) ;
				TextView titleTextView = (TextView) convertView.findViewById(R.id.title) ;
				TextView subTiitleTextView = (TextView) convertView.findViewById(R.id.sub_title) ;
				TextView msgTextView = (TextView) convertView.findViewById(R.id.msg) ;

//				iconImageView.setImageDrawable(item.iconDrawable);

				// 아이템 내 각 위젯에 데이터 반영
				String title = item.getTitle();
				String subTitle = item.getSubTitle();
				String msgText = item.getMsg();

				if (item.imgScr != null)
				{
					iconImageView.setImageBitmap(item.imgScr);
				}

				if (title != null)
				{
					titleTextView.setText(title);
				}
				if (subTitle != null)
				{
					subTiitleTextView.setText(subTitle);
				}

				if (msgText != null)
				{
					msgTextView.setText(msgText);
				}
			}
		}
		return convertView;
	}

	public void updateTopItemWithData(Bitmap scr, JSONObject newData)
	{
		ApplicationClass.Item i = items.get(0);
		EntryItem item  = (EntryItem)i;
		item.setData(scr, newData);
	}

}
