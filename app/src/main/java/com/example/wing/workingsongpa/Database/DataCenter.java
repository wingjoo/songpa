package com.example.wing.workingsongpa.Database;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.GLES20;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.example.wing.workingsongpa.ApplicationClass;
import com.example.wing.workingsongpa.CourseList.EntryItem;
import com.example.wing.workingsongpa.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


import static com.example.wing.workingsongpa.Database.DataCenter.CourseType.COURSE_TYPE_NON;
import static com.example.wing.workingsongpa.Database.DataCenter.CourseType.COURSE_TYPE_ROAD1;
import static com.example.wing.workingsongpa.Database.DataCenter.CourseType.COURSE_TYPE_ROAD2;
import static com.example.wing.workingsongpa.Database.DataCenter.CourseType.COURSE_TYPE_ROAD3;
import static com.example.wing.workingsongpa.Database.DataCenter.CourseType.COURSE_TYPE_ROAD4;
import static com.example.wing.workingsongpa.Database.DataCenter.CourseType.COURSE_TYPE_ROAD5;
import static com.example.wing.workingsongpa.Database.DataCenter.CourseType.COURSE_TYPE_ROAD6;
import static com.example.wing.workingsongpa.Database.DataCenter.CourseType.COURSE_TYPE_ROAD7;
import static com.example.wing.workingsongpa.Database.DataCenter.CourseType.COURSE_TYPE_ROAD8;


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
                    "image_url":"t_road_n_08",
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
                    "sub_msg" :[{"title" : String,"text":String}]
                    “info”:string
                    “latitude”:float
                    “longitude”:float
                    “group_type:int
                     "default_img":string
                     "img_list":[string]
                }

            ]
               "start{
        "longitude" : 127.1073,
        "latitude" : 37.51363
      },
        */



public class DataCenter {
    private static final String COURSE_LIST_NAME = "courseList.json";
    private static final String SPOT_LIST_NAME = "spotList.json";
    private static final String PATH_LIST_NAME = "path.json";
    private static final String AREA_LIST_NAME = "areaList.json";

    public static final String ID = "id";
    public static final String TITLE_KEY = "title";
    public static final String TEXT_KEY = "text";
    public static final String SUB_TITLE_KEY = "sub_title";
    public static final String SPOT_MSG = "msg";
    public static final String SPOT_SUBMSG = "sub_msg";
    public static final String SPOT_NEXT_DIS = "next_spot_distan";
    public static final String SPOT_LATI = "latitude";
    public static final String SPOT_LONGI = "longitude";
    public static final String SPOT_GRUOP_ID = "group_type";
    public static final String SPOT_MAIN_IMG = "default_img";
    public static final String SPOT_IMG_LIST = "img_list";
    public static final String SPOT_INFO = "info";

    public static final String COURSE_COURSELIST = "course";
    public static final String COURSE_STORY = "story";
    public static final String COURSE_SPECIALTY = "specialty";
    public static final String COURSE_INFORUSE = "infor_use";
    public static final String COURSE_IMG_URL = "image_url";

    public static final String AREA_LINE_POSITION = "area_position";

    public enum CourseType
    {
        COURSE_TYPE_NON,
        COURSE_TYPE_ROAD1,
        COURSE_TYPE_ROAD2,
        COURSE_TYPE_ROAD3,
        COURSE_TYPE_ROAD4,
        COURSE_TYPE_ROAD5,
        COURSE_TYPE_ROAD6,
        COURSE_TYPE_ROAD7,
        COURSE_TYPE_ROAD8,
    }

   private volatile static DataCenter sharedInstance;

    private JSONArray spotList;
    private JSONArray courseList;
    private JSONArray areaList;
    private JSONObject pathList;

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
    public void loadDataWithContext(Context myContext)
    {
        loadCourseListJSON(myContext);
        loadPathJSON(myContext);
        loadSpotListJSON(myContext);
        loadAreaListJSON(myContext);
    }


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
    public void loadPathJSON(Context myContext) {
        try {
            InputStream is = myContext.getAssets().open(PATH_LIST_NAME);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String  json = new String(buffer, "UTF-8");

            pathList = new JSONObject(json);
        } catch (IOException ex) {
            ex.printStackTrace();
        }catch(JSONException je) {
            Log.e("jsonErr", "LoadPathJson에러입니당~", je);
        }
    }

    public void loadAreaListJSON(Context myContext) {
        try {
            InputStream is = myContext.getAssets().open(AREA_LIST_NAME);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String  json = new String(buffer, "UTF-8");

            areaList = new JSONArray(json);
        } catch (IOException ex) {
            ex.printStackTrace();
        }catch(JSONException je) {
            Log.e("jsonErr", "LoadAreaJson에러입니당~", je);
        }
    }



    public ArrayList<JSONObject> getCourseList()
    {
        ArrayList<JSONObject> list = new ArrayList<JSONObject>();
        for(int c = 0; c < courseList .length(); c++)
        {
            try
            {
                JSONObject courseObject = courseList.getJSONObject(c);
                list.add(courseObject);

            }catch (JSONException je)
            {
                Log.e("jsonErr", "getCourseList에러입니당~", je);
                break;
            }
        }
        return list;
    }

    public ArrayList<JSONObject> allCourseList()
    {
        //기본만 가져오기
        ArrayList<JSONObject> list = new ArrayList<JSONObject>();
        for(int c = 0; c < courseList .length(); c++)
         {
             try
             {
                 JSONObject courseObject = courseList.getJSONObject(c);
                 list.add(courseObject);

             }catch (JSONException je)
             {
                 Log.e("jsonErr", "getCourseList에러입니당~", je);
                 break;
             }
         }

        return list;
    }


    public ArrayList<JSONObject> getAreaList() {

        ArrayList<JSONObject> list = new ArrayList<JSONObject>();
        for(int c = 0; c < areaList .length(); c++)
        {
            try
            {
                JSONObject areaObject = areaList.getJSONObject(c);
                list.add(areaObject);

            }catch (JSONException je)
            {
                Log.e("jsonErr", "getCourseList에러입니당~", je);
                break;
            }
        }

        return list;

    }


    //모든 스팟 가져오기
    public JSONArray getSpotList()
    {
        return spotList;
    }

    //해당 코스의 스팟정보 가져오기
    public  JSONObject getSpotItem(int position)
    {
        try
        {
            JSONObject spotObj = spotList.getJSONObject(position);

            return  spotObj;
        }catch (JSONException je) {
            Log.e("jsonErr", "getgetSpotItemJson에러입니당~", je);
        }


        return null;
    }


    //String[spot_id]
    public  ArrayList<JSONObject>  getAllPath(ArrayList<String> spot_list)
    {
        ArrayList<JSONObject> allPath = new ArrayList<JSONObject>();
        try
        {
            for (int c=0; c<spot_list.size()-1; c++) {
                String start_id = spot_list.get(c).toString();
                String end_id = spot_list.get(c+1).toString();
                //모든 스팟에서 해당 스팟의 데이터 가져오기
                JSONArray pathList = getBetweenPath(start_id,end_id);
                for (int i=0; i<pathList.length(); i++) {
                    JSONObject pathObj = pathList.getJSONObject(i);
                    allPath.add(pathObj);
                }
            }

            return  allPath;
        }catch (JSONException je) {
            Log.e("jsonErr", "getAvailablePath에러입니당~", je);
        }
        return null;
    }

    //    "96":{
//        "97":[
//        {
//            "longitude" : 127.0842,
//                "latitude" : 37.5108
//        },
//        ]
    //두 점사이의 path구하기
    private JSONArray getBetweenPath(String startKey, String endKey)
    {
        try
        {
            JSONArray path = pathList.getJSONObject(startKey).getJSONArray(endKey);
            return  path;
        }catch (JSONException je) {
            Log.e("jsonErr", "getPath에러입니당~", je);
        }
        return null;
    }


    public CourseType getCourseTypeWithID(int courseID)
    {
        CourseType type = COURSE_TYPE_NON;
        switch (courseID)
        {
            case 0:
                type = COURSE_TYPE_ROAD1;
                break;
            case 1:
                type = COURSE_TYPE_ROAD2;
                break;
            case 2:
                type = COURSE_TYPE_ROAD3;
                break;
            case 3:
                type = COURSE_TYPE_ROAD4;
                break;
            case 4:
                type = COURSE_TYPE_ROAD5;
                break;
            case 5:
                type = COURSE_TYPE_ROAD6;
                break;
            case 6:
                type = COURSE_TYPE_ROAD7;
                break;
            case 7:
                type = COURSE_TYPE_ROAD8;
                break;

        }
         return type;
    }


    public int getColorWithType(CourseType type)
    {
        int color = R.color.color_course1;
        switch (type)
        {
            case COURSE_TYPE_ROAD1:
                color = R.color.color_course1;
                break;
            case COURSE_TYPE_ROAD2:
                color = R.color.color_course2;
                break;
            case COURSE_TYPE_ROAD3:
                color = R.color.color_course3;
                break;
            case COURSE_TYPE_ROAD4:
                color = R.color.color_course4;
                break;
            case COURSE_TYPE_ROAD5:
                color = R.color.color_course5;
                break;
            case COURSE_TYPE_ROAD6:
                color = R.color.color_course6;
                break;
            case COURSE_TYPE_ROAD7:
                color = R.color.color_course7;
                break;
            case COURSE_TYPE_ROAD8:
                color = R.color.color_course8;
                break;

        }
        return color;
    }


    public String getHaxColorWithType(CourseType type)
    {
        String haxColor = "#fbb829";
        switch (type)
        {
            case COURSE_TYPE_ROAD1:
                haxColor = "#fbb829";
                break;
            case COURSE_TYPE_ROAD2:
                haxColor = "#7ab318";
                break;
            case COURSE_TYPE_ROAD3:
                haxColor = "#b90504";
                break;
            case COURSE_TYPE_ROAD4:
                haxColor = "#7e1a0b";
                break;
            case COURSE_TYPE_ROAD5:
                haxColor = "#e94e76";
                break;
            case COURSE_TYPE_ROAD6:
                haxColor = "#0c486c";
                break;
            case COURSE_TYPE_ROAD7:
                haxColor = "#fa6900";
                break;
            case COURSE_TYPE_ROAD8:
                haxColor = "#b62842";
                break;

        }
        return haxColor;
    }


    public int getColorDrawableNumWithType(CourseType type)
    {
        int color = 0xfbb829;
        switch (type)
        {
            case COURSE_TYPE_ROAD1:
                color  = 0xFFFBB829;
                break;
            case COURSE_TYPE_ROAD2:
                color  = 0xFF7AB318;
                break;
            case COURSE_TYPE_ROAD3:
                color  = 0xffb90504;
                break;
            case COURSE_TYPE_ROAD4:
                color  = 0xff7e1a0b;
                break;
            case COURSE_TYPE_ROAD5:
                color  = 0xffe94e76;
                break;
            case COURSE_TYPE_ROAD6:
                color  = 0xff0c486c;
                break;
            case COURSE_TYPE_ROAD7:
                color  = 0xfffa6900;
                break;
            case COURSE_TYPE_ROAD8:
                color  = 0xffb62842;
                break;

        }
        return color;
    }


    public Bitmap resizeImge(Resources rsc, int rscID, int reqWidth)
    {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 2;
//        Bitmap src = BitmapFactory.decodeResource(rsc,rscID,options);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(rsc,rscID, options);

        int imageWidth = options.outWidth;
        int imageheight = options.outWidth;

        if (imageWidth > reqWidth-10){
            int sampleSize = imageWidth/reqWidth;

            options.inSampleSize = sampleSize;
        }

        //리사이즈 다시 고민해봅시다
        Bitmap src = BitmapFactory.decodeResource(rsc,rscID);
        Bitmap resized = null;


        while (imageWidth > reqWidth) {
            resized = Bitmap.createScaledBitmap(src,reqWidth, (imageheight * reqWidth) / imageWidth, true);
            imageheight = resized.getHeight();
            imageWidth = resized.getWidth();
        }



        return resized;
    }

    public Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
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


//데이트 밤길
