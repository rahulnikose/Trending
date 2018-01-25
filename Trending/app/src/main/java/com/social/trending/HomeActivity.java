package com.social.trending;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import static com.social.trending.Constants.ARROUND_ME;
import static com.social.trending.Constants.GLOBE;

/**
 * Created by xxnikosr on 2018-01-24.
 */

public class HomeActivity extends AppCompatActivity implements
         GoogleApiClient.ConnectionCallbacks,
         GoogleApiClient.OnConnectionFailedListener,
         NavigationView.OnNavigationItemSelectedListener,
         SwipeRefreshLayout.OnRefreshListener{

    FusedLocationProviderClient mFusedLocationClient;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    public static Location mLocation;

    public static ArrayList<TrendDetail> TrendList = new ArrayList<>();
    public static final String TAG = "HOME_ACTIVITY";

    public static SwipeRefreshLayout swipeRefreshLayout;
    public static TrendsListAdapter myTrendsAdapter;
    public static RecyclerView recyclerView;

    private static final int REQUEST_CODE_PERMISSION_ALL = 1;
    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_get_trends);

        Toolbar toolbar     = findViewById(R.id.toolbar);
        recyclerView        = findViewById(R.id.rv_trends);
        swipeRefreshLayout  = findViewById(R.id.swipe_refresh_layout);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Clear stale trend list.
        TrendList.clear();
        myTrendsAdapter = new TrendsListAdapter(TrendList);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myTrendsAdapter);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(!swipeRefreshLayout.isRefreshing()){
                                            //fetchAndRefreshTrends(ARROUND_ME);
                                        }

                                    }
                                }
        );

        myTrendsAdapter.notifyDataSetChanged();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setTitle("   Top #Trends ");
    }
    @Override
    public void onResume() {

        super.onResume();
        if (mGoogleApiClient != null && mFusedLocationClient != null) {
            requestLocationUpdates();
        } else {
            buildGoogleApiClient();
        }
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnected(Bundle bundle) {
        requestLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void requestLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(30000); // 5 minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
        }else {
            ActivityCompat.requestPermissions(this, PERMISSIONS_LOCATION,
                    REQUEST_CODE_PERMISSION_ALL);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }

    }

    LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public synchronized void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                mLocation = location;
                fetchAndRefreshTrends(ARROUND_ME);
            }
        };

    };

    static synchronized void ClearAdapter(){
        if(TrendList != null ){
            //Clear list, We fetch new one onResume
            TrendList.clear();
            myTrendsAdapter.notifyDataSetChanged();
        }
    }
    static synchronized void UpdateAdapter(){

            myTrendsAdapter.notifyDataSetChanged();
    }
    void fetchAndRefreshTrends(int where){
        new NetworkAsyncTask(getApplicationContext(), where).execute();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_arround_me) {
             fetchAndRefreshTrends(ARROUND_ME);

        }else if (id == R.id.nav_globe) {
            fetchAndRefreshTrends(GLOBE);

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRefresh() {
        HomeActivity.swipeRefreshLayout.setRefreshing(false);
    }
}
