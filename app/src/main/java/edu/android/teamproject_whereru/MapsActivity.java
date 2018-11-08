package edu.android.teamproject_whereru;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;


import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.ClusterRenderer;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import edu.android.teamproject_whereru.Model.MyItem;
import noman.googleplaces.NRPlaces;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

// ------------------------------ 프로젝트 - 서비스 기능( 현재 위치 조회, 주변 장소 조회, 특정 장소 조회 등)을 포함하는 클래스

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, PlacesListener {

    private static final String NAVER_SEARCH = "https://m.search.naver.com/search.naver?query=";
    private static final int PLACE_PICKER_REQUEST = 1;

    private static final String TAG = "edu.android.maps";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 10 * 1000;  // 10초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 5000; // 5초

    // onRequestPermissionsResult에서 수신된 결과에서 ActivityCompat.requestPermissions를 사용한 퍼미션 요청을 구별하기 위해 사용됩니다.
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;

    // 앱을 실행하기 위해 필요한 퍼미션을 정의합니다.
    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    Location mCurrentLocation;
    LatLng currentPosition;

    private GoogleMap mMap = null;
    private Marker currentMarker = null;

    // 위치 정보(최근 위치, 주기적 업데이트 시작/취소)와 관련된 클래스
    private FusedLocationProviderClient locationClient;
    // 주기적 위치 업데이트를 요청할 때 설정 정보를 저장하는 클래스
    private LocationRequest locationRequest;
    // 주기적 위치 정보를 처리하는 콜백
    private LocationCallback locationCallback;

    private Location location;

    private View mLayout;  // Snackbar 사용하기 위해서는 View가 필요합니다.
    // (참고로 Toast에서는 Context가 필요했습니다.)

    List<Marker> previous_marker = null;

    Button btnSearchBank;
    Button btnSearchCafe;
    Button btnSearchHospital;
    ImageButton place_picker;

    List<edu.android.teamproject_whereru.Model.Location> list = null;
    private ClusterManager<MyItem> mClusterManager;
    private boolean flag = false;

    ProgressBar progressBar;

    class ParseUrlTask extends AsyncTask<String, MyItem, String> {

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection connection = null;
            InputStream in = null;
            try {
                // 1. 웹 주소 문자열을 사용해서 URL 인스턴스 생성
                URL url = new URL(strings[0]);
                // 2. URL connection을 맺음
                connection = (HttpURLConnection) url.openConnection();
                // 3. URL 연결 설정(optional - 선택사항)
                // 요청을 보낸 후 서버로부터 응답을 기다리는 시간
                connection.setConnectTimeout(3000); // 단위: ms
                // 응답을 읽는 InputStream의 대기 시간
                connection.setReadTimeout(3000);
                // 요청 방식 설정
                connection.setRequestMethod("GET");

                // 4. 서버와 연결
                connection.connect();

                // 5. 서버에서 보낸 응답 코드를 확인
                int responceCode = connection.getResponseCode();
                in = connection.getInputStream();
                if (responceCode == HttpURLConnection.HTTP_OK || responceCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                    // 서버에서 정상적으로 응답이 도착한 경우
                    list = parseLocationXml(in);
                    Log.i(TAG, "list size: " + list.size());

                    //TODO: 파싱한 위치데이터를 맵에 마커로 표시해줘야 함.
                    Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());

                    List<Address> addresses = new ArrayList<>();

                    for (edu.android.teamproject_whereru.Model.Location location : list) {

                        try {
                            addresses = geocoder.getFromLocationName(location.getAddress(), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        LatLng latLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());

                        MyItem item = new MyItem(latLng.latitude, latLng.longitude, location.getName(), location.getAddress(), PlaceType.HOSPITAL);
                        publishProgress(item);

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            mMap.clear();
            mClusterManager.clearItems();
            progressBar.setVisibility(View.VISIBLE);
            setButtonEnabled(false);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG, "onPostExecute: " + s);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(MapsActivity.this, "loading complete", Toast.LENGTH_SHORT).show();
            setButtonEnabled(true);

        }


        @Override
        protected void onProgressUpdate(MyItem... values) {
            mClusterManager.addItem(values[0]);
            mClusterManager.cluster();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getSupportActionBar().hide();

        progressBar = findViewById(R.id.loadingProgressBar);
        progressBar.setVisibility(View.GONE);

        PlaceAutocompleteFragment placeAutocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Location location = new Location("");
                location.setLatitude(place.getLatLng().latitude);
                location.setLongitude(place.getLatLng().longitude);

                setCurrentLocation(location, place.getName().toString(), place.getAddress().toString());
            }

            @Override
            public void onError(Status status) {

            }
        });

        btnSearchBank = findViewById(R.id.btnSearchBank);
        btnSearchBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition == null) {
                    Toast.makeText(MapsActivity.this, "Tracing Your Location..", Toast.LENGTH_SHORT).show();
                } else {
                    showPlaceInformation(currentPosition, PlaceType.BANK);
                }
            }
        });

        btnSearchCafe = findViewById(R.id.btnSearchCafe);
        btnSearchCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition == null) {
                    Toast.makeText(MapsActivity.this, "Tracing Your Location..", Toast.LENGTH_SHORT).show();
                } else {
                    showPlaceInformation(currentPosition, PlaceType.CAFE);
                }

            }
        });

        btnSearchHospital = findViewById(R.id.btnSearchHospital);
        btnSearchHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo info = manager.getActiveNetworkInfo();*/

                if (currentPosition == null) {
                    Toast.makeText(MapsActivity.this, "Tracing Your Location..", Toast.LENGTH_SHORT).show();
                } else {
                    ParseUrlTask task = new ParseUrlTask();
                    task.execute("http://openapi.gangnam.go.kr:8088/534d55444474776f3734424d6b6b43/xml/GnAnimalHospital/1/40");
                }

            }
        });


        place_picker = findViewById(R.id.place_picker);
        place_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(MapsActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


        previous_marker = new ArrayList<>();

        mLayout = findViewById(R.id.layout_main);

        // 주기적인 위치 정보 업데이트 설정
        createLocationRequest();

        // Google Play Service에 포함된
        // FusedLocationProviderClient 객체를 가져옴
        locationClient = LocationServices.getFusedLocationProviderClient(this);


        // LocationCallback 객체 생성
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                // 주기적인 위치 정보를 요청했을 때
                // ART이 위치 정보를 주기적으로 전달할 때 호출되는 콜백 메소드
                // GPS에서 전달받은 위치 정보
                super.onLocationResult(locationResult);

                List<Location> locationList = locationResult.getLocations();

                if (locationList.size() > 0) {
                    location = locationList.get(locationList.size() - 1);

                    currentPosition
                            = new LatLng(location.getLatitude(), location.getLongitude());


                    String markerTitle = getCurrentAddress(currentPosition);
                    String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                            + " 경도:" + String.valueOf(location.getLongitude());


                    //현재 위치에 마커 생성하고 이동
                    setCurrentLocation(location, markerTitle, markerSnippet);

                    mCurrentLocation = location;
                }
            }
        };

        /*// 위치 정보 권한(permission)이 허용됐는 지 체크
        checkLocationPermission();*/

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private String getCurrentAddress(LatLng latlng) {
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0);
        }

    }

    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {


        if (currentMarker != null) {
            currentMarker.remove();
        }


        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);

        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.marker);
        Bitmap b = drawable.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 150, 150, false);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

        markerOptions.draggable(true);

        currentMarker = mMap.addMarker(markerOptions);
        if (!flag) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
            mMap.moveCamera(cameraUpdate);
            flag = true;
        }


//        mMap.animateCamera(cameraUpdate);

    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {

            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        } else {

            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);


            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }


            Log.d(TAG, "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");

            locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            if (checkPermission()) {

                mMap.setMyLocationEnabled(true);
            }

        }

    }

    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    private boolean checkPermission() {

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;

    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d(TAG, "onMapReady :");

        mMap = googleMap;

        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
        //지도의 초기위치를 서울로 이동
        setDefaultLocation();

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            startLocationUpdates(); // 3. 위치 업데이트 시작


        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                        ActivityCompat.requestPermissions(MapsActivity.this, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }


        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//
//            @Override
//            public void onMapClick(LatLng latLng) {
//
//                Log.d(TAG, "onMapClick :");
//            }
//        });

        setUpClusterer();

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart");

        if (checkPermission()) {

            Log.d(TAG, "onStart : call mFusedLocationClient.requestLocationUpdates");
            locationClient.requestLocationUpdates(locationRequest, locationCallback, null);

            if (mMap != null) {
                mMap.setMyLocationEnabled(true);

            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (locationClient != null) {

            Log.d(TAG, "onStop : call stopLocationUpdates");
            locationClient.removeLocationUpdates(locationCallback);
        }
    }

    public void setDefaultLocation() {


        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";


        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15f);
        mMap.moveCamera(cameraUpdate);
//        mMap.animateCamera(cameraUpdate);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {

                // 퍼미션을 허용했다면 위치 업데이트를 시작합니다.
                startLocationUpdates();
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {


                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();

                } else {


                    // "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();
                }
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d(TAG, "onActivityResult : GPS 활성화 되있음");

                        needRequest = true;

                        return;
                    }
                }

                break;

            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);
                    String toastMsg = String.format("Place: %s", place.getName());
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    @Override
    public void onPlacesFailure(PlacesException e) {

    }

    @Override
    public void onPlacesStart() {

        setButtonEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPlacesSuccess(final List<noman.googleplaces.Place> places) {

        runOnUiThread(new Runnable() {

            @Override

            public void run() {

                mClusterManager.clearItems();

                for (noman.googleplaces.Place place : places) {

                    LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
                    String markerSnippet = getCurrentAddress(latLng);

                    mClusterManager.addItem(new MyItem(latLng.latitude, latLng.longitude, place.getName(), markerSnippet, place.getTypes()[0]));

                }

                Toast.makeText(MapsActivity.this, "loading complete", Toast.LENGTH_SHORT).show();
                mClusterManager.cluster();

            }

        });


    }

    @Override
    public void onPlacesFinished() {
        progressBar.setVisibility(View.GONE);
        setButtonEnabled(true);

    }

    public void showPlaceInformation(LatLng location, String type) {
        mMap.clear();//지도 클리어

        if (previous_marker != null)
            previous_marker.clear();//지역정보 마커 클리어

        new NRPlaces.Builder()
                .listener(MapsActivity.this)
                .key("AIzaSyA_AOGIkHlZccFDHvk9umgtg93Ze1403ys")
                .latlng(location.latitude, location.longitude)//현재 위치
                .radius(500) //500 미터 내에서 검색
                .type(type) //타입지정( ex)PlaceType.Cafe)
                .build()
                .execute();
    }

    private List<edu.android.teamproject_whereru.Model.Location> parseLocationXml(InputStream inputStream) throws XmlPullParserException, IOException {
        List<edu.android.teamproject_whereru.Model.Location> list = new ArrayList<>();

        String currentTag = null;
        String name = null, address = null, status = null, phone = null;
        boolean bSet = false;

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();

        parser.setInput(inputStream, null);

        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    currentTag = parser.getName();
                    if (currentTag.equals("WRKP_NM") | currentTag.equals("TRD_STATE_GBN_CTN") | currentTag.equals("SITE_ADDR") | currentTag.equals("SITE_TEL")) {
                        bSet = true;
                    }
                    break;
                case XmlPullParser.TEXT:
                    if (bSet) {
                        switch (currentTag) {
                            case "WRKP_NM":
                                name = parser.getText();
                                break;
                            case "TRD_STATE_GBN_CTN":
                                status = parser.getText();
                                break;
                            case "SITE_ADDR":
                                address = parser.getText();
                                break;
                            case "SITE_TEL":
                                phone = parser.getText();
                                break;
                        }
                    }

                    break;
                case XmlPullParser.END_TAG:
                    currentTag = parser.getName();
                    bSet = false;
                    if (currentTag != null && currentTag.equals("row")) {
                        list.add(new edu.android.teamproject_whereru.Model.Location(name, address, status, phone));
                    }

                    break;

            }

            eventType = parser.next();
        }

        return list;
    }

    private void setUpClusterer() {

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(this, mMap);

        mClusterManager.setRenderer(new DefaultClusterRenderer<MyItem>(this, mMap, mClusterManager) {

            private SparseArray<BitmapDescriptor> mIcons = new SparseArray<>();
            private IconGenerator mIconGenerator = new IconGenerator(MapsActivity.this);


            @Override
            protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
                if (item.getType().equals(PlaceType.HOSPITAL)) {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital_small_marker));

                } else if (item.getType().equals(PlaceType.BANK)) {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bank_small_marker));

                } else if (item.getType().equals(PlaceType.CAFE)) {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.cafe_small_marker));

                }
            }

            /*@Override
            protected void onBeforeClusterRendered(Cluster<MyItem> cluster, MarkerOptions markerOptions) {
                super.onBeforeClusterRendered(cluster, markerOptions);
                mIconGenerator = new IconGenerator(MapsActivity.this);
                mIconGenerator.setContentView(makeCustomTextView(MapsActivity.this));
                int bucket = getBucket(cluster);
                BitmapDescriptor descriptor = mIcons.get(bucket);
                if(descriptor == null){
                    //TODO

                }
            }*/


        });

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(NAVER_SEARCH + marker.getTitle()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

    }

    private void setButtonEnabled(boolean flag) {
        btnSearchBank.setEnabled(flag);
        btnSearchHospital.setEnabled(flag);
        btnSearchCafe.setEnabled(flag);
        place_picker.setEnabled(flag);
    }

}
