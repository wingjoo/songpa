package com.example.wing.workingsongpa.CourseList;


import android.graphics.Bitmap;

import com.example.wing.workingsongpa.ApplicationClass;
import com.example.wing.workingsongpa.Database.DataCenter;

import org.json.JSONException;
import org.json.JSONObject;

public class EntryItem implements ApplicationClass.Item {

//	public final Drawable iconDrawable;
	public Bitmap imgScr;
	public JSONObject itemData;

	private String title;
	private String subTitle;
	private String msg;


//	public EntryItem(Drawable icon, JSONObject newData) {
//
//		iconDrawable = icon;
//		itemData = newData;
//		try{
//			this.title =  itemData.getString(DataCenter.TITLE_KEY).toString();
//			this.subTitle = itemData.getString(DataCenter.SUB_TITLE_KEY).toString();
//		}catch (JSONException je)
//		{
//			title = "no title";
//			subTitle = "no subtitle";
//		}
//	}

	public EntryItem(){}

	public EntryItem(Bitmap scr, JSONObject newData) {

		imgScr = scr;
		itemData = newData;
		try{
			this.title =  itemData.getString(DataCenter.TITLE_KEY).toString();
			this.subTitle = itemData.getString(DataCenter.SUB_TITLE_KEY).toString();
			this.msg = itemData.getString(DataCenter.SPOT_MSG).toString();
		}catch (JSONException je)
		{
			title = "no title";
			subTitle = "no subtitle";
		}
	}

	public void setData(Bitmap scr, JSONObject newData)
	{
		imgScr = scr;
		itemData = newData;
		try{
			this.title =  itemData.getString(DataCenter.TITLE_KEY).toString();
			this.subTitle = itemData.getString(DataCenter.SUB_TITLE_KEY).toString();
			this.msg = itemData.getString(DataCenter.SPOT_MSG).toString();
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

	public String getMsg() {return  msg;}

	@Override
	public boolean isSection() {
		return false;
	}

}
