package com.example.wing.workingsongpa.MapTab;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wing.workingsongpa.Database.DataCenter;
import com.example.wing.workingsongpa.R;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
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

    private JSONArray allSpotList;

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
        mapView = (NMapView)getView().findViewById(R.id.mapView);
        // initialize map view
        mapView.setClientId(CLIENT_ID);// 클라이언트 아이디 설정
        mapView.setClickable(true);
        mapView.setEnabled(true);
        mapView.setFocusable(true);
        mapView.setFocusableInTouchMode(true);
        mapView.requestFocus();

        mMapContext.setupMapView(mapView);

        allSpotList = DataCenter.getInstance().getSpotList();

         // register listener for map state changes
//        mMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
//        mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);
//        mMapView.setOnMapViewDelegate(onMapViewTouchDelegate);
//
        // use map controller to zoom in/out, pan and set map center, zoom level etc.
        mMapController = mapView.getMapController();

        // use built in zoom controls
        NMapView.LayoutParams lp = new NMapView.LayoutParams(NMapView.LayoutParams.WRAP_CONTENT,
                NMapView.LayoutParams.WRAP_CONTENT, NMapView.LayoutParams.BOTTOM_RIGHT);
        mapView.setBuiltInZoomControls(true, lp);

        // create resource provider
        mMapViewerResourceProvider = new MapResourseProvider(getActivity());

        // create overlay manager
        //매니져 만들고 프로바이더 제공(프로바이더는 리소스 제공객체)
        mOverlayManager = new NMapOverlayManager(getActivity(), mapView, mMapViewerResourceProvider);
        //핀 만들기
        showALLSpot();

        //register callout overlay listener to customize it.
        //mOverlayManager.setOnCalloutOverlayViewListener(onCalloutOverlayViewListener);

        //경로 만들기
       // testPathDataOverlay();

//        // location manager
//        mMapLocationManager = new NMapLocationManager(getActivity());
//        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);

//        // compass manager
//        mMapCompassManager = new NMapCompassManager(getActivity());

//        // create my location overlay
//        mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);

    }

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

    private void showALLSpot()
    {
        int markerId = MapFlagType.SPOT;

        // pin 추가
        NMapPOIdata poiData = new NMapPOIdata(allSpotList .length(), mMapViewerResourceProvider);
        poiData.beginPOIdata(allSpotList .length());
        for(int i = 0; i < allSpotList .length(); i++) {
            try {
                JSONObject spotData = allSpotList.getJSONObject(i);
                int id = spotData.getInt(DataCenter.SPOT_ID);
                double longi = spotData.getDouble(DataCenter.SPOT_LONGI);
                double lati = spotData.getDouble(DataCenter.SPOT_LATI);
                poiData.addPOIitem(longi,lati, null, markerId, 0, id);
            }catch (JSONException je) {
                Log.e("jsonErr", "Show all spot 에러입니당~", je);
            }
        }
        poiData.endPOIdata();


        //NMapPOIitem addPOIitem(NGeoPoint point, String title, int markerId, Object tag, int id)

        poiData.addPOIitem(127.10645, 37.511063, "석촌호수", markerId, 0);



//      아이템에 액세서리 추가 방법 샘플 코드
        //       NMapPOIitem item = poiData.addPOIitem(127.0630205, 37.5091300, "Pizza 777-111", markerId, 0);
//        item.setRightAccessory(true, MapFlagType.CLICKABLE_ARROW);

        // create POI data overlay
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);


        //아이템 선택할때의 리스러
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

        // select an item
//        poiDataOverlay.selectPOIitem(0, true);

        //전체 화면에 보이기
        poiDataOverlay.showAllPOIdata(0);
    }




    private void testPathDataOverlay() {

        // set path data points
        NMapPathData pathData = new NMapPathData(6);

        pathData.initPathData();
        pathData.addPathPoint(127.101561, 37.512587, NMapPathLineStyle.TYPE_SOLID);
        pathData.addPathPoint(127.100959, 37.512409, 0);
        pathData.addPathPoint(127.106651, 37.51548, 0);
        pathData.addPathPoint(127.108161, 37.511989, 0);
        pathData.addPathPoint(127.106651, 37.510818, 0);
        pathData.addPathPoint(127.10645, 37.511063, 0);
        pathData.endPathData();

//        // set path line style
        NMapPathLineStyle pathLineStyle = new NMapPathLineStyle(mapView.getContext());
        pathLineStyle.setPataDataType(NMapPathLineStyle.DATA_TYPE_POLYLINE);
        pathLineStyle.setLineColor(0xFF0024, 0x80);
        pathLineStyle.setLineWidth(10);
       // pathLineStyle.setFillColor(0xFFFFFF, 0x00);
        pathData.setPathLineStyle(pathLineStyle);

        NMapPathDataOverlay pathDataOverlay = mOverlayManager.createPathDataOverlay(pathData);

        // show all path data
//        pathDataOverlay.showAllPathData(0);

    }


//    핀 선택시 호출
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
                Toast.makeText(getActivity(), "change: " + item.getTitle(), Toast.LENGTH_LONG).show();
            }

        }
    };

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

//
//    private void startMyLocation() {
//
//        if (mMyLocationOverlay != null) {
//            if (!mOverlayManager.hasOverlay(mMyLocationOverlay)) {
//                mOverlayManager.addOverlay(mMyLocationOverlay);
//            }
//
//            if (mMapLocationManager.isMyLocationEnabled()) {
//
//                if (!mMapView.isAutoRotateEnabled()) {
//                    mMyLocationOverlay.setCompassHeadingVisible(true);
//
//                    mMapCompassManager.enableCompass();
//
//                    mMapView.setAutoRotateEnabled(true, false);
//
//                    mMapContainerView.requestLayout();
//                } else {
//                    stopMyLocation();
//                }
//
//                mMapView.postInvalidate();
//            } else {
//                boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);
//                if (!isMyLocationEnabled) {
//                    Toast.makeText(NMapViewer.this, "Please enable a My Location source in system settings",
//                            Toast.LENGTH_LONG).show();
//
//                    Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    startActivity(goToSettings);
//
//                    return;
//                }
//            }
//        }
//    }
//
//    private void stopMyLocation() {
//        if (mMyLocationOverlay != null) {
//            mMapLocationManager.disableMyLocation();
//
//            if (mMapView.isAutoRotateEnabled()) {
//                mMyLocationOverlay.setCompassHeadingVisible(false);
//
//                mMapCompassManager.disableCompass();
//
//                mMapView.setAutoRotateEnabled(false, false);
//
//                mMapContainerView.requestLayout();
//            }
//        }
//    }


