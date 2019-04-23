package customer.app.driveaze.driveaze;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {


    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 18f;

    private LatLng currentlatLng;
    private Location currentLocation;

    //vars
    private Boolean mRequstingLocationUpdates =  true;
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient client;
    private LocationRequest request;
    private LocationServices services;

    private Button btnCurrentLocation;
    private Double latitude, logitude;
    private TextView submitrequest;
    private RelativeLayout servicelayout;
    private View bottomSheet;
    private Toolbar mTopToolbar;
    private BottomSheetBehavior mBottomSheetBehavior;
    private LocationCallback locationCallback;
    private ImageView movemarker;
    private Marker currentMarker;
    private LinearLayout layoutService1;
    private NavigationView navigationView;
    private DrawerLayout drawerLayoutNav;
    private ListView mDrawerListItem;
    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        client = LocationServices.getFusedLocationProviderClient(this);

        mTopToolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);

        movemarker = findViewById(R.id.map_marker_move);
        submitrequest =findViewById(R.id.textSubmitRequest);
        servicelayout =  findViewById(R.id.service_layout);

        bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        btnCurrentLocation = findViewById(R.id.currentLocationText);
        layoutService1 = findViewById(R.id.service1);
        drawerLayoutNav = findViewById(R.id.navdrawer);
        navigationView =  findViewById(R.id.nav_view);
        mDrawerListItem = findViewById(R.id.nav_drawer_list_view);

        layoutService1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MapActivity.this, VehicleDetailActivity.class);
                startActivity(intent);
            }
        });


        DrawerNavItem[] drawerNavItems = new DrawerNavItem[7];

        drawerNavItems[0] = new DrawerNavItem(R.drawable.home, "Home");
        drawerNavItems[1] = new DrawerNavItem(R.drawable.home, "My Service Requests");
        drawerNavItems[2] = new DrawerNavItem(R.drawable.home, "Emergency Contacts");
        drawerNavItems[3] = new DrawerNavItem(R.drawable.home, "Terms and Conditions");
        drawerNavItems[4] = new DrawerNavItem(R.drawable.home, "App Suggestions");
        drawerNavItems[5] = new DrawerNavItem(R.drawable.home, "Contact Us");
        drawerNavItems[6] = new DrawerNavItem(R.drawable.home, "Logout");


        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_drawer_item, drawerNavItems);
        mDrawerListItem.setAdapter(adapter);
        mDrawerListItem.setOnItemClickListener(new DrawerItemClickListener());

        drawerToggle = new ActionBarDrawerToggle(this,drawerLayoutNav,mTopToolbar,R.string.open_drawer,R.string.close_drawer){
            //We can perform a particular action when the
            // Navigation View is opened by overriding the
            // onDrawerOpened() method.
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //Toast.makeText(getApplicationContext(),"Drawer Opened",Toast.LENGTH_SHORT).show();
            }

            //We can perform a particular action when the
            // Navigation View is closed by overriding the
            // onDrawerClosed() method.
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //Toast.makeText(getApplicationContext(),"Drawer Closed",Toast.LENGTH_SHORT).show();
            }
        };

        //Finally setting up the drawer listener for DrawerLayout
        drawerLayoutNav.setDrawerListener(drawerToggle);

        //Sync State of the navigation icon on the toolbar
        // with the drawer when the drawer is opened or closed.
        drawerToggle.syncState();

        getLocationPermission();

        locationCallback = new LocationCallback(){
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data

                    // ...
                }
            };
        };


        submitrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (BottomSheetBehavior.STATE_COLLAPSED == 4) {

                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                } else {

                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        });


        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:

                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:

                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:

                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:

                        break;
                    case BottomSheetBehavior.STATE_SETTLING:

                        break;
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            selectItem(position);
            drawerLayoutNav.closeDrawers();

        }

    }

    private void selectItem(int position){

        // Toast.makeText(getApplicationContext(), "Item Clicked " + mNavigationDrawerItemTitles[position]  , Toast.LENGTH_SHORT).show();

        switch (position){
            case 0:
                Intent intent = new Intent(getApplicationContext(), VehicleDetailActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        client = LocationServices
                .getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {

                final Task location = client.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Log.d(TAG, "onComplete: found location!");
                            currentLocation = (Location) task.getResult();

                            latitude = currentLocation.getLatitude();
                            logitude = currentLocation.getLongitude();

                            currentlatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                            moveCamera(currentlatLng,
                                    DEFAULT_ZOOM);
                            showToast("Current Location");

                            createLocationRequest();


                            MarkerOptions options = new MarkerOptions();
                            options.position(currentlatLng);
                            options.title("Current Location");
                            mMap.addMarker(options);


                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }


    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        btnCurrentLocation.setText(getAddress(latitude,logitude,getApplicationContext()));

    }

    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);


        }
    }

    private void createLocationRequest() {
        request = new LocationRequest().create();
        request.setInterval(100);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        client.removeLocationUpdates(locationCallback);
    }

    public String getAddress(double lat, double lng, Context mContext) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            String add = "";
            for (int i = 0; i < addresses.size(); i++) {
                Address obj = addresses.get(i);
                //String = obj.getAddressLine(i);
                add = add + obj.getAddressLine(i) + "," + obj.getLocality() + "," + obj.getAdminArea() + "," + obj.getCountryName();

                Log.v("IGA", "\n" + "Address " + add);

            }
            return add;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        client.requestLocationUpdates(request,
                locationCallback,
                null /* Looper */);

//        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
//            @Override
//            public void onCameraMove() {
//                LatLng center = mMap.getCameraPosition().target;
//
//                if (btnCurrentLocation.getText().toString().equals("") &&  currentMarker != null){
//                    currentMarker.remove();
//                    MarkerOptions m2 = new MarkerOptions();
//                    m2.title("Current Location");
//                    m2.position(center);
//                    currentMarker = mMap.addMarker(m2);
//                    currentlatLng = currentMarker.getPosition();
//                    btnCurrentLocation.setText(getAddress(latitude, logitude,getApplicationContext()));
//                }else {
//                    movemarker.setVisibility(View.VISIBLE);
//                }
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRequstingLocationUpdates) {
            startLocationUpdates();
        }

    }
}
