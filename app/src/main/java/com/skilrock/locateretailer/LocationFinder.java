package com.skilrock.locateretailer;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by stpl on 7/27/2015.
 */
public class LocationFinder {

    private static LocationManager locationManager;
    private static Criteria criteria;
    private static String provider;

    public static Location getLocation(Context context){
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setCostAllowed(false);

        provider = locationManager.getBestProvider(criteria, false);

        Location location = locationManager.getLastKnownLocation(provider);
        return location;
    }

}
