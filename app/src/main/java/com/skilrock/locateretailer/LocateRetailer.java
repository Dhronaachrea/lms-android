package com.skilrock.locateretailer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.skilrock.adapters.CityAdapter;
import com.skilrock.adapters.RetailerAdapter;
import com.skilrock.adapters.StateAdapter;
import com.skilrock.bean.CityModal;
import com.skilrock.bean.RetailerFilterBean;
import com.skilrock.bean.RetailerListModal;
import com.skilrock.bean.RetailerListModal.RetailerList;
import com.skilrock.bean.RetailerValueBean;
import com.skilrock.bean.StateModal;
import com.skilrock.config.FilterDismissListener;
import com.skilrock.customui.CustomCheckedTextView;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.customui.RetailerFilterDialog;
import com.skilrock.lms.communication.JSONParser;
import com.skilrock.utils.Utils;
import com.skilrock.lms.communication.GPSTracker;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.DataSource;
import com.skilrock.utils.GlobalVariables;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LocateRetailer extends DrawerBaseActivity implements FilterDismissListener, WebServicesListener {

    private String fakeJson;
    private String stateCode, cityCode;
    private List<String> types;
    private List<String> services;
    private List<String> typeslist;
    private List<String> serviceslist;

    private int servicesRes[] = new int[]{
            R.drawable.dg_sale, R.drawable.scratch_sale,
            R.drawable.sports_lot, R.drawable.mobile_cash,
            R.drawable.mobile_money, R.drawable.dg_win_claim,
            R.drawable.cancel, R.drawable.high_win
    };
    private int typesRes[] = new int[]{R.drawable.agent, R.drawable.back_office, R.drawable.retailer};


    /*private int typesRes[] = new int[]{R.drawable.agent, R.drawable.bo,
            R.drawable.head_qtr, R.drawable.retailer};
    private int servicesRes[] = new int[]{R.drawable.dg_sale, R.drawable.scratch_sale,
            R.drawable.sports_lot, R.drawable.cash_in, R.drawable.cash_in, R.drawable.dg_win_claim,
            R.drawable.high_win};*/

    private boolean servicesChecked[] = new boolean[]{false, false, false,
            false, false, false, false, false};
    private boolean typesChecked[] = new boolean[]{false, false, false,
            false, false, false, false, false};
    private LatLng localRouteBase;
    private boolean isMarkerClickable = false;
    public HashMap<LatLng, List<RetailerValueBean>> mapData;
    private GoogleMap googleMap;
    protected RetailerListModal modal, filteredModal, infoModel;
    private CameraPosition position;
    private LatLng latLng;
    private CustomTextView state;
    private CustomTextView city;
    private Dialog dialog;
    private View stateDialogView, cityDialogView;
    private ListView stateListView, cityListView;
    private StateModal stateModal;
    private CityModal cityModal;
    private RetailerListModal retailerListModal;
    private ArrayList<RetailerList> retailerListModals = new ArrayList<RetailerList>();
    private StateAdapter stateAdapter;
    private CityAdapter cityAdapter;
    private LatLng myCurrentLatLong;
    private RelativeLayout mapLay;
    private LinearLayout listLay;
    private LinearLayout tabLay;
    private LinearLayout llRetailerInfo;
    private ImageView currentLoc;
    private RelativeLayout infoView;
    private CustomTextView okay;
    private LatLng latLongs[] = new LatLng[]{
            new LatLng(28.624439, 77.135508), new LatLng(28.475763, 77.058604),
            new LatLng(28.635308, 77.22496), new LatLng(28.546185, 77.264409),
            new LatLng(28.483134, 77.081576), new LatLng(28.494061, 77.087822)};
    private GPSTracker gpsTracker;
    // private float[] results = new float[3];
    // private float[] minResult = new float[3];
    protected MarkerOptions marker;
    private Animation slideDown;
    private Animation slideUp;
    private ListView retailerListView;
    //private boolean isClickable;
    private JSONObject data;
    private Context context;
    public PolylineOptions polyLineOptions;
    public Polyline polyline;
    private boolean isRetailerAddressClickabale = false;
    private RelativeLayout stateLay, cityLay;
    private LinearLayout servicesView;
    private RelativeLayout locationDesclay;
    private CustomTextView name, add, contact, distance;
    private CustomCheckedTextView retList, retMap;
    private boolean isScratchRet;

    private SharedPreferences preferences;

    private SharedPreferences.Editor editor;
    private RetailerFilterDialog filterDialog;
    private ImageView retTypeImage;
    private int typeImages;

    public static final String LOAD_LIST = "load_list";

    //new features
    private CustomTextView typelist1, typelist2, typelist3, typelist4, typelist5;
    private CustomTextView servicelist1, servicelist2, servicelist3, servicelist4, servicelist5, servicelist6, servicelist7, servicelist8;
    private DownloadDialogBox downloadDialogBox;
    private boolean isDialogVisible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.locate_retailer);


        analytics = new Analytics();
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.LOCATE_RETAILER);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);


        isScratchRet = getIntent().getBooleanExtra("isScratchRet", false);

        setLRHeader();
        setDrawerItems();
        bindViewIds();

        fakeJson = getIntent().getStringExtra("RETAILER");
        if (fakeJson.equals("")) fakeJson = null;
        if (fakeJson != null) {
            filteredModal = new Gson().fromJson(fakeJson, RetailerListModal.class);
            services = filteredModal.getOrgServices();
            types = filteredModal.getOrgTypes();
        }

        headerNavigation.setImageResource(R.drawable.back);
        headerNavigation.setOnTouchListener(null);
        headerNavigation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        filter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (services != null && types != null) {
                    RetailerFilterBean bean = new RetailerFilterBean(types, services, typesChecked, servicesChecked, typesRes, servicesRes);
                    filterDialog = new RetailerFilterDialog(context, bean,
                            LocateRetailer.this);
                    filterDialog.show();
                } else
                    Utils.Toast(LocateRetailer.this, "Please select state and city.");
            }
        });
        infoIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (services != null && types != null)
                    showInfoAnim(infoView, true);
                else
                    Utils.Toast(LocateRetailer.this, "Please select state and city.");
            }
        });
        okay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoAnim(infoView, false);
            }
        });
        mapLay = (RelativeLayout) findViewById(R.id.mapLay);
        retailerListView = (ListView) findViewById(R.id.ret_list_view);

        retailerListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (isRetailerAddressClickabale) {
                    showLocInfoAnim(locationDesclay, false, null);
                    retailerVisibleIn(RetailerView.MAP);
                    LatLng latLng = new LatLng(Double
                            .parseDouble(retailerListModals.get(position)
                                    .getLatitude()), Double
                            .parseDouble(retailerListModals.get(position)
                                    .getLongitude()));
                    locateMap(latLng, 20);
                    RetailerList list = retailerListModals.get(position);
                    RetailerValueBean bean = new RetailerValueBean(list
                            .getAddr_1(), list.getPhoneNbr(), list
                            .getLastName(), list.getMobileNbr(), list
                            .getAddr_2(), list.getFirstName(), list
                            .getEmail_id(), list.getType(), list.getService());
                    showLocInfoAnim(locationDesclay, true, bean);
                }
            }
        });
        retList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                retailerVisibleIn(RetailerView.LIST);
            }
        });
        retMap.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                retailerVisibleIn(RetailerView.MAP);
            }
        });
        currentLoc.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isMarkerClickable = false;
                getNearestLocation();
            }
        });
        listLay = (LinearLayout) findViewById(R.id.listLay);
        MapsInitializer.initialize(getApplicationContext());
        slideDown = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        slideUp = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);

        setUpMapIfNeeded();

        stateDialogView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_state_dialog, null);
        cityDialogView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_city_dialog, null);

        stateListView = (ListView) stateDialogView
                .findViewById(R.id.state_dialog_list);
        stateListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                dialog.dismiss();
            }
        });
        // stateListView.setOnItemClickListener(new OnItemClickListener() {
        // @Override
        // public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
        // long arg3) {
        // dialog.dismiss();
        // state.setText(((TextView) ((LinearLayout) arg1).getChildAt(0))
        // .getText().toString());
        // // if (GlobalVariables.isLive) {
        // String path =
        // "com/skilrock/pms/mobile/lmsMgmt/action/fetchCityList.action?";
        // JSONObject data = null;
        // try {
        // data = new JSONObject("{\"stateCode\":\""
        // + stateModal.getStateList().get(pos).getStateCode()
        // + "\",\"playerName\":\""
        // + DataSource.Login.username + "\"}");
        // } catch (JSONException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // new PMSWebTask(LocateRetailer.this, path, data, "CL",
        // CityModal.class, "Getting city list").execute();
        //
        // // } else {
        // // cityModal = (CityModal) GlobalVariables.fakeParser(
        // // LocalJsons.cityJson, CityModal.class);
        // // cityAdapter = new CityAdapter(LocateRetailer.this,
        // // R.layout.main_list_row, cityModal.getCityList());
        // // cityListView.setAdapter(cityAdapter);
        // // city.setEnabled(true);
        // //
        // // }
        // }
        // });
        cityListView = (ListView) cityDialogView
                .findViewById(R.id.city_dialog_list);
        cityListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                dialog.dismiss();
            }
        });
        // cityListView.setOnItemClickListener(new OnItemClickListener() {
        // @Override
        // public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
        // long arg3) {
        // dialog.dismiss();
        // city.setText(((TextView) ((LinearLayout) arg1).getChildAt(0))
        // .getText().toString());
        // // if (GlobalVariables.isLive) {
        // String path =
        // "com/skilrock/pms/mobile/lmsMgmt/action/fetchRetailerInfo.action?";
        // JSONObject data = null;
        // try {
        // data = new JSONObject("{\"cityCode\":\""
        // + cityModal.getCityList().get(pos).getCityCode()
        // + "\",\"isRetailer\":true, \"playerName\":\""
        // + DataSource.Login.username + "\"}");
        // } catch (JSONException e) {
        // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // if (GooglePlayServicesUtil
        // .isGooglePlayServicesAvailable(getApplicationContext()) !=
        // ConnectionResult.SUCCESS) {
        //
        // new AlertDialog.Builder(LocateRetailer.this)
        // .setIcon(android.R.drawable.ic_dialog_alert)
        // .setTitle("Wait...!")
        // .setMessage("Please install google play services.")
        // .setCancelable(false)
        // .setPositiveButton("Ok",
        // new DialogInterface.OnClickListener() {
        // @Override
        // public void onClick(
        // DialogInterface dialog,
        // int which) {
        // dialog.dismiss();
        // }
        // }).show();
        //
        // } else {
        // new PMSWebTask(LocateRetailer.this, path, data, "RL",
        // RetailerListModal.class, "Getting Retailer list")
        // .execute();
        // }
        //
        // }
        // });
        dialog = new Dialog(LocateRetailer.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        state = (CustomTextView) findViewById(R.id.state);
        city = (CustomTextView) findViewById(R.id.city);
        city.setEnabled(false);
        stateLay.setOnClickListener(commonClickListener);
        cityLay.setOnClickListener(commonClickListener);
        city.setEnabled(false);
        try {
            data = new JSONObject("{\"playerName\":\""
                    + DataSource.Login.username + "\"}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (googleMap != null) {

            // googleMap.addMarker()
            googleMap.setOnMapClickListener(new OnMapClickListener() {
                @Override
                public void onMapClick(LatLng arg0) {
                    if (locationDesclay.getVisibility() == View.VISIBLE) {
                        showLocInfoAnim(locationDesclay, false, null);
                    }
                }
            });
            googleMap.setOnMarkerClickListener(retailersClick);
        }
        // new PMSWebTask(LocateRetailer.this, path, data, "SL", StateModal.class,
        // "Getting State List").execute();
        if (getIntent().hasExtra(LOAD_LIST) && getIntent().getBooleanExtra(LOAD_LIST, false)) {
            retailerVisibleIn(RetailerView.LIST);
            //getNearestLocation();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        selectedItemId = R.id.locate_ret;
        setUpMapIfNeeded();
        if (isScratchRet) {
            retTypeImage.setVisibility(View.VISIBLE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            if (googleMap != null)
                googleMap.setOnMarkerClickListener(null);
            RetailerListModal listModal = (RetailerListModal) getIntent()
                    .getSerializableExtra("modal");
            RetailerList retList = listModal.getRetailerList().get(0);
            RetailerValueBean valueBean = new RetailerValueBean(retList.getAddr_1(), retList.getPhoneNbr(), retList.getLastName(), retList.getMobileNbr(), retList.getAddr_2(), retList.getFirstName(), retList.getEmail_id(), retList.getType(), retList.getService());
            valueBean.setLatLng(null);
            if (locationDesclay.getVisibility() != View.VISIBLE) {
                showLocInfoAnim(locationDesclay, true, valueBean);
            }
            drawMaps(listModal);
            if (googleMap != null)
                googleMap.setOnMarkerClickListener(scratchRetailersClick);
            tabLay.setVisibility(View.GONE);
            currentLoc.setVisibility(View.GONE);
            filter.setVisibility(View.GONE);
            infoIcon.setVisibility(View.GONE);
            headerText.setText(getResources().getString(R.string.locate_retailer));
            headerSubText.setText(getResources().getString(R.string.scratch_retailer));
            if (googleMap != null) {
                RetailerListModal filterModel = new Gson().fromJson(fakeJson, RetailerListModal.class);
                localRouteBase = new LatLng(Double.parseDouble(filterModel.getLat()), Double.parseDouble(filterModel.getLng()));
                //locateAtMap(localRouteBase, R.drawable.my_location_point);
                // getNearestLocation();
            }
        } else {
            preferences = getSharedPreferences("name", 0);
            if (!preferences.getBoolean("show", false)) {
                showInfoAnim(infoView, true);
                editor = getSharedPreferences("name", 0).edit();
                editor.putBoolean("show", true);
                editor.commit();
            }

            retTypeImage.setVisibility(View.VISIBLE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            if (googleMap != null) {
                if (filteredModal != null) {
                    LatLng latLng = new LatLng(Double.parseDouble(filteredModal.getLat()), Double.parseDouble(filteredModal.getLng()));
                    //locateAtMap(latLng, R.drawable.my_location_point);
                }
                googleMap.setOnMarkerClickListener(retailersClick);
            }
            tabLay.setVisibility(View.VISIBLE);
            currentLoc.setVisibility(View.GONE);
            filter.setVisibility(View.VISIBLE);
            infoIcon.setVisibility(View.VISIBLE);
            headerText.setText("LOCATE RETAILER");
            headerSubText.setText("MAP VIEW");
            // if (fakeJson != null)
            getNearestLocation();
            if (locationDesclay.getVisibility() != View.VISIBLE) {
                showLocInfoAnim(locationDesclay, true, new RetailerValueBean(
                        "", "", "", "", "", ""
                        + "Your Current Location", "", "Current", null));
            }
        }
    }

    private void locateAtMap(LatLng latLng, int pinRes) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory
                .fromResource(pinRes));
        googleMap.addMarker(markerOptions);
        locateMap(latLng, 12);
    }


    private void retailerVisibleIn(RetailerView visible) {
        switch (visible) {
            case MAP:
                analytics.sendAll(Fields.Category.LOCATE_RETAILER_VIEW, Fields.Action.CLICK, Fields.Label.MAP);

                headerSubText.setText("MAP VIEW");
                listLay.setVisibility(View.GONE);
                mapLay.setVisibility(View.VISIBLE);
                retMap.setTextColor(getResources().getColor(
                        R.color.loc_white));
                retMap.setChecked(true);
                retList.setTextColor(getResources().getColor(
                        R.color.loc_purple));
                retList.setChecked(false);
                break;

            case LIST:
                analytics.sendAll(Fields.Category.LOCATE_RETAILER_VIEW, Fields.Action.CLICK, Fields.Label.LIST);
                headerSubText.setText("LIST VIEW");
                listLay.setVisibility(View.VISIBLE);
                mapLay.setVisibility(View.GONE);
                retList.setTextColor(getResources().getColor(
                        R.color.loc_white));
                retList.setChecked(true);
                retMap.setTextColor(getResources().getColor(
                        R.color.loc_purple));
                retMap.setChecked(false);
                break;
        }
    }

    private OnClickListener commonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!GlobalVariables.connectivityExists(LocateRetailer.this)) {
                GlobalVariables.showDataAlert(LocateRetailer.this);
                return;
            }
            switch (v.getId()) {
                case R.id.state_lay:
                    new Place(LocateRetailer.this, "Loading States...", selection).execute();
                    break;
                case R.id.city_lay:
                    if (stateCode != null) {
                        new Place(LocateRetailer.this, stateCode, "Loading Cities...", selection).execute();
                    } else {
                        Utils.Toast(LocateRetailer.this, "Please select state first!");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private Place.Selection selection = new Place.Selection() {
        String retailerPath = "/com/skilrock/pms/mobile/lmsMgmt/action/fetchNearByRetailerInfo.action?requestData=";

        @Override
        public void onStateSelected(String code, String name) {
            state.setText(name);
            city.setText("Select City");
            stateCode = code;
        }

        @Override
        public void onCitySelected(String code, String name) {
            city.setText(name);
            cityCode = code;
            JSONObject object = new JSONObject();
            try {
                object.put("cityCode", cityCode);
                object.put("stateCode", stateCode);
                object.put("isCitySearch", true);
                PMSWebTask PMSWebTask = new PMSWebTask(LocateRetailer.this, retailerPath + URLEncoder.encode(object.toString()), "GET", null, "RETAILER_HACK", null, "Loading...");
                PMSWebTask.execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the
        // map.
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
            if (status == ConnectionResult.SUCCESS) {
                googleMap = ((SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map)).getMap();
                googleMap.setMyLocationEnabled(true);
                //googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                // googleMap.getUiSettings().setZoomControlsEnabled(true);
                // Check if we were successful in obtaining the map.
                if (googleMap != null) {
                    setUpMap();
                }
            } else {
                tabLay.setVisibility(View.GONE);
                mapLay.setVisibility(View.GONE);
//                int requestCode = 10;
//                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
//                dialog.show();

                updateGoogleplay();
            }
        }
    }

    public void updateGoogleplay() {
        OnClickListener okClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                callMarketPlace();
                dBox.dismiss();
                finish();
            }
        };

        OnClickListener calcelClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                dBox.dismiss();
                finish();
            }
        };

        dBox = new DownloadDialogBox(this, "This Application Want To Update You Google Play Services App", "Update Google Play Services", true, true, okClickListener, calcelClickListener, "OK", "CANCEL");
        dBox.show();
    }

    public void callMarketPlace() {
        try {
            startActivityForResult(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + "com.google.android.gms")), 1);
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivityForResult(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + "com.google.android.gms")), 1);
        }
    }


    private void setUpMap() {
        // googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        if (dialog != null)
            dialog.dismiss();
        if (methodType.equals("RETAILER_HACK")) {
            JSONObject jsonObject = JSONParser.parse((String) resultData);
            if (resultData != null) {
                JSONObject object;
                try {
                    object = new JSONObject(resultData.toString());
                    if (object.has("errorCode")) {
                        if (object.getInt("errorCode") != 0) {
                            downloadDialogBox = new DownloadDialogBox(this, object.getString("errorMsg"), "", false, true, null, null);
                            downloadDialogBox.show();
                            dialog.dismiss();
                            return;
                        }
                    }
                    if (object.has("responseCode")) {
                        if (object.getInt("responseCode") != 0) {
                            downloadDialogBox = new DownloadDialogBox(this, object.getString("responseMsg"), "", false, true, null, null);
                            downloadDialogBox.show();
                            dialog.dismiss();
                            return;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                fakeJson = (String) resultData;
                modal = new Gson().fromJson((String) resultData, RetailerListModal.class);
                retailerListModals.clear();
                retailerListModals.addAll(modal.getRetailerList());
                services = modal.getOrgServices();
                types = modal.getOrgTypes();
                drawMaps(modal);
//                retailerListView.setAdapter(new RetailerAdapter(
//                        getApplicationContext(),
//                        R.layout.retailer_list_row, modal.getRetailerList()));
                dialog.dismiss();
            } else {
                GlobalVariables.showServerErr(LocateRetailer.this);
                dialog.dismiss();
            }
        }
    }

    private void locateMap(LatLng latLng, float zoom) {
        position = new CameraPosition.Builder().zoom(zoom).target(latLng)
                .build();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
    }


    private class NoRetailerFoundAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public NoRetailerFoundAdapter(Context context, int textViewResourceId,
                                      List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    private class MyInfoWindowAdapter implements InfoWindowAdapter {

        List<RetailerList> retailerLists;
        RetailerList retailerList;
        int myPos;
        TextView name, address, contact, email;

        int integer = 0;

        public MyInfoWindowAdapter(List<RetailerList> retailerListModal) {
            // System.out.println("called one  " + (+integer));
            mapData = new HashMap<LatLng, List<RetailerValueBean>>();
            retailerLists = retailerListModal;
            getPrepareRetailerListData(retailerLists);
        }

        @Override
        public View getInfoWindow(Marker marker) {

            // LinearLayout mLayout = new
            // LinearLayout(getApplicationContext());
            // mLayout.setLayoutParams(new LinearLayout.LayoutParams(
            // LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            // mLayout.setOrientation(LinearLayout.VERTICAL);
            View view = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.map_info_window, null);
            // ListView listView = (ListView)
            // view.findViewById(R.id.main_menu);
            // ((View) view.findViewById(R.id.menu_header))
            // .setVisibility(View.GONE);
            List<RetailerValueBean> mapAddressList = mapData.get(new LatLng(
                    latLongFormater(marker.getPosition().latitude),
                    latLongFormater(marker.getPosition().longitude)));

            // System.out.println(latLongFormater(marker.getPosition().latitude)
            // + "==getting==="
            // + latLongFormater(marker.getPosition().longitude));
            if (mapAddressList != null) {
                if (mapAddressList.size() > 0) {
                    RetailerValueBean showData = mapAddressList.get(0);
                    name = (TextView) view.findViewById(R.id.mapfirstLastName);
                    address = (TextView) view.findViewById(R.id.mapaddress);
                    contact = (TextView) view.findViewById(R.id.mapcontact);
                    email = (TextView) view.findViewById(R.id.mapemail);

                    name.setText(showData.getFirstName() + " "
                            + showData.getLastName());

                    address.setText(showData.getAddr_1() + showData.getAddr_2());
                    if (showData.getPhoneNbr().length() > 1
                            && showData.getMobileNbr().length() > 1) {
                        contact.setText(showData.getPhoneNbr() + ", "
                                + showData.getMobileNbr());
                    } else if (showData.getMobileNbr().length() > 1
                            && !(showData.getPhoneNbr().length() > 1)) {
                        contact.setText(showData.getMobileNbr());

                    } else if (showData.getPhoneNbr().length() > 1
                            && !(showData.getMobileNbr().length() > 1)) {
                        contact.setText(showData.getPhoneNbr());

                    } else if (!(showData.getPhoneNbr().length() > 1)
                            && !(showData.getMobileNbr().length() > 1)) {
                        contact.setVisibility(TextView.GONE);
                    } else {
                        contact.setVisibility(TextView.GONE);

                    }
                    email.setText(showData.getEmail_id());

                }
                // return mLayout;
                return null;
            } else {

                TextView me = new TextView(getApplicationContext());
                me.setText("");
                return null;

            }

        }

        @Override
        public View getInfoContents(Marker arg0) {

            return null;
        }

        private void getPrepareRetailerListData(
                List<RetailerList> retailerLists2) {
            List<RetailerValueBean> mRetailerList = new ArrayList<RetailerValueBean>();

            for (int i = 0; i < retailerLists2.size(); i++) {

                mRetailerList.add(new RetailerValueBean(retailerLists2.get(i)
                        .getAddr_1(), retailerLists2.get(i).getPhoneNbr(),
                        retailerLists2.get(i).getLastName(), retailerLists2
                        .get(i).getMobileNbr(), retailerLists2.get(i)
                        .getAddr_2(), retailerLists2.get(i)
                        .getFirstName(), retailerLists2.get(i)
                        .getEmail_id(), new LatLng(
                        latLongFormater(Double
                                .parseDouble(retailerLists2.get(i)
                                        .getLatitude())),
                        latLongFormater(Double
                                .parseDouble(retailerLists2.get(i)
                                        .getLongitude()))), retailerLists2.get(i).getType(), retailerLists2.get(i).getService()));

                Utils.consolePrint(latLongFormater(Double
                        .parseDouble(retailerLists2.get(i).getLatitude()))
                        + "======"
                        + latLongFormater(Double.parseDouble(retailerLists2
                        .get(i).getLongitude())));

            }

            for (int j = 0; j < mRetailerList.size(); j++) {
                RetailerValueBean mBean = mRetailerList.get(j);
                if (!mapData.containsKey(mBean.getLatLng())) {
                    List<RetailerValueBean> retList = new ArrayList<RetailerValueBean>();
                    RetailerValueBean tempData = new RetailerValueBean(
                            mBean.getAddr_1(), mBean.getPhoneNbr(),
                            mBean.getLastName(), mBean.getMobileNbr(),
                            mBean.getAddr_2(), mBean.getFirstName(),
                            mBean.getEmail_id(), mBean.getType(), mBean.getServices());
                    retList.add(tempData);
                    if (retList.size() > 0)
                        mapData.put(mBean.getLatLng(), retList);
                } else {
                    List<RetailerValueBean> retList = new ArrayList<RetailerValueBean>();
                    retList.addAll(mapData.get(mBean.getLatLng()));
                    RetailerValueBean tempData = new RetailerValueBean(
                            mBean.getAddr_1(), mBean.getPhoneNbr(),
                            mBean.getLastName(), mBean.getMobileNbr(),
                            mBean.getAddr_2(), "(" + j + ")"
                            + mBean.getFirstName(), mBean.getEmail_id(), mBean.getType(), mBean.getServices());
                    retList.add(tempData);
                    Utils.consolePrint("Duplicate size ===" + retList.size());
                    if (retList.size() > 0)
                        mapData.put(mBean.getLatLng(), retList);
                }
            }

        }
    }

    // Retailer Data bean to show on Map

    // Method to formatting latitude/longitude value
    private double latLongFormater(double value) {

        return round(value, 6);
        // return Double.parseDouble(new
        // DecimalFormat("##.#####").format(value));
    }

    private class InfoListAdapter extends ArrayAdapter<RetailerValueBean> {
        List<RetailerValueBean> objects;

        public InfoListAdapter(Context context, int resource,
                               List<RetailerValueBean> objects) {
            super(context, resource, objects);
            this.objects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            RetailerValueBean showData = objects.get(position);
            if (convertView == null) {
                holder = new Holder();
                convertView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.map_info_window, null);
                holder.name = (TextView) convertView
                        .findViewById(R.id.mapfirstLastName);
                holder.address = (TextView) convertView
                        .findViewById(R.id.mapaddress);
                holder.contact = (TextView) convertView
                        .findViewById(R.id.mapcontact);
                holder.email = (TextView) convertView
                        .findViewById(R.id.mapemail);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.name.setText(showData.getFirstName() + " "
                    + showData.getLastName());

            holder.address.setText(showData.getAddr_1() + showData.getAddr_2());
            if (showData.getPhoneNbr().length() > 1
                    && showData.getMobileNbr().length() > 1) {
                holder.contact.setText(showData.getPhoneNbr() + ", "
                        + showData.getMobileNbr());
            } else if (showData.getMobileNbr().length() > 1
                    && !(showData.getPhoneNbr().length() > 1)) {
                holder.contact.setText(showData.getMobileNbr());

            } else if (showData.getPhoneNbr().length() > 1
                    && !(showData.getMobileNbr().length() > 1)) {
                holder.contact.setText(showData.getPhoneNbr());

            } else if (!(showData.getPhoneNbr().length() > 1)
                    && !(showData.getMobileNbr().length() > 1)) {
                holder.contact.setVisibility(TextView.GONE);
            } else {
                holder.contact.setVisibility(TextView.GONE);

            }
            holder.email.setText(showData.getEmail_id());
            return convertView;
        }

        class Holder {
            TextView name, address, contact, email;
        }
    }

    public Bitmap writeOnDrawable(int drawableId, String text) {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);

        Paint paint = new Paint();
        paint.setStyle(Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);

        Canvas canvas = new Canvas(bm);
        canvas.drawText(text, canvas.getWidth() / 2, canvas.getHeight() / 2,
                paint);

        return bm;
    }

    private Bitmap writeTextOnDrawable(int drawableId, String text) {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);

        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

        Paint paint = new Paint();
        paint.setStyle(Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(tf);
        paint.setTextAlign(Align.CENTER);
        paint.setTextSize(convertToPixels(22));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bm);

        // If the text is bigger than the canvas , reduce the font size
        if (textRect.width() >= (canvas.getWidth() - 4)) // the padding on
            // either sides is
            // considered as 4,
            // so as to
            // appropriately fit
            // in the text
            paint.setTextSize(convertToPixels(14)); // Scaling needs to
        // be used for
        // different dpi's

        // Calculate the positions
        int xPos = (canvas.getWidth() / 2) - 2; // -2 is for regulating the x
        // position offset
        // "- ((paint.descent() + paint.ascent()) / 2)" is the distance from the
        // baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint
                .ascent()) / 2));

        canvas.drawText(text, xPos, yPos, paint);

        return bm;
    }

    public int convertToPixels(int nDP) {
        final float conversionScale = getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f);

    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                // Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, PolylineOptions> {

        @Override
        protected PolylineOptions doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                // e.printStackTrace();
            }

            ArrayList<LatLng> points = null;
            polyLineOptions = null;
            // traversing through routes
            if (routes != null && routes.size() > 0) {
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<LatLng>();

                    List<HashMap<String, String>> path = routes.get(i);

                    // System.out.println("path-----------" + path.size());

                    if (!(path.size() > 7000)) {
                        polyLineOptions = new PolylineOptions();
                        for (int j = 0; j < path.size(); j++) {
                            HashMap<String, String> point = path.get(j);

                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);
                            // Log.i("loc", position + "");
                            points.add(position);
                        }
                    }
                    if (points != null) {
                        polyLineOptions.addAll(points);
                        polyLineOptions.width(5);
                        polyLineOptions.color(Color.GREEN);
                    }
                }
            }

            return polyLineOptions;
        }

        @Override
        protected void onPostExecute(PolylineOptions routes) {
            if (routes != null) {

                try {
                    polyline = googleMap.addPolyline(routes);
                } catch (OutOfMemoryError e) {
                    // Utils.Toast(getApplicationContext(),
                    // "No Routes Found",
                    // Toast.LENGTH_SHORT);
                }
            } else {
                // Utils.Toast(getApplicationContext(), "No Routes Found",
                // Toast.LENGTH_SHORT);
            }

        }

    }

    private String getMapsApiDirectionsUrl(LatLng src, LatLng dest) {
        String waypoints = "waypoints=optimize:true&origin=" + src.latitude
                + "," + src.longitude + "&destination=" + dest.latitude + ","
                + dest.longitude;

        String sensor = "sensor=false";
        String params = waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
        // Log.i("url", url);
        return url;
    }

    public void getNearestLocation() {
        gpsTracker = new GPSTracker(LocateRetailer.this);

        if (gpsTracker.canGetLocation()) {
            if (fakeJson != null) {
                modal = new Gson().fromJson(fakeJson, RetailerListModal.class);
                modal.setLat(gpsTracker.getLatitude() + "");
                modal.setLng(gpsTracker.getLongitude() + "");
                drawMaps(modal);
            }
            // String path =
            // "com/skilrock/pms/mobile/lmsMgmt/action/fetchNearByRetailerInfo.action?";
            // String data = "{\"playerName\":\"dushyant"
            // + "\", \"isGps\":\"ON\",\"lat\":\"" + tracker.getLatitude()
            // + "\",\"lng\":\"" + tracker.getLongitude() + "\"}";
            // JSONObject jsonObject;
            // try {
            // jsonObject = new JSONObject("");
            // new PMSWebTask(LocateRetailer.this, path, jsonObject, "NBL",
            // RetailerListModal.class, "Getting Nearest Retailers...")
            // .execute();
            // } catch (JSONException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            // navigateToCurrentLoc(tracker);
        } else {
            OnClickListener okClickListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    downloadDialogBox.dismiss();
                }
            };
            OnClickListener cancelClickListener = new OnClickListener() {
                @Override
                public void onClick(View view) {
                    downloadDialogBox.dismiss();
//                    modal = new Gson().fromJson(fakeJson,
//                            RetailerListModal.class);
//                    drawMaps(modal);
                }
            };
            downloadDialogBox = new DownloadDialogBox(LocateRetailer.this, getResources().getString(R.string.network_error), "GPS", true, true, okClickListener, cancelClickListener);
            downloadDialogBox.show();
        }
    }

    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private void bindViewIds() {
        servicesView = (LinearLayout) findViewById(R.id.service_view);
        llRetailerInfo = (LinearLayout) findViewById(R.id.ll_retailer_info);
        okay = (CustomTextView) findViewById(R.id.okay);
        infoView = (RelativeLayout) findViewById(R.id.info_view);
        currentLoc = (ImageView) findViewById(R.id.imageView1);
        retTypeImage = (ImageView) findViewById(R.id.ret_type);
        tabLay = (LinearLayout) findViewById(R.id.four_opns);
        name = (CustomTextView) findViewById(R.id.ret_name);
        add = (CustomTextView) findViewById(R.id.ret_add);
        contact = (CustomTextView) findViewById(R.id.ret_contact);
        distance = (CustomTextView) findViewById(R.id.distance);
        locationDesclay = (RelativeLayout) findViewById(R.id.location_desc_lay);
        ((CustomTextView) findViewById(R.id.state_icon)).getCompoundDrawables()[2]
                .setColorFilter(
                        getResources().getColor(R.color.loc_purple),
                        Mode.SRC_IN);
        ((CustomTextView) findViewById(R.id.city_icon)).getCompoundDrawables()[2]
                .setColorFilter(
                        getResources().getColor(R.color.loc_purple),
                        Mode.SRC_IN);
        stateLay = (RelativeLayout) findViewById(R.id.state_lay);
        cityLay = (RelativeLayout) findViewById(R.id.city_lay);
        retList = (CustomCheckedTextView) findViewById(R.id.ret_list);
        retMap = (CustomCheckedTextView) findViewById(R.id.ret_map);

        //new initialization
        servicelist1 = (CustomTextView) findViewById(R.id.servicelist1);
        servicelist2 = (CustomTextView) findViewById(R.id.servicelist2);
        servicelist3 = (CustomTextView) findViewById(R.id.servicelist3);
        servicelist4 = (CustomTextView) findViewById(R.id.servicelist4);
        servicelist5 = (CustomTextView) findViewById(R.id.servicelist5);
        servicelist6 = (CustomTextView) findViewById(R.id.servicelist6);
        servicelist7 = (CustomTextView) findViewById(R.id.servicelist7);
        servicelist8 = (CustomTextView) findViewById(R.id.servicelist8);


        typelist1 = (CustomTextView) findViewById(R.id.typelist1);
        typelist2 = (CustomTextView) findViewById(R.id.typelist2);
        typelist3 = (CustomTextView) findViewById(R.id.typelist3);
    }

    @Override
    public void finish() {
        super.finish();
        selectedItemId = -1;
        overridePendingTransition(GlobalVariables.startAmin,
                GlobalVariables.endAmin);
    }

    OnMarkerClickListener retailersClick = new OnMarkerClickListener() {

        @Override
        public boolean onMarkerClick(Marker marker) {
            // drawerLayout.openDrawer(itemListView);

            // if (localRouteBase != null) {
            if (isMarkerClickable) {
                LatLng latLng = new LatLng(
                        latLongFormater(marker.getPosition().latitude),
                        latLongFormater(marker.getPosition().longitude));
                RetailerValueBean data = null;
                try {
                    data = mapData.get(latLng).get(0);
                    // if (locationDesclay.getVisibility() != View.VISIBLE)
                    // {
                    showLocInfoAnim(locationDesclay, true, data);
                    // }
                } catch (Exception e) {
                    e.printStackTrace();
                    data = new RetailerValueBean(
                            "", "", "", "", "", ""
                            + "Your Current Location", "", "Current", null);
                    if (locationDesclay.getVisibility() != View.VISIBLE) {
                        showLocInfoAnim(locationDesclay, true, data);
                    }
                }
//                    LatLng current = new LatLng(
//                            latLongFormater(localRouteBase.latitude),
//                            latLongFormater(localRouteBase.longitude));
//                    if (!(latLng.equals(current))) {
//                        if ((polyline != null)) {
//                            polyline.remove();
//                        }
//                        String url = getMapsApiDirectionsUrl(localRouteBase,
//                                marker.getPosition());
//                        ReadTask downloadTask = new ReadTask();
//                        downloadTask.execute(url);
//                    }
            }
            //  }
            return false;
        }
    };

    private void showLocInfoAnim(View view, boolean isOpen, RetailerValueBean list) {
        if (isOpen) {
            if (list != null) {


                name.setText(list.getFirstName() + " " + list.getLastName());
                List<String> arr = list.getServices();
                if (list.getServices() == null) {
                    servicesView.setVisibility(View.GONE);
                } else {
                    servicesView.setVisibility(View.VISIBLE);
                    servicesView.removeAllViews();
                    int[] intA = new int[arr.size()];
                    for (int j = 0; j < intA.length; j++) {
                        View vi = ((LayoutInflater) context
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                .inflate(R.layout.services_row, null);
                        ImageView imageView = (ImageView) vi.findViewById(R.id.image);

                        switch (arr.get(j).trim().toUpperCase(Locale.ENGLISH)) {
                            case "TICKET CANCEL":
                                intA[j] = R.drawable.cancel;
                                break;
                            case "WINNING CLAIM HIGH PRIZE":
                                intA[j] = R.drawable.high_win;
                                break;
                            case "BUY SCRATCH CARDS":
                                intA[j] = R.drawable.scratch_sale;
                                break;
                            case "BUY DRAW GAMES":
                                intA[j] = R.drawable.dg_sale;
                                break;
                            case "MOBILE RECHARGE":
                                intA[j] = R.drawable.mobile_money;
                                break;
                            case "DEPOSIT":
                                intA[j] = R.drawable.mobile_cash;
                                break;
                            case "BUY SPORTS LOTTERY":
                                intA[j] = R.drawable.sports_lot;
                                break;
                            case "WINNING CLAIM LOW PRIZE UPTO $100":
                                intA[j] = R.drawable.dg_win_claim;
                                break;

                        }
                        imageView.setImageResource(intA[j]);
                        servicesView.addView(vi, j);
                    }

                }
                if (!list.getAddr_1().equals("") || !list.getAddr_2().equals("")) {
                    add.setVisibility(View.VISIBLE);
                    add.setText(list.getAddr_1() + " " + list.getAddr_2());
                } else
                    add.setVisibility(View.GONE);
                int imageId = 0;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llRetailerInfo.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                llRetailerInfo.setLayoutParams(params);
                switch (list.getType().toUpperCase(Locale.ENGLISH)) {
                    case "AGENT":
                        imageId = R.drawable.agent;
                        break;
                    case "BO":
                        imageId = R.drawable.back_office;
                        break;
                    case "RETAILER":
                        imageId = R.drawable.retailer;
                        break;
                    default:
                        return;
//                        imageId = R.drawable.my_location_point;
//                        params.addRule(RelativeLayout.CENTER_VERTICAL);
//                        llRetailerInfo.setLayoutParams(params);
//

                }
                Animation btn_toogleanim = AnimationUtils.loadAnimation(
                        getApplicationContext(), R.anim.anim_translate);
                view.startAnimation(btn_toogleanim);
                view.setVisibility(View.VISIBLE);
                retTypeImage.setImageResource(imageId);
                if (list.getPhoneNbr().length() > 1
                        && list.getMobileNbr().length() > 1) {
                    contact.setText(list.getEmail_id() + "\n" + "Phone : "
                            + list.getPhoneNbr() + "\n" + "Mobile : "
                            + list.getMobileNbr());
                } else if (list.getPhoneNbr().length() > 1
                        && !(list.getMobileNbr().length() > 1)) {
                    contact.setText(list.getEmail_id() + "\n" + "Phone : "
                            + list.getPhoneNbr());
                } else if (!(list.getPhoneNbr().length() > 1)
                        && (list.getMobileNbr().length() > 1)) {
                    contact.setText(list.getEmail_id() + "\n" + "Mobile : "
                            + list.getMobileNbr());
                } else if (!(list.getPhoneNbr().length() > 1)
                        && !(list.getMobileNbr().length() > 1)) {
                    contact.setText(list.getEmail_id());
                } else {
                    contact.setText(list.getEmail_id());

                }
                if (list.getMobileNbr().equals("")) {
                    contact.setVisibility(View.GONE);
                }
            }
        } else {
            Animation btn_toogleanim = AnimationUtils.loadAnimation(
                    getApplicationContext(), R.anim.anim_translate_return);
            view.startAnimation(btn_toogleanim);
            view.setVisibility(View.GONE);
        }
    }

    private void showInfoAnim(View view, boolean isShow) {
        if (isShow) {
//            Animation btn_toogleanim = AnimationUtils.loadAnimation(
//                    getApplicationContext(), R.anim.zoom_in);
//            view.startAnimation(btn_toogleanim);
            createInfoView();
            view.setVisibility(View.VISIBLE);
        } else {
//            Animation btn_toogleanim = AnimationUtils.loadAnimation(
//                    getApplicationContext(), R.anim.zoom_out);
//            view.startAnimation(btn_toogleanim);
            view.setVisibility(View.GONE);
        }
    }

    //new info view
    private void createInfoView() {
        if (services != null && types != null) {
            for (int j = 0; j < services.size(); j++) {
                check_Service_Res(services, j);
            }
            for (int j = 0; j < types.size(); j++) {
                check_Type_Res(types, j);
            }
        }
    }

    private void check_Type_Res(List<String> type, int position) {
        switch (type.get(position).trim().toUpperCase(Locale.ENGLISH)) {
            case "AGENT":
                typelist1.setVisibility(View.VISIBLE);
                typelist1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.agent, 0, 0, 0);
                typelist1.setText(type.get(position).toString());
                findViewById(R.id.typelist1_divider).setVisibility(View.VISIBLE);
                break;
            case "BO":
                typelist2.setVisibility(View.VISIBLE);
                typelist2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.back_office, 0, 0, 0);
                typelist2.setText(type.get(position).toString());
                findViewById(R.id.typelist2_divider).setVisibility(View.VISIBLE);
                break;
            case "RETAILER":
                typelist3.setVisibility(View.VISIBLE);
                typelist3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.retailer, 0, 0, 0);
                typelist3.setText(type.get(position).toString());
                break;
        }
    }

    private void check_Service_Res(List<String> service, int j) {
        switch (service.get(j).trim().toUpperCase(Locale.ENGLISH)) {
            case "TICKET CANCEL":
                servicelist1.setVisibility(View.VISIBLE);
                servicelist1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cancel, 0, 0, 0);
                servicelist1.setText(service.get(j).toString());
                findViewById(R.id.servicelist1_divider).setVisibility(View.VISIBLE);
                break;
            case "WINNING CLAIM HIGH PRIZE":
                servicelist2.setVisibility(View.VISIBLE);
                servicelist2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.high_win, 0, 0, 0);
                servicelist2.setText(service.get(j).toString());
                findViewById(R.id.servicelist2_divider).setVisibility(View.VISIBLE);
                break;
            case "BUY SCRATCH CARDS":
                servicelist3.setVisibility(View.VISIBLE);
                servicelist3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.scratch_sale, 0, 0, 0);
                servicelist3.setText(service.get(j).toString());
                findViewById(R.id.servicelist3_divider).setVisibility(View.VISIBLE);
                break;
            case "BUY DRAW GAMES":
                servicelist4.setVisibility(View.VISIBLE);
                servicelist4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dg_sale, 0, 0, 0);
                servicelist4.setText(service.get(j).toString());
                findViewById(R.id.servicelist4_divider).setVisibility(View.VISIBLE);

                break;
            case "MOBILE RECHARGE":
                servicelist5.setVisibility(View.VISIBLE);
                servicelist5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mobile_money, 0, 0, 0);
                servicelist5.setText(service.get(j).toString());
                findViewById(R.id.servicelist5_divider).setVisibility(View.VISIBLE);

                break;
            case "DEPOSIT":
                servicelist6.setVisibility(View.VISIBLE);
                servicelist6.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mobile_cash, 0, 0, 0);
                servicelist6.setText(service.get(j).toString());
                findViewById(R.id.servicelist6_divider).setVisibility(View.VISIBLE);

                break;
            case "BUY SPORTS LOTTERY":
                servicelist7.setVisibility(View.VISIBLE);
                servicelist7.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sports_lot, 0, 0, 0);
                servicelist7.setText(service.get(j).toString());
                findViewById(R.id.servicelist7_divider).setVisibility(View.VISIBLE);
                break;

            case "WINNING CLAIM LOW PRIZE UPTO $100":
                servicelist8.setVisibility(View.VISIBLE);
                servicelist8.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dg_win_claim, 0, 0, 0);
                servicelist8.setText(service.get(j).toString());
                break;
        }
    }

    OnMarkerClickListener scratchRetailersClick = new OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            RetailerValueBean modal = (RetailerValueBean) getIntent()
                    .getSerializableExtra("modal");
            if (locationDesclay.getVisibility() != View.VISIBLE) {
                showLocInfoAnim(locationDesclay, true, modal);
            }
            return false;
        }
    };

    private void drawMaps(RetailerListModal resultData) {

        retailerListModal = resultData;
        if (retailerListModal != null) {

            if (retailerListModal.isSuccess()) {
                isMarkerClickable = true;
//                ArrayList<CityList> cityLists = new ArrayList<CityModal.CityList>();
//                CityModal.CityList list = new CityModal().new CityList();
//                list.setCityName("London");
//                list.setCityCode("101");
//                cityLists.add(list);
//                cityAdapter = new CityAdapter(LocateRetailer.this,
//                        R.layout.main_list_row, cityLists);
//                cityListView.setAdapter(cityAdapter);
//
//                ArrayList<StateList> stateLists = new ArrayList<StateModal.StateList>();
//                StateModal.StateList statelist = new StateModal().new StateList();
//                statelist.setStateName("UK");
//                statelist.setStateCode("101");
//                stateLists.add(statelist);
//                stateAdapter = new StateAdapter(LocateRetailer.this,
//                        R.layout.main_list_row, stateLists);
//                stateListView.setAdapter(stateAdapter);
//                city.setText(list.getCityName());
//                state.setText(statelist.getStateName());
//                if (retailerListModals != null) {
//                    if (retailerListModals.size() > 0) {
//                        retailerListModals.clear();
//                    }
//                }
                if (googleMap == null) {
                    Utils.Toast(context, "google map not found");
                    return;
                }
                googleMap.clear();
                ArrayList<Marker> markers = new ArrayList<>();
                for (int i = 0; i < retailerListModal.getRetailerList().size(); i++) {

                    RetailerList retailerList = retailerListModal
                            .getRetailerList().get(i);

                    retailerListModals.add(retailerList);
                    latLng = new LatLng(Double.parseDouble(retailerListModal
                            .getRetailerList().get(i).getLatitude()),
                            Double.parseDouble(retailerListModal
                                    .getRetailerList().get(i).getLongitude()));
                    MarkerOptions marker = new MarkerOptions().position(latLng);
                    String arr = retailerListModal
                            .getRetailerList().get(i).getType();
                    switch (arr.trim().toUpperCase(Locale.ENGLISH)) {
                        case "BO":
                            typeImages = R.drawable.back_office;
                            break;
                        case "AGENT":
                            typeImages = R.drawable.agent;
                            break;
                        case "RETAILER":
                            typeImages = R.drawable.retailer;
                            break;

                    }
                    marker.icon(BitmapDescriptorFactory
                            .fromResource(typeImages));

                    markers.add(googleMap.addMarker(marker));

                }
                LatLngBounds.Builder b = new LatLngBounds.Builder();
                for (Marker m : markers) {
                    b.include(m.getPosition());
                }
                LatLngBounds bounds = b.build();
//Change the padding as per needed
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100, 100, 5);
                googleMap.animateCamera(cu);
                googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter(
                        retailerListModal.getRetailerList()));
                //isClickable = true;
//                if (retailerListModal.getLat() != null && retailerListModal.getLng() != null) {
//                    localRouteBase = new LatLng(
//                            Double.parseDouble(retailerListModal.getLat()),
//                            Double.parseDouble(retailerListModal.getLng()));
//                    locateAtMap(localRouteBase, R.drawable.my_location_point);
//                }
                isRetailerAddressClickabale = true;
                retailerListView.setAdapter(new RetailerAdapter(
                        getApplicationContext(), R.layout.retailer_list_row,
                        retailerListModal.getRetailerList()));

            } else {
                isMarkerClickable = false;
                Utils.Toast(LocateRetailer.this,
                        retailerListModal.getErrorMsg());
            }
        } else {
            isMarkerClickable = false;
            Utils.Toast(LocateRetailer.this, "Server error!" + ""
            );
        }

    }

    private ArrayList<RetailerList> selectedServ = new ArrayList<RetailerList>();

    private void filterLocations(String[] types, String[] services) {
        selectedServ.clear();
        for (int i = 0; i < modal.getRetailerList().size(); i++) {
            RetailerList retailerList = modal.getRetailerList().get(i);
            if (types.length != 0) {
                for (int j = 0; j < types.length; j++) {
                    if (services.length != 0) {
                        for (int k = 0; k < services.length; k++) {
                            if (retailerList.getService().contains(services[k]) || retailerList.getType().equalsIgnoreCase(types[j])) {
                                if (!selectedServ.contains(retailerList))
                                    selectedServ.add(retailerList);
                            }
                        }
                    } else {
                        if (retailerList.getType().equalsIgnoreCase(types[j])) {
                            if (!selectedServ.contains(retailerList))
                                selectedServ.add(retailerList);
                        }
                    }
                }
            } else if (services.length != 0) {
                for (int k = 0; k < services.length; k++) {
                    if (retailerList.getService().contains(services[k])) {
                        if (!selectedServ.contains(retailerList))
                            selectedServ.add(retailerList);
                    }
                }
            }
        }
    }


    @Override
    public void onCancelFilterDiloag(boolean[] types, boolean[] services) {
        typesChecked = types;
        servicesChecked = services;
    }

    @Override
    public void onDismissFilterDiloag(String[] typesResult, String[] servicesResult) {
        filterLocations(typesResult, servicesResult);
        if (servicesResult.length > 0) {
            for (int i = 0; i < services.size(); i++) {
                for (int j = 0; j < servicesResult.length; j++) {
                    if (services.get(i).equals(servicesResult[j])) {
                        servicesChecked[services.indexOf(servicesResult[j])] = true;
                        break;
                    } else {
                        servicesChecked[services.indexOf(services.get(i))] = false;
                    }
                }
            }
        } else {
            Arrays.fill(servicesChecked, false);
        }
        if (typesResult.length > 0) {
            for (int i = 0; i < types.size(); i++) {
                for (int j = 0; j < typesResult.length; j++) {
                    if (types.get(i).equals(typesResult[j])) {
                        typesChecked[types.indexOf(typesResult[j])] = true;
                        break;
                    } else {
                        typesChecked[types.indexOf(types.get(i))] = false;
                    }
                }
            }
        } else {
            Arrays.fill(typesChecked, false);
        }
        retailerListModals.clear();
        retailerListModals.addAll(selectedServ);
        filteredModal = new Gson().fromJson(fakeJson, RetailerListModal.class);
        filteredModal.setRetailerList(selectedServ);
        drawMaps(filteredModal);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (infoView.getVisibility() == View.VISIBLE) {
                infoView.setVisibility(View.GONE);
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (downloadDialogBox != null)
            downloadDialogBox.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
        analytics.endAnalytics(this);
    }

}