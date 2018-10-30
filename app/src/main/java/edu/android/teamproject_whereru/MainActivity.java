package edu.android.teamproject_whereru;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.Serializable;

import edu.android.teamproject_whereru.Model.Guest;
import edu.android.teamproject_whereru.Model.Post;

// 메인 액티비티
// 모델, 컨트롤러 폴더 추가(MVC 분할)
// firebase 인증에 필요한 라이브러리 추가
public class MainActivity extends AppCompatActivity implements PostMainFragment.PostMainCallback, NavigationView.OnNavigationItemSelectedListener {



    private Button btnLogout, btnMainLogin;
    private TextView textUserInfo;

    private Button btnMapDisplay;
    public static Guest guestList;
    public static final String KEY = "detailActivity";
    private String id;
    private static final String TAG = "why";
    private static final String TAG2 = "teamproject_whereru";
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private boolean gps;
    private LayoutInflater layoutInflater;
    private View nav_header_view;
    private NavigationView navigationView;




    private static final String SAVED_GUEST_DATA = "WhereRU_Guest_Data";
    private static final String GUEST_DATA = "guestData";
    @Override
    protected void onStart() {
        super.onStart();




    }

    // BottomNavigation 뷰 클릭에 대한 이벤트 처리
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menuitem_bottombar_location:
                    return true;
                case R.id.menuitem_bottombar_community:
                    return true;
                case R.id.menuitem_bottombar_service:
                    return true;
            }
            return false;
        }
    };

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(GUEST_DATA, MODE_PRIVATE);
        Log.i(TAG2, "SharedPre: " + sharedPreferences.toString());
        if(sharedPreferences == null) {

        } else {
            Gson gson = new Gson();
            Log.i(TAG2, "Main sharedPrefernces:  " + sharedPreferences.toString());
            String guestData = sharedPreferences.getString(SAVED_GUEST_DATA, "");
            Log.i(TAG2, "Main GuestData:  " + guestData);
            // 변환
            guestList = gson.fromJson(guestData, Guest.class);

        }


        // 로그인 테스트용


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gps == false){
                    Snackbar.make(view, "GPS가 켜졌습니다", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    gps = true;
                }else{
                    Snackbar.make(view, "GPS가 꺼졌습니다", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    gps = false;
                }
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        nav_header_view = navigationView.getHeaderView(0);
        textUserInfo = nav_header_view.findViewById(R.id.textUserInfo);
        btnMainLogin = nav_header_view.findViewById(R.id.btnMainLogin);
        if(guestList != null) {
            textUserInfo.setText(guestList.toString());
            btnMainLogin.setEnabled(false);
        }
        else {

        }
        navigationView.setNavigationItemSelectedListener(this);



        // BottomNavigation 뷰에 정의한 xml파일, String을 사용하여 구성함
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_botton);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);




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





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gps) {
            // Handle the camera action
        } else if (id == R.id.nav_gpsinfo) {

        } else if (id == R.id.nav_mydocument) {

        } else if (id == R.id.nav_memberchange) {

        } else if (id == R.id.nav_logout) {
            if(guestList == null) {
                Toast.makeText(this, "로그인을 먼저하세요", Toast.LENGTH_SHORT).show();
            }
            else if(guestList != null) {
                guestList = null;
                SharedPreferences sharedPreferences = getSharedPreferences(GUEST_DATA, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(this, "로그아웃 성공!", Toast.LENGTH_SHORT).show();
                textUserInfo.setText("로그인 하면 정보가 보입니다");
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Community 테스트 메소드
    public void startPostFragment(View view) {

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.testFragment);

        if (fragment == null) {
            Fragment frag = PostMainFragment.newInstance();
            manager.beginTransaction().add(R.id.testFragment, frag).commit();
        }
    }


    @Override
    public void startDetailActivity(Post post) {

        Log.i(TAG, "startDetailActivity 실행");

        Intent intent = new Intent(this, PostDetailActivity.class);

        intent.putExtra(KEY, post);

        startActivity(intent);
    }


    public void userLogin(View view) {

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}