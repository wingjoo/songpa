package com.example.wing.workingsongpa.MapTab;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wing.workingsongpa.ApplicationClass;
import com.example.wing.workingsongpa.CourseList.EntryAdapter;
import com.example.wing.workingsongpa.CourseList.EntryItem;
import com.example.wing.workingsongpa.CourseList.SectionItem;
import com.example.wing.workingsongpa.CourseList.SpotDetailActivity;
import com.example.wing.workingsongpa.Database.DataCenter;
import com.example.wing.workingsongpa.MainPage.MainActivity;
import com.example.wing.workingsongpa.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.maps.overlay.NMapPathData;
import com.nhn.android.maps.overlay.NMapPathLineStyle;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapPathDataOverlay;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapTapi;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.wing.workingsongpa.CourseList.DetailCourseListActivity.SPOT_DATA;

/**
 * Created by knightjym on 2017. 2. 23..
 */




public class MapActivity extends NMapActivity implements AppCompatCallback {

    public static final String MAP_ACTIVITY_INTENT= "map_intent";

    public enum CourseType { Recommand, Area, Custom}

    DrawMenuAdapter menuAdapter;
    private NMapContext mMapContext;
    private static final String CLIENT_ID = "z4imsbY8VsrmA4Z4DUws";// 애플리케이션 클라이언트 아이디 값
    private NMapView mapView;
    private NMapController mMapController;
    private NMapOverlayManager mOverlayManager;

    private NMapMyLocationOverlay mMyLocationOverlay;
    private NMapLocationManager mMapLocationManager;
    private NMapCompassManager mMapCompassManager;
    private  MapResourseProvider mMapViewerResourceProvider;

    private NGeoPoint beforPoint;
    private int beforZoomlevel;

    private NMapController mapController;
    private DataCenter.CourseType selectedCourseType;
    //overaly
///    private NMapPOIdataOverlay allPinOverlay;
    private NMapPOIdataOverlay courseOverlay;
    private NMapPOIdataOverlay groupPOIDataOverlay;
    private NMapPathDataOverlay pathOverlay;
    private NMapPathDataOverlay areaOverlay;

    private TextView titleView;


    private JSONArray allSpotList;
    private ImageButton trackingBtn;
    //데이트 코스일때 낮밤 전환용
    private ImageButton onoffDayBtn;
    private ListView bottomView;
    private EntryAdapter bottomListAdapter;

    private Boolean isTraking;

    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private ListView lvNavList;
    private AppCompatDelegate delegate;

    //화면에 보이는 코스 스팟리스트
    JSONObject selected_courseData;

    //******************데이트 코스 낮밤 구분*******************//

    private Boolean isDayON;
    private Boolean isMenuOpen;
    private CourseType coruseType;


    protected Boolean isEdittingMode;

    NMapPathDataOverlay customPathOverlay;

    //create Cusom Course
    //선택된 코스 리스트
    private ArrayList<JSONObject> createCustomCourseList;
    private ArrayList<NMapPOIitem> customCourseItems;
    private Integer selectedCustomCourselastIndex;
    private LinearLayout createCourseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_map_view);
        delegate = AppCompatDelegate.create(this, this);
        //we need to call the onCreate() of the AppCompatDelegate
        delegate.onCreate(savedInstanceState);
        //we use the delegate to inflate the layout
        delegate.setContentView(R.layout.activity_map_view);

        titleView = (TextView)findViewById(R.id.toolbar_title);

        TMapTapi tmaptapi = new TMapTapi(this);
        tmaptapi.setSKPMapAuthentication ("4ef5764d-39cf-315c-859a-665a118cd3f6");

        isEdittingMode = false;
        isDayON = true;
        // ****************************Intent DataSet*********************** //
        Intent intent = getIntent();
        String intentStr = intent.getStringExtra(MAP_ACTIVITY_INTENT);
        if (intentStr != null)
        {
            String[] extraList = intent.getStringExtra(MAP_ACTIVITY_INTENT).split("///");
            String flagStr =  extraList[0];
            String extraStr =  extraList[1];

            try
            {
                selected_courseData = new JSONObject(extraStr);

                int area_id = selected_courseData.getInt(DataCenter.ID);
//                //선택된 코스 타입
                selectedCourseType = DataCenter.getInstance().getCourseTypeWithID(area_id);
                if(selectedCourseType == DataCenter.CourseType.COURSE_TYPE_ROAD5)
                {
                    titleView.setText("석촌호수 데이트길-낮길");
                }else
                {
                    titleView.setText(selected_courseData.getString(DataCenter.TITLE_KEY));
                }


                if (flagStr.equals("area"))
                {
                    coruseType = CourseType.Area;
                    //isRecommandCourse = false;
                }else
                {
                    coruseType = CourseType.Recommand;
                    //isRecommandCourse = true;
                }

                isMenuOpen = false;

            }catch (JSONException e)
            {
                selectedCourseType = DataCenter.CourseType.COURSE_TYPE_NON;
                isMenuOpen = true;
            }

        }else
        {
            selectedCourseType = DataCenter.CourseType.COURSE_TYPE_NON;
            isMenuOpen = true;
        }


        // ****************************드로우 메뉴*********************** //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        delegate.setSupportActionBar(toolbar);
        delegate.getSupportActionBar().setDisplayShowTitleEnabled(false);
        delegate.getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_icon);
        delegate.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        delegate.getSupportActionBar().setHomeButtonEnabled(true);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawer.addDrawerListener(toggle);
        } else {
            drawer.setDrawerListener(toggle);
        }


        //***************트래킥 버튼 클릭****************//
        trackingBtn = (ImageButton)findViewById(R.id.traking_btn);
        trackingBtn .setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Do something with the value of the button
                if (mMapLocationManager.isMyLocationEnabled())
                {
                    stopMyLocation();

                }else
                {
                    startMyLocation();
                    isTraking = true;
                }
            }
        });
        //***************낮밤 버튼****************//
        onoffDayBtn = (ImageButton)findViewById(R.id.OnOffDay);

        onoffDayBtn .setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Do something with the value of the button
                if (selectedCourseType == DataCenter.CourseType.COURSE_TYPE_ROAD5)
                {
                    //데이트 코스일때 on/off기능
                    Drawable drawable;
                    if (isDayON)
                    {
                        titleView.setText("석촌호수 데이트길-밤길");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            drawable = getResources().getDrawable(R.drawable.map_moon, getTheme());
                            //bg_speech
                        }else
                        {
                            drawable = getResources().getDrawable(R.drawable.map_moon);
                        }

                        isDayON = false;

                    }else
                    {
                        titleView.setText("석촌호수 데이트길-낮길");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            drawable = getResources().getDrawable(R.drawable.map_sun, getTheme());
                        }else
                        {
                            drawable = getResources().getDrawable(R.drawable.map_sun);
                        }

                        isDayON = true;
                    }
                    onoffDayBtn.setBackground(drawable);
                    showRecommandCourse(selected_courseData);
                }
            }
        });
        onoffDayBtn.setVisibility(View.GONE);


        //***************커스텀 코스 생성****************//
        ImageButton addMapBtn = (ImageButton) findViewById(R.id.add_map);
        addMapBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                //모든 구역 보여주기
                if (!isEdittingMode)
                {
                    titleView.setText("구역을 선택해 주세요");
                    bottomView.setVisibility(View.GONE);

                    isEdittingMode = true;
                    selectedCustomCourselastIndex = 0;
                    if (createCustomCourseList == null)
                    {
                        createCustomCourseList = new ArrayList<JSONObject>();
                        customCourseItems = new ArrayList<NMapPOIitem>();
                    }
                    createCustomCourseList.clear();
                    customCourseItems.clear();

                    choiceAllArea();
                }
            }
        });

        //데이터 초기화
        initData();
        //menu만들기
        createMenu();
        //위치정보 확인 요청
        requestTrackingPermission();

        beforPoint = new NGeoPoint(127.11227,37.49735);
        beforZoomlevel = 10;
        isEdittingMode =false;
        //맵 초기화
        initMapView();

        isDayON = true;
        setBottomView();
        setCreateControlView();
    }

    //menu 만들기
    private void createMenu()
    {
        new CreateMenu().execute();
    }
    private class CreateMenu extends AsyncTask<Void, Void, ArrayList<ApplicationClass.Item>> {

        //시작전
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //백그라운드 실행내용
        @Override
        protected ArrayList<ApplicationClass.Item> doInBackground(Void... voids) {
            ArrayList<ApplicationClass.Item> menuItem = new ArrayList<ApplicationClass.Item>();
            //스팟
            menuItem.add(new SectionItem("recommd List"));
            /***********************Draw Menu 코스 보여주기***********************/
            ///////////////////기본 코스///////////////////
            ArrayList<JSONObject> courseData = DataCenter.getInstance().getCourseList(getApplicationContext());
            for (JSONObject data: courseData) {
                menuItem.add(new DrawMenuItem(data, 1));
            }
            /////////////////////구간 정보///////////////////
            menuItem.add(new SectionItem("구간"));
            ArrayList<JSONObject> areaData = DataCenter.getInstance().getAreaList(getApplicationContext());
            for (JSONObject data: areaData) {
                menuItem.add(new DrawMenuItem(data, 2));
            }

            menuItem.add(new SectionItem("사용자 코스"));
            //addBtn
            menuItem.add(new AddCustomCourseItem());
            /////////////////////Custom Course///////////////////
            ArrayList<JSONObject> custom = DataCenter.getInstance().getCustomList(getApplicationContext());
            if (custom.size() > 0)
            {
                for (JSONObject data: custom) {
                    menuItem.add(new DrawMenuItem(data, 3));
                }
                //add버튼 만들기
                //menuItem.add(new DrawMenuItem(ADD, 3));
            }
            return menuItem;
        }

        //프로그래스바 필요시
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        //실행 후

        @Override
        protected void onPostExecute(ArrayList<ApplicationClass.Item> items) {
            super.onPostExecute(items);
            menuAdapter = new DrawMenuAdapter(getApplicationContext(), items);
            lvNavList = (ListView)findViewById(R.id.nav_menu);
            lvNavList.setAdapter(menuAdapter);
            lvNavList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    //add버튼 처리
                    if (position == 16)
                    {
                        if (!isEdittingMode)
                        {
                            titleView.setText("구역을 선택해 주세요");
                            bottomView.setVisibility(View.GONE);

                            isEdittingMode = true;
                            selectedCustomCourselastIndex = 0;
                            if (createCustomCourseList == null)
                            {
                                createCustomCourseList = new ArrayList<JSONObject>();
                                customCourseItems = new ArrayList<NMapPOIitem>();
                            }
                            createCustomCourseList.clear();
                            customCourseItems.clear();

                            choiceAllArea();
                        }
                    }else
                    {
                        //drawMenu Item
                        if (isEdittingMode)
                        {
                            //에딧팅 취소
                            createCourseView.setVisibility(View.GONE);
                            isEdittingMode = false;
                        }

                        DrawMenuItem item = (DrawMenuItem)parent.getItemAtPosition(position) ;
                        //아이템이 코스인지 구역인지 구분 필요
                        //item.itemData
                        JSONObject selecteData = item.itemData;
                        try
                        {
                            int area_id = selecteData.getInt(DataCenter.ID);
                            //선택된 코스 타입
                            selectedCourseType = DataCenter.getInstance().getCourseTypeWithID(area_id);
                            if (selectedCourseType == DataCenter.CourseType.COURSE_TYPE_ROAD5)
                            {
                                if (isDayON)
                                {
                                    titleView.setText("석촌호수 데이트길-낮길");
                                }else
                                {
                                    titleView.setText("석촌호수 데이트길-밤길");
                                }

                            }else
                            {
                                titleView.setText(selecteData.getString(DataCenter.TITLE_KEY));
                            }


                            if (item.getSection() == 1)
                            {
                                coruseType = CourseType.Recommand;
                                showRecommandCourse(selecteData);
                            }else if (item.getSection() == 2)
                            {
                                coruseType = CourseType.Area;
                                showArea(selecteData);
                            }else if (item.getSection() == 3)
                            {
                                //커스텀 클릭시
                                coruseType = CourseType.Custom;
                                showCustomCourse(selecteData);
                            }

                        }catch (JSONException e)
                        {

                        }
                    }

                    //닫기
                    drawer.closeDrawer(lvNavList);
                }
            });

            if (isMenuOpen)
            {
                drawer.openDrawer(lvNavList);
            }else{
                drawer.closeDrawer(lvNavList);
            }
        }
    }

    private void addMenu(JSONObject newData)
    {
        menuAdapter.add(new DrawMenuItem(newData, 3));
        menuAdapter.notifyDataSetChanged();
    }

    private void initData()
    {
        //모듬 스팟 데이터
        new Thread(new Runnable() {
            public void run() {
                allSpotList = DataCenter.getInstance().getSpotList();
            }
        }).start();
    }

    private void initMapView()
    {
        // ****************************Map setting*********************** //
        mMapContext =  new NMapContext(this);
        mMapContext.onCreate();
        isTraking = false;

        mapView = (NMapView)findViewById(R.id.mapView);
        // initialize map view
        mapView.setClientId(CLIENT_ID);// 클라이언트 아이디 설정
        mapView.setClickable(true);
        mapView.setEnabled(true);

        mapView.setFocusable(true);
        mapView.setFocusableInTouchMode(true);
        mapView.requestFocus();

        mMapContext.setupMapView(mapView);

        /////지도 설정////////////
        mapController = mapView.getMapController();
        mapController.setZoomEnabled(true);
        //지도 중심 좌표 및 축척 레벨을 설정한다. 축척 레벨을 지정하지 않으면 중심 좌표만 변경된다. 유효 축척 레벨 범위는 1~14이다
        // register listener for map state changes
        mapView.setOnMapStateChangeListener(onMapViewStateChangeListener);

        //map 컨트롤러
        // use map controller to zoom in/out, pan and set map center, zoom level etc.
        mMapController = mapView.getMapController();
        mMapController.setMapCenter(beforPoint,beforZoomlevel);

        // create resource provider
        mMapViewerResourceProvider = new MapResourseProvider(MapActivity.this);
        // create overlay manager
        //매니져 만들고 프로바이더 제공(프로바이더는 리소스 제공객체)
        mOverlayManager = new NMapOverlayManager(MapActivity.this, mapView, mMapViewerResourceProvider);
        mMapLocationManager = new NMapLocationManager(MapActivity.this);
        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);
//        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);
        mMapCompassManager = new NMapCompassManager(MapActivity.this);
        mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);

        //초기 코스 표시하기
        if (selectedCourseType != DataCenter.CourseType.COURSE_TYPE_NON)
        {
            if (coruseType == CourseType.Recommand)
            {
                showRecommandCourse(selected_courseData);
            }else
            {
                showArea(selected_courseData);
            }
        }

    }


    private void requestTrackingPermission()
    {
        /***********************trackingBtn***********************************/
        //위치 정보 확인
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
//                Toast.makeText(MapActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//                Toast.makeText(MapActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        //////인증
        new TedPermission(MapActivity.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }


    private void setBottomView()
    {
        ArrayList<ApplicationClass.Item> bottomItem = new ArrayList<ApplicationClass.Item>();
        bottomView = (ListView)findViewById(R.id.detail_course_listView);
        bottomItem.add(new EntryItem());
        bottomListAdapter = new EntryAdapter(this, bottomItem);
        bottomView.setAdapter(bottomListAdapter);

        //스팟 선택시 행동
        //디테일 화면으로 이동
        bottomView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                EntryItem item = (EntryItem)parent.getItemAtPosition(position);

                //next activity
                Intent intent = new Intent(MapActivity.this , SpotDetailActivity.class);

                String sendStr = item.itemData.toString();
                //intent를 통해서 json객체 전송(string으로 변환
                intent.putExtra(SPOT_DATA, sendStr);

                startActivity(intent);
            }
        });


        bottomView.setVisibility(View.GONE);
    }


    private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {

        @Override
        public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {

            if (mMapController != null) {
                if (isTraking)
                {
                    //
                    mMapController.animateTo(myLocation);
                    mMapController.setMapCenter(myLocation,14);
                    //이미지 변경
                    Resources resources =  getResources();
                    int resID  = getResources().getIdentifier("map_tracking_p", "drawable", "com.example.wing.workingsongpa");
//                    Bitmap bScr = BitmapFactory.decodeResource(resources,resID);

                    Drawable drawable;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        drawable = getResources().getDrawable(R.drawable.map_tracking_p, getTheme());
                        //bg_speech
                    }else
                    {
                        drawable = getResources().getDrawable(R.drawable.map_tracking_p);
                    }
                    trackingBtn.setBackground(drawable);


                    isTraking = false;
                }
            }
            return true;
        }

        @Override
        public void onLocationUpdateTimeout(NMapLocationManager locationManager) {


            Toast.makeText(MapActivity.this, "Your current location is temporarily unavailable.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {

            Toast.makeText(MapActivity.this, "Your current location is unavailable area.", Toast.LENGTH_LONG).show();
            stopMyLocation();
        }

    };

    //Location 시작
    private void startMyLocation() {
//        && !mapView.isAutoRotateEnabled()
        if (mMyLocationOverlay != null ) {

            //locationOverlay가 있는지 확인후 없으면 추가
            if (!mOverlayManager.hasOverlay(mMyLocationOverlay)) {
                mOverlayManager.addOverlay(mMyLocationOverlay);
            }

            //로케이션중이 아닐때
            boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);
            if (!isMyLocationEnabled) {
                //셋팅이 안되어 있을경우

                Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(goToSettings);

                return;
            }

            //위치 찾기
//            mMyLocationOverlay.setCompassHeadingVisible(true);
            mMapCompassManager.enableCompass();
//            mapView.setAutoRotateEnabled(false, false);

            mapView.postInvalidate();
        }
    }

    //Location 종료
    private void stopMyLocation() {
        if (mMyLocationOverlay != null && mMapLocationManager.isMyLocationEnabled()) {

            Drawable drawable;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = getResources().getDrawable(R.drawable.map_tracking_n, getTheme());
                //bg_speech
            }else
            {
                drawable = getResources().getDrawable(R.drawable.map_tracking_n);
            }
            trackingBtn.setBackground(drawable);

            mMapLocationManager.disableMyLocation();
            mMapCompassManager.disableCompass();
            mMapController.setMapCenter(beforPoint,beforZoomlevel);

            mOverlayManager.removeOverlay(mMyLocationOverlay);
        }
    }

    private void allClear()
    {
        if (courseOverlay != null)
        {
            mOverlayManager.removeOverlay(courseOverlay);
        }
        if (pathOverlay != null)
        {
            mOverlayManager.removeOverlay(pathOverlay);
        }
        if (areaOverlay != null)
        {
            mOverlayManager.removeOverlay(areaOverlay);
        }

        if (groupPOIDataOverlay != null)
        {
            mOverlayManager.removeOverlay(groupPOIDataOverlay);
        }

        if (customPathOverlay != null)
        {
            mOverlayManager.removeOverlay(customPathOverlay);
        }

        onoffDayBtn.setVisibility(View.GONE);
    }


    public void choiceAllArea() {
        allClear();
//        new ShowAllArea().execute(mapView.getContext());

        NMapPathDataOverlay pathDataOverlay = mOverlayManager.createPathDataOverlay();

        ArrayList<JSONObject> areaList = DataCenter.getInstance().getAreaList(getApplicationContext());
        NMapPOIdata poiData = new NMapPOIdata(areaList.size(), mMapViewerResourceProvider);
        try {
            //영역 네이밍 data
            poiData.beginPOIdata(areaList.size());

            //모든 영역 그리기
            for (JSONObject areaData : areaList) {
                int areaID = areaData.getInt(DataCenter.ID);

                JSONArray area_potion = areaData.getJSONArray(DataCenter.AREA_LINE_POSITION);

                NMapPathData pathData = new NMapPathData(area_potion.length());
                pathData.initPathData();
                for (int i = 0; i < area_potion.length(); i++) {
                    JSONObject position = area_potion.getJSONObject(i);
                    double longi = position.getDouble(DataCenter.SPOT_LONGI);
                    double lati = position.getDouble(DataCenter.SPOT_LATI);
                    pathData.addPathPoint(longi, lati, 0);
                }
                pathData.endPathData();

                int color = Color.parseColor(DataCenter.getInstance().getHaxColorWithType(DataCenter.getInstance().getCourseTypeWithID(areaID),false));
                // set path line style
                NMapPathLineStyle pathLineStyle = new NMapPathLineStyle(mapView.getContext());
                pathLineStyle.setPataDataType(NMapPathLineStyle.DATA_TYPE_POLYGON);
                pathLineStyle.setLineColor(color, 0xff);
                pathLineStyle.setFillColor(color, 0x33);
                pathLineStyle.setLineWidth(1);
                pathData.setPathLineStyle(pathLineStyle);
                pathDataOverlay.addPathData(pathData);

                //////////////거점 이름표시///////////////////
                JSONObject centerPosition = areaData.getJSONObject("center_position");

                //pin 이미지에 텍스트 넣어야됨
                //커스텀 핀을 사용 해야되나?
                double center_longi = centerPosition.getDouble(DataCenter.SPOT_LONGI);
                double center_lati = centerPosition.getDouble(DataCenter.SPOT_LATI);

                //poiData.addPOIitem(center_longi, center_lati, null, MapFlagType.NORMAL, areaData, areaID);

                Drawable markerDrawable = mMapViewerResourceProvider.getDrawableForGruopWithID(areaID);
                poiData.addPOIitem(center_longi, center_lati, null, markerDrawable, areaData);
            }

            NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
            //아이템 선택할때의 리스러
            poiDataOverlay.setOnStateChangeListener(onGroupPOIdataStateChangeListener);
            //pin overlay
            groupPOIDataOverlay = poiDataOverlay;
            pathDataOverlay.showAllPathData(10);
            areaOverlay = pathDataOverlay;

        } catch (JSONException e) {
            Log.e("jsonErr", "Show AreaSpot 에러입니당~", e);

        }
    }
        //코스 보여주기
    //courseListData = 풀 코스 데이터
    private JSONArray selectCourseData(JSONObject courseListData)
    {
        try {

            if (selectedCourseType == DataCenter.CourseType.COURSE_TYPE_ROAD5)
            {

                JSONObject dateCourseData = courseListData.getJSONObject(DataCenter.COURSE_COURSELIST);

                if (isDayON)
                {
                    return dateCourseData.getJSONArray("day");
//                    selectCourseList = dayCourseList;

                }else
                {
                    return dateCourseData.getJSONArray("night");
//                    selectCourseList = nightCourseList;
                }
            }else
            {
                return courseListData.getJSONArray(DataCenter.COURSE_COURSELIST);
                //path그리기
//                selectCourseList = courseListData.getJSONArray(DataCenter.COURSE_COURSELIST);
            }

        }catch (JSONException e)
        {
            Log.e("jsonErr", "Show CourseSpot 에러입니당~", e);
            return  null;
        }

    }

    private void showRecommandCourse(JSONObject selecteData)
    {
        selected_courseData = selecteData;
        //이전 Paht 지우기
        allClear();

        //spot를 구하는 용도로 사용
        ArrayList<JSONObject> spotDataList = new ArrayList<JSONObject>();
        //path를 구하는 용도로 사용
        ArrayList<String> spotIDList  = new ArrayList<String>();
        try
        {
            int area_id = selecteData.getInt(DataCenter.ID);
            //선택된 코스 타입
            selectedCourseType = DataCenter.getInstance().getCourseTypeWithID(area_id);
            JSONArray selected_course_list = selectCourseData(selecteData);

            if (selected_course_list != null && selected_course_list.length() > 0)
            {
                //course List를 확인해서 SPOT 데이터 리스트 만들기
                for (int i=0; i<selected_course_list.length(); i++) {
                    JSONArray spot_list = selected_course_list.getJSONObject(i).getJSONArray("spot") ;
                    for (int c=0; c<spot_list.length(); c++)
                    {
                        String spot_id = spot_list.getString(c).toString();
                        spotIDList.add(spot_id);
                        int spotIndex = Integer.parseInt(spot_id);
                        //모든 스팟에서 해당 스팟의 데이터 가져오기
                        JSONObject spotData = allSpotList.getJSONObject(spotIndex);
                        spotDataList.add(spotData);
                    }
                }


                //출구 정보 추가
                //로멘틱 길일때
                if (selectedCourseType == DataCenter.CourseType.COURSE_TYPE_ROAD5) {
                    //플로우 버튼 Show
                    onoffDayBtn.setVisibility(View.VISIBLE);
                    //in-out data 추가

                    JSONObject inData = selecteData.getJSONObject(DataCenter.COURSE_IN);
                    JSONObject outData = selecteData.getJSONObject(DataCenter.COURSE_OUT);
                    JSONObject selectInData;
                    JSONObject selectOutData;
                    if (isDayON)
                    {
                        selectInData = inData.getJSONObject("day");
                        selectOutData = outData.getJSONObject("day");
                    }else
                    {
                        selectInData = inData.getJSONObject("night");
                        selectOutData = outData.getJSONObject("night");
                    }

                    if (inData.length() > 0)
                    {

                        //첫번째 path
                        spotIDList.add(0,selectInData.getString(DataCenter.ID).toString());
                        //allPathList.add(0,selectInData);
                        spotDataList.add(0,selectInData);

                        //마지막 path
                        spotIDList.add(selectOutData.getString(DataCenter.ID).toString());
                        //allPathList.add(selectOutData);
                        spotDataList.add(selectOutData);
                    }

                }else {
                    onoffDayBtn.setVisibility(View.GONE);

                    JSONObject inData = selecteData.getJSONObject(DataCenter.COURSE_IN);
                    if (inData.length() > 0)
                    {
                        //첫번째 path
                        spotIDList.add(0,inData.getString(DataCenter.ID).toString());
                        //allPathList.add(0,inData);
                        spotDataList.add(0,inData);
                    }
                    JSONObject outData = selecteData.getJSONObject(DataCenter.COURSE_OUT);
                    //마지막 path
                    if (outData.length() > 0) {
                        spotIDList.add(inData.getString(DataCenter.ID).toString());
                        //allPathList.add(outData);
                        spotDataList.add(outData);
                    }
                }
                ArrayList<JSONObject> allPathList = DataCenter.getInstance().getAllPath(spotIDList);
                drawPathWithList(allPathList);
                //spot 표시
                drawSpotWithList(spotDataList);
            }else
            {
                Log.d("listError", "Show CourseSpot 빈 리스트 에러 입니다 ");
            }


        }catch (JSONException e)
        {
            Log.e("jsonErr", "Show CourseSpot 에러입니당~", e);
        }

    }


    //구간 스팟 보여주기
    //areaListData = [1,2,3,]
    public void showArea(JSONObject areaListData)
    {
        allClear();
        //Area안의 spotData : pin찍을 용도
        ArrayList<JSONObject> spotDataList = new ArrayList<JSONObject>();

        try {


            JSONArray spot_list = areaListData.getJSONArray(DataCenter.COURSE_COURSELIST);

            //SPOT 데이터 리스트 만들기
            for (int c=0; c<spot_list.length(); c++)
            {
                String spot_id = spot_list.getString(c).toString();

                int spotIndex = Integer.parseInt(spot_id);
                //모든 스팟에서 해당 스팟의 데이터 가져오기
                JSONObject spotData = allSpotList.getJSONObject(spotIndex);
                spotDataList.add(spotData);
            }

            JSONArray area_potion = areaListData.getJSONArray(DataCenter.AREA_LINE_POSITION);
            drawAreaWithList(area_potion);

            //spot 표시
            drawSpotWithList(spotDataList);

        }catch (JSONException e)
        {
            Log.e("jsonErr", "Show AreaSpot 에러입니당~", e);
        }
    }


    //구간 스팟 보여주기
    //areaListData = [1,2,3,]
    public void showCustomCourse(JSONObject customListData)
    {
        //이전 Paht 지우기
        allClear();

        //spot를 구하는 용도로 사용
        ArrayList<JSONObject> spotDataList = new ArrayList<JSONObject>();

        try
        {
            titleView.setText(customListData.getString(DataCenter.TITLE_KEY));
            JSONArray spotObj_list = customListData.getJSONArray(DataCenter.COURSE_COURSELIST);

            //new DrawCustomPathThread().execute(spotObj_list);
            for (int c = 0; c < spotObj_list.length() ; c++) {

                JSONObject spotData = spotObj_list.getJSONObject(c);
                spotDataList.add(spotData);

                if (c + 1 < spotObj_list.length() )
                {
                    try
                    {
                        //모든 스팟에서 해당 스팟의 데이터 가져오기
                        JSONObject start_obj = spotObj_list.getJSONObject(c);
                        JSONObject end_ojb = spotObj_list.getJSONObject(c + 1);

                        double sLati = start_obj.getDouble(DataCenter.SPOT_LATI);
                        double sLong = start_obj.getDouble(DataCenter.SPOT_LONGI);
                        double eLati= end_ojb.getDouble(DataCenter.SPOT_LATI);
                        double eLong = end_ojb.getDouble(DataCenter.SPOT_LONGI);

                        final TMapPoint sPoint = new TMapPoint(sLati, sLong);
                        final TMapPoint ePoint = new TMapPoint(eLati, eLong);

                        final TMapData tmapdata = new TMapData();

                        tmapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, sPoint, ePoint, new TMapData.FindPathDataListenerCallback() {
                            @Override
                            public void onFindPathData(TMapPolyLine polyLine) {
                                ArrayList<TMapPoint> tPoints = polyLine.getLinePoint();

                                final ArrayList<JSONObject> allPathList = new ArrayList<JSONObject>();

                                for (TMapPoint tpoint: tPoints) {
                                    double latitude =  tpoint.getLatitude();
                                    double longitude =  tpoint.getLongitude();

                                    JSONObject pointObj = new JSONObject();
                                    try
                                    {
                                        pointObj.put(DataCenter.SPOT_LATI,latitude);
                                        pointObj.put(DataCenter.SPOT_LONGI,longitude);
                                        allPathList.add(pointObj);

                                    }catch (JSONException ex)
                                    {

                                    }
                                }

                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        // 메시지 큐에 저장될 메시지의 내용
                                        appendDrawPathWithList(allPathList);
                                    }
                                });
                            }
                        });

                    }catch (JSONException je)
                    {

                    }
                }
            }
            //spot 표시
            drawSpotWithList(spotDataList);
        }catch (JSONException e)
        {
            Log.e("jsonErr", "Show CourseSpot 에러입니당~", e);
        }

    }


    /***********************  private Method *************************************/
    //스팟 그리기
    private void drawSpotWithList(ArrayList<JSONObject> list)
    {
        //custom일때 순서 표시

        int firstSpotNum = 0;
        NMapPOIdata poiData = new NMapPOIdata(list .size(), mMapViewerResourceProvider);
        poiData.beginPOIdata(list .size());
        try {
            for (int i = 0; i< list.size(); i++)
            {
                JSONObject spotData = (JSONObject)list.get(i);
                int id = spotData.getInt(DataCenter.ID);

                double longi = spotData.getDouble(DataCenter.SPOT_LONGI);
                double lati = spotData.getDouble(DataCenter.SPOT_LATI);
                //NMapPOIitem addPOIitem(NGeoPoint point, String title, int markerId, Object tag, int id)

                String title = spotData.getString(DataCenter.TITLE_KEY);

                if (isEdittingMode)
                {
                    //editting Mode
                    Drawable markerDrawable = mMapViewerResourceProvider.getDrawableForMarkerWithIndex(0);
                    poiData.addPOIitem(longi, lati, null, markerDrawable, spotData);
                }else
                {
                    if (coruseType == CourseType.Recommand)
                    {
                        int markID;
                        //첫번째가 아닌 firstnumber가 필요함
                        if(title.startsWith("in:"))
                        {
                            firstSpotNum = 1;
                            markID = MapFlagType.IN;
                            title = title.substring(3);
                        }else if (title.startsWith("out:"))
                        {
                            markID = MapFlagType.OUT;
                            title = title.substring(4);
                        }else
                        {
                            if (i == firstSpotNum)
                            {
                                markID = MapFlagType.START;
                            }else
                            {
                                markID = MapFlagType.COURSE;
                            }
                        }

                        poiData.addPOIitem(longi, lati, title, markID, spotData, id);

                    }else if(coruseType == CourseType.Area)
                    {
                        int markID = MapFlagType.COURSE;
                        poiData.addPOIitem(longi, lati, title, markID, spotData, id);
                    }else
                    {
                        //editting Mode
                        Drawable markerDrawable = mMapViewerResourceProvider.getDrawableForMarkerWithIndex(i+1);
                        poiData.addPOIitem(longi, lati, null, markerDrawable, spotData);

//                        item.setMarker(mMapViewerResourceProvider.getDrawableForMarkerWithIndex(0));
                    }

                }
            }

            // create POI data overlay
            NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
            //아이템 선택할때의 리스러
            poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
            // select an item

            //0이 아니면 해당 축척에서 센터로 이동됨
            poiDataOverlay.showAllPOIdata(0);
            courseOverlay = poiDataOverlay;
        }catch (JSONException e)
        {
            Log.e("jsonErr", "Show CourseSpot 에러입니당~", e);
        }

    }

    //경로 그리기
    //JSONObject{longitude, latitude}
    private  void drawPathWithList(ArrayList<JSONObject> list)
    {
        // set path data points
        NMapPathData pathData = new NMapPathData(list.size());

        pathData.initPathData();
        try{
            for (JSONObject position:list) {
                double longi =  position.getDouble(DataCenter.SPOT_LONGI);
                double lati =  position.getDouble(DataCenter.SPOT_LATI);
                pathData.addPathPoint(longi, lati, 0);
            }

            pathData.endPathData();


            int color = Color.parseColor(DataCenter.getInstance().getHaxColorWithType(selectedCourseType, isDayON));

            NMapPathDataOverlay pathDataOverlay = mOverlayManager.createPathDataOverlay(pathData);
            pathDataOverlay.setLineColor(color, 0x88);
            pathDataOverlay.setLineWidth(8);
            pathDataOverlay.showAllPathData(0);
            pathOverlay = pathDataOverlay;

        }catch (JSONException e)
        {
            Log.e("jsonErr", "draw Line 에러입니당~", e);
        }
    }

    private  void appendDrawPathWithList(ArrayList<JSONObject> list)
    {
        // set path data points
        NMapPathData pathData = new NMapPathData(list.size());

        pathData.initPathData();
        try{
            for (JSONObject position:list) {
                double longi =  position.getDouble(DataCenter.SPOT_LONGI);
                double lati =  position.getDouble(DataCenter.SPOT_LATI);
                pathData.addPathPoint(longi, lati, 0);
            }

            pathData.endPathData();

            int color = Color.parseColor(DataCenter.getInstance().getHaxColorWithType(selectedCourseType, false));

            if (customPathOverlay == null)
            {
                customPathOverlay = mOverlayManager.createPathDataOverlay(pathData);
                customPathOverlay.setLineColor(color, 0x88);
                customPathOverlay.setLineWidth(8);
            }else
            {
                customPathOverlay.addPathData(pathData);
            }

            customPathOverlay.showAllPathData(0);

        }catch (JSONException e)
        {
            Log.e("jsonErr", "draw Line 에러입니당~", e);
        }
    }


    private  void drawAreaWithList(JSONArray jsonList)
    {
        // set path data points
        NMapPathData areaData = new NMapPathData(jsonList.length());
        areaData.initPathData();

        try{
            for (int i = 0; i<jsonList.length(); i++)
            {
                JSONObject position = jsonList.getJSONObject(i);
                double longi =  position.getDouble(DataCenter.SPOT_LONGI);
                double lati =  position.getDouble(DataCenter.SPOT_LATI);
//                pathData.addPathPoint(longi, lati, NMapPathLineStyle.TYPE_SOLID);
                areaData.addPathPoint(longi, lati, 0);
            }
            areaData.endPathData();

            int color = Color.parseColor(DataCenter.getInstance().getHaxColorWithType(selectedCourseType,false));

            // set path line style
            NMapPathLineStyle pathLineStyle = new NMapPathLineStyle(mapView.getContext());
            pathLineStyle.setPataDataType(NMapPathLineStyle.DATA_TYPE_POLYGON);
            pathLineStyle.setLineColor(color, 0xff);
            pathLineStyle.setFillColor(color, 0x33);
            pathLineStyle.setLineWidth(1);
            areaData.setPathLineStyle(pathLineStyle);

            NMapPathDataOverlay pathDataOverlay = mOverlayManager.createPathDataOverlay(areaData);

            pathDataOverlay.showAllPathData(0);
            areaOverlay = pathDataOverlay;
        }catch (JSONException e)
        {
            Log.e("jsonErr", "draw Line 에러입니당~", e);
        }

    }

    /*************************커스텀 그룹 선택 리스너 *************************/
    private final NMapPOIdataOverlay.OnStateChangeListener onGroupPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {

        //클릭시
        @Override
        public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {

        }
        //선택시 행동
        @Override
        public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            if (item != null)
            {
                mOverlayManager.removeOverlay(groupPOIDataOverlay);

                JSONObject itemData = (JSONObject)item.getTag();
                try{
                    //두번째 선택
                    titleView.setText("원하는 장소를 선택 해주세요");
                    selectedCourseType = DataCenter.getInstance().getCourseTypeWithID(itemData.getInt(DataCenter.ID));

                }catch (JSONException ex)
                {
                    selectedCourseType = DataCenter.getInstance().getCourseTypeWithID(0);
                }

                //Show Course
                //하단 컨트롤 뷰 on
                createCourseView.setVisibility(View.VISIBLE);
                //전체 리스트 띄우기
                JSONObject selectedData = (JSONObject)item.getTag();
                showArea(selectedData);

            }
        }
    };


    //////////////////// 핀 선택시 호출
    private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {

        //클릭시
        @Override
        public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {

            // [[TEMP]] handle a click event of the callout
           // Toast.makeText(MapActivity.this, "onCalloutClick: " + item.getTitle(), Toast.LENGTH_LONG).show();
        }

        //포커스 변경시
        @Override
        public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            // 이전에 선택된 아이템이 선택 해제되면 poiItem으로 null이 전달된다.
            if (item != null) {
                if (isEdittingMode) {

                    if (item.isKeepSelected())
                    {
                        item.setKeepSelected(false);
                        selectedCustomCourselastIndex += 1;


                        item.setMarker(mMapViewerResourceProvider.getDrawableForMarkerWithIndex(selectedCustomCourselastIndex));
                        JSONObject selectedData = (JSONObject) item.getTag();
                        createCustomCourseList.add(selectedData);
                        customCourseItems.add(item);
                        JSONObject objectData = (JSONObject) item.getTag();
                        showBottomView(objectData);
                    }else
                    {
                        item.setKeepSelected(true);
                        selectedCustomCourselastIndex -= 1;

                        item.setMarker(mMapViewerResourceProvider.getDrawableForMarkerWithIndex(0));
                        JSONObject selectedData = (JSONObject) item.getTag();
                        createCustomCourseList.remove(selectedData);
                        customCourseItems.remove(item);
                        //레이블 재 정렬
                        for (int i = 0; i < customCourseItems.size(); i++)
                        {
                            NMapPOIitem oldItem = customCourseItems.get(i);
                            oldItem.setMarker(mMapViewerResourceProvider.getDrawableForMarkerWithIndex(i+1));
                        }
                        hideBottomView();

                    }



                }else
                {
                    JSONObject objectData = (JSONObject) item.getTag();
                    try{
                        String title = objectData.getString(DataCenter.SUB_TITLE_KEY).toString();
                        if (title.length() > 0)
                        {
                            showBottomView(objectData);
                        }
                    }catch (JSONException ex)
                    {

                    }
                }


            } else {
                if (isEdittingMode) {
                    //hideBottomView();
                } else {
                    hideBottomView();
                }
            }
        }
    };


    private void showBottomView(JSONObject objectData)
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        //JSONObject objectData = (JSONObject) item.getTag();
        Resources resources = getResources();
        /**********************************/
        String img_url = null;
        try {
            img_url = objectData.getString(DataCenter.SPOT_MAIN_IMG).toString();

        } catch (JSONException e) {

        }
        //디폴트 이미지 설정
        if (img_url == null || img_url.length() == 0) {
            img_url = "list_img";
        }

        //이미지 리소스 아이디
        int resID = getResources().getIdentifier(img_url, "drawable", "com.example.wing.workingsongpa");
        Bitmap bScr = DataCenter.getInstance().resizeImge(resources, resID, width / 4);

        bottomListAdapter.updateTopItemWithData(bScr, objectData);
        bottomListAdapter.notifyDataSetChanged();
        bottomView.setVisibility(View.VISIBLE);
    }

    private void hideBottomView()
    {
        bottomView.setVisibility(View.GONE);
    }


    /* MapView State Change Listener*/
    private final NMapView.OnMapStateChangeListener onMapViewStateChangeListener = new NMapView.OnMapStateChangeListener() {

        @Override
        public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {

            if (errorInfo == null) { // success
                // restore map view state such as map center position and zoom level.

            } else { // fail

            }
        }

        @Override
        public void onAnimationStateChange(NMapView mapView, int animType, int animState) {
            Log.i("StateChange", "onAnimationStateChange: animType=" + animType + ", animState=" + animState);
        }

        @Override
        public void onMapCenterChange(NMapView mapView, NGeoPoint center) {

//            Log.i("zoomlevel", "onMapCenterChange: center=" + String.valueOf(mapController.getZoomLevel()));
            if (!isTraking && mMapLocationManager != null && !mMapLocationManager.isMyLocationEnabled())
            {
                beforPoint = center;
                Log.i("onMapCenterChange", "onMapCenterChange: center=" + center.toString());
            }
        }

        @Override
        public void onZoomLevelChange(NMapView mapView, int level) {
            if (!isTraking && mMapLocationManager != null && !mMapLocationManager.isMyLocationEnabled())
            {
                beforZoomlevel = level;

            }
        }

        @Override
        public void onMapCenterChangeFine(NMapView mapView) {

        }

    };

    /*************************추천 코드 만들기 해당 코드*************************/
    private void setCreateControlView()
    {
        createCourseView = (LinearLayout)findViewById(R.id.create_control_view);

        TextView createBtn = (TextView)findViewById(R.id.create_btn);
        createBtn .setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //코스 생성!
                if (createCustomCourseList.size() > 0)
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MapActivity.this);
                    alert.setTitle("코스 이름 입력");
                    alert.setMessage("코스 이름을 입력해주세요");

                    final EditText inputTitleView = new EditText(MapActivity.this);
                    alert.setView(inputTitleView);

                    alert.setPositiveButton("생성하기", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //새로우 코스 저장하기
                            final String courseTitle = inputTitleView.getText().toString();

                            new Thread(new Runnable(){
                                public void run(){
                                    final JSONObject saveData = new JSONObject();
                                    try
                                    {

                                        isEdittingMode = false;
                                        JSONArray jsArray = new JSONArray(createCustomCourseList);
                                        saveData.put(DataCenter.ID,0);
                                        saveData.put(DataCenter.COURSE_COURSELIST, jsArray);
                                        saveData.put(DataCenter.TITLE_KEY, courseTitle);
                                        //데이터 저장
                                        DataCenter.getInstance().saveCustomData(getApplicationContext(), saveData);

                                        //저장 완료 후 액션
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                //munu update
                                                addMenu(saveData);
                                                createCourseView.setVisibility(View.GONE);
                                                //바로 선택된 화면으로 이동

                                                //int area_id = saveData.getInt(DataCenter.ID);
                                                //선택된 코스 타입
                                                selectedCourseType = DataCenter.getInstance().getCourseTypeWithID(0);
                                                coruseType = CourseType.Custom;
                                                //isRecommandCourse = false;
                                                showCustomCourse(saveData);
                                            }
                                        });


                                    }catch (JSONException ex)
                                    {
                                        //추가 실패
                                    }
                                }
                            }).start();

                        }
                    });
                    alert.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //알럿 닫기
                        }
                    });
                    alert.show();

                }else
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MapActivity.this);
                    alert.setTitle("확인!").setMessage("코스를 선택 해주세요").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //알럿 닫기
                        }
                    });
                    alert.create();
                    alert.show();

                }
            }
        });

        TextView cancelBtn = (TextView)findViewById(R.id.cancel_btn);
        cancelBtn .setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //취소 액션
                AlertDialog.Builder alert = new AlertDialog.Builder(MapActivity.this);
                alert.setTitle("뒤로가기")
                        .setMessage("이전단계로 이동하시겠습니까?")
                        .setPositiveButton("뒤로가기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //예 버튼 클릭시 행동
                                //선택된 코스 취소
                                createCustomCourseList.clear();
                                customCourseItems.clear();
                                selectedCustomCourselastIndex = 0;
                                //전체 코스 선택화면으로 이동
                                choiceAllArea();
                                createCourseView.setVisibility(View.GONE);
                            }
                        })
                        .setNeutralButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //알럿 닫기
                            }
                });
                alert.create();
                alert.show();

            }
        });
    }





    @Override
    public void onStart(){
        super.onStart();
        mMapContext.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapContext.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mMapContext.onPause();
    }
    @Override
    public void onStop() {
        mMapContext.onStop();
        stopMyLocation();
        super.onStop();
    }


    @Override
    public void onDestroy() {
        mMapContext.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onSupportActionModeStarted(ActionMode mode) {
        //let's leave this empty, for now
    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {
        // let's leave this empty, for now
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }


    //고딕체로 변경
    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }


}
