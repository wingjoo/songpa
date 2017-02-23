package com.example.wing.workingsongpa.MapTab;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wing.workingsongpa.ApplicationClass;
import com.example.wing.workingsongpa.Database.DataCenter;
import com.example.wing.workingsongpa.MainPage.MainActivity;
import com.example.wing.workingsongpa.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MapFlagment extends Fragment {
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
    NMapPOIdataOverlay allPinOverlay;
    NMapPOIdataOverlay courseOverlay;
    NMapPathDataOverlay pathOverlay;

    private JSONArray allSpotList;
    private ImageButton trackingBtn;
    private LinearLayout bottomView;
    private PinOverlayView overlayView;
    private Boolean isTraking;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapContext =  new NMapContext(super.getActivity());
        mMapContext.onCreate();


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getActivity(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }

        };

        new TedPermission(getActivity())
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

        isTraking = false;

        mapView = (NMapView)getView().findViewById(R.id.mapView);
        // initialize map view
        mapView.setClientId(CLIENT_ID);// 클라이언트 아이디 설정
        mapView.setClickable(true);
        mapView.setEnabled(true);

        mapView.setFocusable(true);
        mapView.setFocusableInTouchMode(true);
        mapView.requestFocus();

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
//        mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);
//        mMapView.setOnMapViewDelegate(onMapViewTouchDelegate);
//

        //컨트롤러
        // use map controller to zoom in/out, pan and set map center, zoom level etc.
        mMapController = mapView.getMapController();
        beforPoint = new NGeoPoint(127.11227,37.49735);
        beforZoomlevel = 10;
        mMapController.setMapCenter(beforPoint,beforZoomlevel);

        // use built in zoom controls
//        NMapView.LayoutParams lp = new NMapView.LayoutParams(NMapView.LayoutParams.WRAP_CONTENT,
//                NMapView.LayoutParams.WRAP_CONTENT, NMapView.LayoutParams.BOTTOM_RIGHT);
//        mapView.setBuiltInZoomControls(true, lp);

        // create resource provider
        mMapViewerResourceProvider = new MapResourseProvider(getActivity());
        // create overlay manager
        //매니져 만들고 프로바이더 제공(프로바이더는 리소스 제공객체)
        mOverlayManager = new NMapOverlayManager(getActivity(), mapView, mMapViewerResourceProvider);


        /***********************trackingBtn***********************************/
        mMapLocationManager = new NMapLocationManager(getActivity());
        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);
//        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);
        mMapCompassManager = new NMapCompassManager(getActivity());
        mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);


        //***************트래킥 버튼 클릭****************//
        trackingBtn = (ImageButton)getView().findViewById(R.id.traking_btn);
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
       //****************Waring bottom overlay다시 만들기
        bottomView = (LinearLayout) getView().findViewById(R.id.pinOverlayView);

        overlayView = new PinOverlayView(getContext());
        bottomView.addView(overlayView);
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


            Toast.makeText(getActivity(), "Your current location is temporarily unavailable.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {

            Toast.makeText(getActivity(), "Your current location is unavailable area.", Toast.LENGTH_LONG).show();
            stopMyLocation();
        }

    };


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
                Toast.makeText(getActivity(), "Please enable a My Location source in system settings", Toast.LENGTH_LONG).show();

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


    public void setSelectedCourseType(DataCenter.CourseType selectedCourseType) {
        this.selectedCourseType = selectedCourseType;
    }

    //모든 스팟 보여주기
    //NMapPOIdataOverlay로 관리 하는것 찾아보기
    private void showALLSpot()
    {
        int markerId = MapFlagType.SPOT;

        if (allPinOverlay == null)
        {
            NMapPOIdata poiData = new NMapPOIdata(allSpotList .length(), mMapViewerResourceProvider);
            poiData.beginPOIdata(allSpotList .length());
            //0번 index는 빈데이터
            for(int i = 1; i < allSpotList .length(); i++) {
                try {
                    JSONObject spotData = allSpotList.getJSONObject(i);
                    int id = spotData.getInt(DataCenter.SPOT_ID);
                    double longi = spotData.getDouble(DataCenter.SPOT_LONGI);
                    double lati = spotData.getDouble(DataCenter.SPOT_LATI);
                    //NMapPOIitem addPOIitem(NGeoPoint point, String title, int markerId, Object tag, int id)
                    poiData.addPOIitem(longi,lati, null, markerId, 0, id);
                }catch (JSONException je) {
                    Log.e("jsonErr", "Show all spot 에러입니당~", je);
                }
            }
            poiData.endPOIdata();

            // create POI data overlay
            allPinOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
            //아이템 선택할때의 리스러
            allPinOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
            // select an item
//        poiDataOverlay.selectPOIitem(0, true);
            allPinOverlay = allPinOverlay;
        }

        //전체 화면에 보이기
        allPinOverlay.showAllPOIdata(0);
    }


    private void hideAllSopot()
    {
        mOverlayManager.removeOverlay(allPinOverlay);
    }

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
    }


    //courseList
    //[{"course_info":String, "spot":List}]
    //코스 보여주기
    public void showCourse(JSONObject courseListData)
    {
        clearCourse();
        //spot를 구하는 용도로 사용
        ArrayList<JSONObject> spotDataList = new ArrayList<JSONObject>();
        //path를 구하는 용도로 사용
        ArrayList<String> spotIDList  = new ArrayList<String>();

        try {
            int coursse_id = courseListData.getInt(DataCenter.SPOT_ID);
            selectedCourseType = DataCenter.getInstance().getCourseTypeWithID(coursse_id);
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

    //스팟 그리기
    private void drawSpotWithList(ArrayList<JSONObject> list)
    {
        int markerId = MapFlagType.COURSE;
        NMapPOIdata poiData = new NMapPOIdata(list .size(), mMapViewerResourceProvider);
        poiData.beginPOIdata(list .size());
        try {
            for (JSONObject spotData: list) {
                int id = spotData.getInt(DataCenter.SPOT_ID);
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


//            NMapPathLineStyle pathLineStyle = new NMapPathLineStyle(mapView.getContext());
//            pathLineStyle.setPataDataType(NMapPathLineStyle.DATA_TYPE_POLYLINE);
//
////            pathLineStyle.setLineColor(DataCenter.getInstance().getColorWithType(selectedCourseType), 0x80);
//            //색상 변하지 않음
//            pathLineStyle.setLineStyle(NMapPathLineStyle.TYPE_SOLID);
//            pathLineStyle.setLineColor(R.color.color_course1, 0x88);
//            pathLineStyle.setLineWidth(9);
//            pathData.setPathLineStyle(pathLineStyle);
            pathData.endPathData();


            //rgb로 바꿔야 색상 바뀜color
//            int color = (int)Long.parseLong(myColorString, 16);
//            int r = (color >> 16) & 0xFF;
//            int g = (color >> 8) & 0xFF;
//            int b = (color >> 0) & 0xFF;


            NMapPathDataOverlay pathDataOverlay = mOverlayManager.createPathDataOverlay(pathData);
            pathDataOverlay.setLineColor(Color.rgb(128,255,128), 0x88);
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
            Toast.makeText(getActivity(), "onCalloutClick: " + item.getTitle(), Toast.LENGTH_LONG).show();
        }
    //포커스 변경시
        @Override
        public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            if (item != null)
            {
                JSONObject objectData = (JSONObject)item.getTag();
                overlayView.setData(objectData);
                bottomView.setVisibility(View.VISIBLE);

//                Toast.makeText(getActivity(), "change: " + item.getTitle(), Toast.LENGTH_LONG).show();
            }else
            {
                bottomView.setVisibility(View.GONE);
            }

        }

    };


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

            /*if (bottomView != null && bottomView.getVisibility() == View.VISIBLE)
            {
                mapView.clearFocus();
                bottomView.setVisibility(View.GONE);
            }*/

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


//    private final NMapOverlayManager.OnCalloutOverlayViewListener onCalloutOverlayViewListener = new NMapOverlayManager.OnCalloutOverlayViewListener() {
//
//        @Override
//        public View onCreateCalloutOverlayView(NMapOverlay itemOverlay, NMapOverlayItem overlayItem, Rect itemBounds) {
//
//            if (overlayItem != null) {
//                // [TEST] 말풍선 오버레이를 뷰로 설정함
//                String title = overlayItem.getTitle();
//
//                if (title != null ) {
//                    return new CustomOverlaView(getActivity(), itemOverlay, overlayItem, itemBounds);
//                }
//            }
//
//            // null을 반환하면 말풍선 오버레이를 표시하지 않음
//            return null;
//        }
//
//    };

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
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onDestroy() {
        mMapContext.onDestroy();
        super.onDestroy();
    }




}






//
//    private void testPathPOIdataOverlay() {
//
//        // set POI data
//        NMapPOIdata poiData = new NMapPOIdata(4, mMapViewerResourceProvider, true);
//        poiData.beginPOIdata(4);
//        poiData.addPOIitem(349652983, 149297368, "Pizza 124-456", NMapPOIflagType.FROM, null);
//        poiData.addPOIitem(349652966, 149296906, null, NMapPOIflagType.NUMBER_BASE + 1, null);
//        poiData.addPOIitem(349651062, 149296913, null, NMapPOIflagType.NUMBER_BASE + 999, null);
//        poiData.addPOIitem(349651376, 149297750, "Pizza 000-999", NMapPOIflagType.TO, null);
//        poiData.endPOIdata();
//
//        // create POI data overlay
//        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
//
//        // set event listener to the overlay
//        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
//
//    }

//액션
//private final NMapView.OnMapViewDelegate onMapViewTouchDelegate = new NMapView.OnMapViewDelegate() {
//
//    @Override
//    public boolean isLocationTracking() {
//        if (mMapLocationManager != null) {
//            if (mMapLocationManager.isMyLocationEnabled()) {
//                return mMapLocationManager.isMyLocationFixed();
//            }
//        }
//        return false;
//    }
//
//};

