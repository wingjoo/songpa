package com.example.wing.workingsongpa.Database;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by knightjym on 2017. 2. 1..
 */

/*
            courceList
            [
                {
                    “id”:int
                    “title”:string
                    “sub_title:string
                    “story”:string
                    “specialty”:string
                    “infor_use”:string
                    “total_distan”:string
                    “walking_time”:string
                    “course":[
                        {
                            "course_info":"어쩌구저쩌구",
                            "spot":[1,2,34,10]
                        }
                    ]
                }
            ]

            spotList
            [
                {
                    “id”:int
                    “title”:string
                    “sub_title:string
                    “next_spot_distan”:string
                    “msg”:string
                    “info”:string
                    “latitude”:float
                    “longitude”:float
                    “group_type:int
                     "default_img":string
                     "img_list":[string]
                }

            ]
        */



public class DataCenter {
    public static final String COURSE_LIST_NAME = "courseList.json";
    public static final String SPOT_LIST_NAME = "spotList.json";

    public static final String SPOT_ID = "id";
    public static final String SPOT_TITLE = "title";
    public static final String SPOT_SUB_TITLE = "sub_title";
    public static final String SPOT_MSG = "msg";
    public static final String SPOT_NEXT_DIS = "next_spot_distan";
    public static final String SPOT_LATI = "latitude";
    public static final String SPOT_LONGI = "longitude";
    public static final String SPOT_GRUOP_ID = "group_type";
    public static final String SPOT_MAIN_IMG = "default_img";
    public static final String SPOT_IMG_LIST = "img_list";



   private volatile static DataCenter sharedInstance;

    private JSONArray spotList;
    private JSONArray courseList;

    public static DataCenter getInstance(){
        if (sharedInstance == null)
        {
            synchronized (DataCenter.class){
                if (sharedInstance == null)
                {
                    sharedInstance = new DataCenter();
                }
            }
        }
        return sharedInstance;
    }


    // ****************************데이터*********************** //
    public void loadCourseListJSON(Context myContext) {
        try {
            InputStream is = myContext.getAssets().open(COURSE_LIST_NAME);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String  json = new String(buffer, "UTF-8");

            courseList = new JSONArray(json);
        } catch (IOException ex) {
            ex.printStackTrace();
        }catch(JSONException je) {
            Log.e("jsonErr", "LoadCourseJson에러입니당~", je);
        }
    }

    public void loadSpotListJSON(Context myContext) {
        try {
            InputStream is = myContext.getAssets().open(SPOT_LIST_NAME);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String  json = new String(buffer, "UTF-8");

            spotList = new JSONArray(json);
        } catch (IOException ex) {
            ex.printStackTrace();
        }catch(JSONException je) {
            Log.e("jsonErr", "LoadSpotJson에러입니당~", je);
        }
    }

    public JSONArray getCourseList()
    {
        return courseList;
    }

    //모든 스팟 가져오기
    public JSONArray getSpotList()
    {
        return spotList;
    }

    //해당 코스의 스팟 가져오기
    public  JSONArray getCourseItems(JSONObject course_data)
    {
        try
        {
            JSONArray courses = course_data.getJSONArray("course");
            return  courses;
        }catch (JSONException je) {
            Log.e("jsonErr", "getCourseItemsJson에러입니당~", je);
        }


        return null;
    }


    //모든 스팟 가져오기

    //해당 코스의 스팟 가져오기

    //스팟 데이터 가져오기



    //JSONObject 얻어 오기
//        JSONObject jsonObject =  new JSONObject(json);

    //json value 값 얻기
//        String title = jsonObject.getString("title").toString();   //결과값 TEST
    //StringArray에 buttons 의 title 키의 value값을 담겠습니다.
//        String btnTitle [] = new String[jArr.length()];

//        for(int i = 0; i < jArr.length(); i++){
//            btnTitle [i] = jArr.getJSONObject(i).getString("title").toString();
//            //출력하여 결과 얻기
//            System.out.println("btnTitle[" + i + "]=" + btnTitle[i]);
//        }

}
