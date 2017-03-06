package com.example.wing.workingsongpa.MapTab;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wing.workingsongpa.ApplicationClass;
import com.example.wing.workingsongpa.CourseList.DetailCourseListActivity;
import com.example.wing.workingsongpa.CourseList.EntryAdapter;
import com.example.wing.workingsongpa.CourseList.EntryItem;
import com.example.wing.workingsongpa.CourseList.SectionItem;
import com.example.wing.workingsongpa.CourseList.SpotDetailActivity;
import com.example.wing.workingsongpa.Database.DataCenter;
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
    private NMapPOIdataOverlay allPinOverlay;
    private NMapPOIdataOverlay courseOverlay;
    private NMapPathDataOverlay pathOverlay;
    private NMapPathDataOverlay areaOverlay;

    private JSONArray allSpotList;
    private ImageButton trackingBtn;
    private ListView bottomView;
    private EntryAdapter bottomListAdapter;

    private Boolean isTraking;

    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private ListView lvNavList;
    private AppCompatDelegate delegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_map_view);

        // ****************************드로우 메뉴*********************** //

        delegate = AppCompatDelegate.create(this, this);
        //we need to call the onCreate() of the AppCompatDelegate
        delegate.onCreate(savedInstanceState);
        //we use the delegate to inflate the layout
        delegate.setContentView(R.layout.activity_map_view);

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
        // toggle.setDrawerIndicatorEnabled(false);
        ArrayList<ApplicationClass.Item> menuItem = new ArrayList<ApplicationClass.Item>();
        ArrayList<ApplicationClass.Item> bottomItem = new ArrayList<ApplicationClass.Item>();
        //[1,2,3,4,]
        //스팟
        menuItem.add(new SectionItem("recommd List"));
        /***********************Draw Menu 코스 보여주기***********************/
        //기본 코스
        ArrayList<JSONObject> courseData = DataCenter.getInstance().allCourseList();
        for (JSONObject data: courseData) {
            menuItem.add(new DrawMenuItem(data, 1));
        }

        //구간 정보
        menuItem.add(new SectionItem("구간"));
        ArrayList<JSONObject> areaData = DataCenter.getInstance().getAreaList();

        for (JSONObject data: areaData) {
            menuItem.add(new DrawMenuItem(data, 2));
        }

        //adaper
        DrawMenuAdapter adapter = new DrawMenuAdapter(this,menuItem);
        lvNavList = (ListView)findViewById(R.id.nav_menu);
        lvNavList.setAdapter(adapter);
        lvNavList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                //drawMenu Item
                DrawMenuItem item = (DrawMenuItem)parent.getItemAtPosition(position) ;
                //아이템이 코스인지 구역인지 구분 필요
                //item.itemData
                JSONObject data =  item.itemData;
                if (item.getSection() == 1)
                {
                    showCourse(data);
                }else if (item.getSection() == 2)
                {
                    showArea(data);
                }

                //닫기
                drawer.closeDrawer(lvNavList);
            }
        });


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

        //위치 정보 확인
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MapActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MapActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(MapActivity.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

        /*
        scalingFactor : 타일 이미지 확대 배율. 최솟값은 1배 (1.0f) 이며, 지도 타일 1픽셀이 화면의 1픽셀에 대응됨을 의미한다. 1배일 경우 최근의 고밀도 단말에서는 글자가 지나치게 작게 보이므로 적절한 배율을 지정해 시인성을 높일 수 있다. 고정값을 사용하기보다는 화면 밀도를 기준으로 적정한 값을 계산하여 전달하는 편이 바람직하다.
               mapHD : HD 타일 사용 여부. 고해상도 지도 타일을 사용하려면 true로 지정한다. 지도 타일이 더 선명해지지만 동일 영역을 표시하기 위한 데이터량이 대략 2배 정도 증가한다.
         */
        //mapView.setScalingFactor(1,true);

        //내장된 줌표시기 >> 출시 할때 활성화
//        mapView.setBuiltInZoomControls(false,null);
        mMapContext.setupMapView(mapView);

        /////지도 설정////////////
        mapController = mapView.getMapController();
        mapController.setZoomEnabled(true);
        //지도 중심 좌표 및 축척 레벨을 설정한다. 축척 레벨을 지정하지 않으면 중심 좌표만 변경된다. 유효 축척 레벨 범위는 1~14이다
        //NGeoPoint point, int level
        //NGeoPoint(double longitude, double latitude)

        allSpotList = DataCenter.getInstance().getSpotList();

        // register listener for map state changes
        mapView.setOnMapStateChangeListener(onMapViewStateChangeListener);

        //map 컨트롤러
        // use map controller to zoom in/out, pan and set map center, zoom level etc.
        mMapController = mapView.getMapController();
        beforPoint = new NGeoPoint(127.11227,37.49735);
        beforZoomlevel = 10;
        mMapController.setMapCenter(beforPoint,beforZoomlevel);

        // create resource provider
        mMapViewerResourceProvider = new MapResourseProvider(MapActivity.this);
        // create overlay manager
        //매니져 만들고 프로바이더 제공(프로바이더는 리소스 제공객체)
        mOverlayManager = new NMapOverlayManager(MapActivity.this, mapView, mMapViewerResourceProvider);


        /***********************trackingBtn***********************************/
        mMapLocationManager = new NMapLocationManager(MapActivity.this);
        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);
//        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);
        mMapCompassManager = new NMapCompassManager(MapActivity.this);
        mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);


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

        // ****************************진입시 행동*********************** //
        Intent intent = getIntent();
        String intentData = intent.getStringExtra(MAP_ACTIVITY_INTENT);

        if (intentData != null)
        {//menu선택하기
            try
            {
                JSONObject start_courseData = new JSONObject(intentData);
//        String extra_str= intent.getStringExtra(MAP_ACTIVITY_INTENT);
                showCourse(start_courseData);
            }catch (JSONException e)
            {
                drawer.openDrawer(lvNavList);
            }

        }else
        {//menu 띄우기
            drawer.openDrawer(lvNavList);
        }

        //****************Waring bottom overlay다시 만들기
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
                    Bitmap bScr = BitmapFactory.decodeResource(resources,resID);
                    trackingBtn.setImageBitmap(bScr);


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
                Toast.makeText(MapActivity.this, "Please enable a My Location source in system settings", Toast.LENGTH_LONG).show();

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

            Resources resources =  getResources();
            int resID  = getResources().getIdentifier("map_tracking_n", "drawable", "com.example.wing.workingsongpa");
            Bitmap bScr = BitmapFactory.decodeResource(resources,resID);
            trackingBtn.setImageBitmap(bScr);

            mMapLocationManager.disableMyLocation();

//            mMyLocationOverlay.setCompassHeadingVisible(false);
            mMapCompassManager.disableCompass();
//            mapView.setAutoRotateEnabled(false, false);
            mMapController.setMapCenter(beforPoint,beforZoomlevel);

            mOverlayManager.removeOverlay(mMyLocationOverlay);
        }
    }


    //모든 스팟 보여주기
    //NMapPOIdataOverlay로 관리 하는것 찾아보기
//    private void showALLSpot()
//    {
//        int markerId = MapFlagType.SPOT;
//
//        if (allPinOverlay == null)
//        {
//            NMapPOIdata poiData = new NMapPOIdata(allSpotList .length(), mMapViewerResourceProvider);
//            poiData.beginPOIdata(allSpotList .length());
//            //0번 index는 빈데이터
//            for(int i = 1; i < allSpotList .length(); i++) {
//                try {
//                    JSONObject spotData = allSpotList.getJSONObject(i);
//                    int id = spotData.getInt(DataCenter.ID);
//                    double longi = spotData.getDouble(DataCenter.SPOT_LONGI);
//                    double lati = spotData.getDouble(DataCenter.SPOT_LATI);
//                    //NMapPOIitem addPOIitem(NGeoPoint point, String title, int markerId, Object tag, int id)
//                    poiData.addPOIitem(longi,lati, null, markerId, 0, id);
//                }catch (JSONException je) {
//                    Log.e("jsonErr", "Show all spot 에러입니당~", je);
//                }
//            }
//            poiData.endPOIdata();
//
//            // create POI data overlay
//            allPinOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
//            //아이템 선택할때의 리스러
//            allPinOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
//            // select an item
////        poiDataOverlay.selectPOIitem(0, true);
//            allPinOverlay = allPinOverlay;
//        }
//
//        //전체 화면에 보이기
//        allPinOverlay.showAllPOIdata(0);
//    }


//    private void hideAllSopot()
//    {
//        mOverlayManager.removeOverlay(allPinOverlay);
//    }

    private void clearCourse()
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
    }


    //코스 보여주기
    //courseListData = 풀 코스 데이터
    public void showCourse(JSONObject courseListData)
    {
        clearCourse();
        //spot를 구하는 용도로 사용
        ArrayList<JSONObject> spotDataList = new ArrayList<JSONObject>();
        //path를 구하는 용도로 사용
        ArrayList<String> spotIDList  = new ArrayList<String>();

        try {
            int course_id = courseListData.getInt(DataCenter.ID);
            //선택된 코스 타입
            selectedCourseType = DataCenter.getInstance().getCourseTypeWithID(course_id);

            JSONArray courseList = courseListData.getJSONArray(DataCenter.COURSE_COURSELIST);

            //course List를 확인해서 SPOT 데이터 리스트 만들기
            for (int i=0; i<courseList.length(); i++) {
                JSONArray spot_list = courseList.getJSONObject(i).getJSONArray("spot") ;
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
            //path그리기
            ArrayList<JSONObject> allPathLIst = DataCenter.getInstance().getAllPath(spotIDList);
            drawPathWithList(allPathLIst);

            //spot 표시
            drawSpotWithList(spotDataList);


        }catch (JSONException e)
        {
            Log.e("jsonErr", "Show CourseSpot 에러입니당~", e);
        }

    }

    //구간 스팟 보여주기
    //
    public void showArea(JSONObject areaListData)
    {
        clearCourse();
        //Area안의 spotData : pin찍을 용도
        ArrayList<JSONObject> spotDataList = new ArrayList<JSONObject>();


        try {
            int area_id = areaListData.getInt(DataCenter.ID);
            //선택된 코스 타입
            selectedCourseType = DataCenter.getInstance().getCourseTypeWithID(area_id);

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

            //path그리기
//            ArrayList<JSONObject> allPathLIst = DataCenter.getInstance().getAllPath(spotIDList);
            JSONArray area_potion = areaListData.getJSONArray(DataCenter.AREA_LINE_POSITION);

            drawAreaWithList(area_potion);

            //spot 표시
//            drawSpotWithList(spotDataList);

        }catch (JSONException e)
        {
            Log.e("jsonErr", "Show AreaSpot 에러입니당~", e);
        }
    }




    /***********************  private Method *************************************/
    //스팟 그리기
    private void drawSpotWithList(ArrayList<JSONObject> list)
    {
        int markerId = MapFlagType.COURSE;
        NMapPOIdata poiData = new NMapPOIdata(list .size(), mMapViewerResourceProvider);
        poiData.beginPOIdata(list .size());
        try {
            for (JSONObject spotData: list) {
                int id = spotData.getInt(DataCenter.ID);
                double longi = spotData.getDouble(DataCenter.SPOT_LONGI);
                double lati = spotData.getDouble(DataCenter.SPOT_LATI);
                //NMapPOIitem addPOIitem(NGeoPoint point, String title, int markerId, Object tag, int id)
                poiData.addPOIitem(longi,lati, null, markerId, spotData, id);
            }
            // create POI data overlay
            NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
            //아이템 선택할때의 리스러
            poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
            // select an item
//        poiDataOverlay.selectPOIitem(0, true);
            courseOverlay = poiDataOverlay;
            //0이 아니면 해당 축척에서 센터로 이동됨
            poiDataOverlay.showAllPOIdata(0);

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
//                pathData.addPathPoint(longi, lati, NMapPathLineStyle.TYPE_SOLID);
                pathData.addPathPoint(longi, lati, 0);
            }

            pathData.endPathData();


            //rgb로 바꿔야 색상 바뀜color
            //잘못됬음
            int haxColor = DataCenter.getInstance().getColorWithType(selectedCourseType);

            int r = (haxColor & 0xFF0000) >> 16;
            int g = (haxColor & 0xFF00) >> 8;
            int b = (haxColor & 0xFF);

            int color = Color.parseColor(DataCenter.getInstance().getHaxColorWithType(selectedCourseType));

//            int color = Integer.parseInt(DataCenter.getInstance().getHaxColorWithType(selectedCourseType) , 16);
//            int r = (color >> 16) & 0xFF;
//            int g = (color >> 8) & 0xFF;
//            int b = (color >> 0) & 0xFF;

            NMapPathDataOverlay pathDataOverlay = mOverlayManager.createPathDataOverlay(pathData);
//            pathDataOverlay.setLineColor(Color.rgb(r,g,b), 0x88);
            pathDataOverlay.setLineColor(color, 0x88);
            pathDataOverlay.setLineWidth(9);
            pathDataOverlay.showAllPathData(0);
            pathOverlay = pathDataOverlay;
        }catch (JSONException e)
        {
            Log.e("jsonErr", "draw Line 에러입니당~", e);
        }

    }


    //////////////////// 핀 선택시 호출
    private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {

        //클릭시
        @Override
        public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {

            // [[TEMP]] handle a click event of the callout
            Toast.makeText(MapActivity.this, "onCalloutClick: " + item.getTitle(), Toast.LENGTH_LONG).show();
        }
        //포커스 변경시
        @Override
        public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            if (item != null)
            {

                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;


                JSONObject objectData = (JSONObject)item.getTag();
                Resources resources =  getResources();
                /**********************************/
                String img_url = null;
                try
                {
                    img_url = objectData.getString(DataCenter.SPOT_MAIN_IMG).toString();

                }catch (JSONException e)
                {

                }

                if (img_url == null || img_url.length() == 0)
                {
                    img_url = "list_img";
                }

                //이미지 리소스 아이디

                int resID  = getResources().getIdentifier(img_url  , "drawable", "com.example.wing.workingsongpa");
                Bitmap bScr = DataCenter.getInstance().resizeImge(resources,resID,width/4);


                bottomListAdapter.updateTopItemWithData(bScr,objectData);
                bottomListAdapter.notifyDataSetChanged();
                bottomView.setVisibility(View.VISIBLE);

//                Toast.makeText(getActivity(), "change: " + item.getTitle(), Toast.LENGTH_LONG).show();
            }else
            {
                bottomView.setVisibility(View.GONE);
            }

        }

    };

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


            //rgb로 바꿔야 색상 바뀜color
//            int color = (int)Long.parseLong(myColorString, 16);
//            int r = (color >> 16) & 0xFF;
//            int g = (color >> 8) & 0xFF;
//            int b = (color >> 0) & 0xFF;
            // set path line style
            NMapPathLineStyle pathLineStyle = new NMapPathLineStyle(mapView.getContext());
            pathLineStyle.setPataDataType(NMapPathLineStyle.DATA_TYPE_POLYGON);
            pathLineStyle.setLineColor(0xA04DD2, 0xff);
            pathLineStyle.setLineWidth(1);
            pathLineStyle.setFillColor(Color.rgb(128,255,128), 0x88);
            areaData.setPathLineStyle(pathLineStyle);

            NMapPathDataOverlay pathDataOverlay = mOverlayManager.createPathDataOverlay(areaData);
//            pathDataOverlay.setLineColor(Color.rgb(128,255,128), 0x88);
//            pathDataOverlay.setLineWidth(9);
            pathDataOverlay.showAllPathData(0);
            areaOverlay = pathDataOverlay;
        }catch (JSONException e)
        {
            Log.e("jsonErr", "draw Line 에러입니당~", e);
        }

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
