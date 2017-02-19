package com.example.wing.workingsongpa.CourseList;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.example.wing.workingsongpa.ApplicationClass;
import com.example.wing.workingsongpa.Database.DataCenter;

import org.json.JSONException;
import org.json.JSONObject;

public class EntryItem implements ApplicationClass.Item {

//	public final Drawable iconDrawable;
	public final Bitmap imgScr;
	public final JSONObject itemData;

	private String title;
	private String subTitle;

//	public EntryItem(Drawable icon, JSONObject newData) {
//
//		iconDrawable = icon;
//		itemData = newData;
//		try{
//			this.title =  itemData.getString(DataCenter.SPOT_TITLE).toString();
//			this.subTitle = itemData.getString(DataCenter.SPOT_SUB_TITLE).toString();
//		}catch (JSONException je)
//		{
//			title = "no title";
//			subTitle = "no subtitle";
//		}
//	}

	public EntryItem(Bitmap scr, JSONObject newData) {

		imgScr = scr;
		itemData = newData;
		try{
			this.title =  itemData.getString(DataCenter.SPOT_TITLE).toString();
			this.subTitle = itemData.getString(DataCenter.SPOT_SUB_TITLE).toString();
		}catch (JSONException je)
		{
			title = "no title";
			subTitle = "no subtitle";
		}
	}


	public String getTitle() {
		return title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	@Override
	public boolean isSection() {
		return false;
	}

}
