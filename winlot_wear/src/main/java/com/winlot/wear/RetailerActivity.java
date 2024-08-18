package com.winlot.wear;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.skilrock.lms.ui.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.DismissOverlayView;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * Sample that shows how to set up a basic Google Map on Android Wear.
 */
public class RetailerActivity extends Activity implements OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener {

    //private static final LatLng SYDNEY = new LatLng(-33.85704, 151.21522);

    private LatLng SYDNEY[] = new LatLng[]{
            new LatLng(-22.200116, 30.004282), new LatLng(-15.297427, 22.973032),
            new LatLng(-19.362468, 22.259086), new LatLng(-19.030454, 30.960257),
            new LatLng(-18.531189, 24.148734)};


    private int servicesRes[] = new int[]{
            R.drawable.dg_sale, R.drawable.cash_out,
            R.drawable.cash_in, R.drawable.mobile_cash,
            R.drawable.mobile_money, R.drawable.dg_win_claim,
            R.drawable.cancel, R.drawable.high_win
    };
    private String RetailerList[] = new String[]{
            "Dg Sale", "Cash Out",
            "Cash In", "Mobile Cash",
            "Mobile Money", "Dg Win Claim",
            "Cancel", "High Win"
    };
    /**
     * Overlay that shows a short help text when first launched. It also provides an option to
     * exit the app.
     */
    private DismissOverlayView mDismissOverlay;

    /**
     * The map. It is initialized when the map has been fully loaded and is ready to be used.
     *
     * @see #onMapReady(com.google.android.gms.maps.GoogleMap)
     */
    private GoogleMap mMap;
    private LatLng latLng;
    private CameraPosition position;

    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        // Set the layout. It only contains a SupportMapFragment and a DismissOverlay.
        setContentView(R.layout.ret_layt);

        // Retrieve the containers for the root of the layout and the map. Margins will need to be
        // set on them to account for the system window insets.
        final FrameLayout topFrameLayout = (FrameLayout) findViewById(R.id.root_container);
        final FrameLayout mapFrameLayout = (FrameLayout) findViewById(R.id.map_container);

        // Set the system view insets on the containers when they become available.
        topFrameLayout.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                // Call through to super implementation and apply insets
                insets = topFrameLayout.onApplyWindowInsets(insets);

                FrameLayout.LayoutParams params =
                        (FrameLayout.LayoutParams) mapFrameLayout.getLayoutParams();

                // Add Wearable insets to FrameLayout container holding map as margins
                params.setMargins(
                        insets.getSystemWindowInsetLeft(),
                        insets.getSystemWindowInsetTop(),
                        insets.getSystemWindowInsetRight(),
                        insets.getSystemWindowInsetBottom());
                mapFrameLayout.setLayoutParams(params);

                return insets;
            }
        });

        // Obtain the DismissOverlayView and display the intro help text.
        mDismissOverlay = (DismissOverlayView) findViewById(R.id.dismiss_overlay);
        mDismissOverlay.setIntroText("Welcome To Retailer App");
        mDismissOverlay.showIntroIfNecessary();

        // Obtain the MapFragment and set the async listener to be notified when the map is ready.
        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Map is ready to be used.
        mMap = googleMap;

        // Set the long click listener as a way to exit the map.
        mMap.setOnMapLongClickListener(this);


        MarkerOptions marker = null;
        ArrayList<Marker> markers = new ArrayList<>();


//        int r = 0;
        for (int i = 0; i < SYDNEY.length; i++) {

            latLng = new LatLng(SYDNEY[i].latitude, SYDNEY[i].longitude);

            marker = new MarkerOptions().position(SYDNEY[i])
                    .title(RetailerList[i]).icon(BitmapDescriptorFactory
                            .fromResource(servicesRes[i]));

//            mMap.addMarker(new MarkerOptions().position(SYDNEY[i])
//                    .title(RetailerList[i]).icon(BitmapDescriptorFactory
//                            .fromResource(servicesRes[i])));


//            if (servicesRes.length > i) {
//                r = i;
//            marker.icon(BitmapDescriptorFactory
//                    .fromResource(servicesRes[i]));
//            } else {
////                r = 0;
//                marker.icon(BitmapDescriptorFactory
//                        .fromResource(servicesRes[r]));
//            }

            markers.add(googleMap.addMarker(marker));
        }
        LatLngBounds.Builder b = new LatLngBounds.Builder();
        for (Marker m : markers) {
            b.include(m.getPosition());
        }
        LatLngBounds bounds = b.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100, 100, 5);
        googleMap.animateCamera(cu);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        // Display the dismiss overlay with a button to exit this activity.
        mDismissOverlay.show();
    }
}