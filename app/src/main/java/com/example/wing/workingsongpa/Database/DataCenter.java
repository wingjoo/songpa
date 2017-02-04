package com.example.wing.workingsongpa.Database;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

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
                    “walking_time”:float
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
            InputStream is = myContext.getAssets().open("course_list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String  json = new String(buffer, "UTF-8");

            courseList = new JSONArray(json);
        } catch (IOException ex) {
            ex.printStackTrace();
        }catch(JSONException je) {
            Log.e("jsonErr", "json에러입니당~", je);
        }
    }

    public void loadSpotListJSON(Context myContext) {
        try {
            InputStream is = myContext.getAssets().open("position_list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String  json = new String(buffer, "UTF-8");

            spotList = new JSONArray(json);
        } catch (IOException ex) {
            ex.printStackTrace();
        }catch(JSONException je) {
            Log.e("jsonErr", "json에러입니당~", je);
        }
    }

    public JSONArray getCourseList()
    {
        return courseList;
    }

    public JSONArray getSpotList()
    {
        return spotList;
    }

    public  JSONArray getCourseItems()
    {

        return null;
    }


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
