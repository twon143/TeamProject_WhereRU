package edu.android.teamproject_whereru;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edu.android.teamproject_whereru.Model.Guest;
import edu.android.teamproject_whereru.Model.Post;

// 메인 액티비티
// 모델, 컨트롤러 폴더 추가(MVC 분할)
// firebase 인증에 필요한 라이브러리 추가

public class MainActivity extends AppCompatActivity implements PostMainFragment.PostMainCallback, NavigationView.OnNavigationItemSelectedListener {

    public static final String START_DETAIL_ACTIVITY = "detailActivity";

    private Button btnMainLogin;
    private TextView textUserInfo;
    public static List<String> postNumberList;
    public static Guest guestList;



    private View nav_header_view;

    private BackPressCloseHandler backPressCloseHandler;


    private static final String SAVED_GUEST_DATA = "WhereRU_Guest_Data";
    private static final String GUEST_DATA = "guestData";

    // BottomNavigation 뷰 클릭에 대한 이벤트 처리
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menuitem_bottombar_home:
                    startHomeFragment();
                    return true;
                case R.id.menuitem_bottombar_location:
                    if(guestList == null) {
                        Toast.makeText(MainActivity.this, "로그인 후 사용 가능 합니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent1 = new Intent(MainActivity.this, LocationActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                    }
                    return false;
                case R.id.menuitem_bottombar_community:
                    startPostFragment();
                    return true;
                case R.id.menuitem_bottombar_service:
                    Intent intent2 = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(intent2);
                    return false;
            }
            return false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        postNumberList = new ArrayList<>();
        startHomeFragment();

        // 뒤로가기 핸들러
        backPressCloseHandler = new BackPressCloseHandler(this);

        SharedPreferences sharedPreferences = getSharedPreferences(GUEST_DATA, MODE_PRIVATE);
        if (sharedPreferences == null) {

        } else {
            Gson gson = new Gson();
            String guestData = sharedPreferences.getString(SAVED_GUEST_DATA, "");
            // 변환
            guestList = gson.fromJson(guestData, Guest.class);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        nav_header_view = navigationView.getHeaderView(0);
        textUserInfo = nav_header_view.findViewById(R.id.textUserInfo);
        btnMainLogin = nav_header_view.findViewById(R.id.btnMainLogin);
        if (guestList != null) {
            textUserInfo.setText(guestList.getGuestName() + "\t" + "님 환영합니다!");
            btnMainLogin.setEnabled(false);
        } else {
            btnMainLogin.setEnabled(true);
        }
        btnMainLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (guestList == null) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(this);


        // BottomNavigation 뷰에 정의한 xml파일, String을 사용하여 구성함
        BottomNavigationView navigation = findViewById(R.id.navigation_botton);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            backPressCloseHandler.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gps) {
            // gps 등록
            if (guestList == null) {
                Toast.makeText(this, "먼저 로그인을 하세요", Toast.LENGTH_SHORT).show();
            } else {

            }
        } else if (id == R.id.nav_mydocument) {
            startMyDocumentList();

        } else if (id == R.id.nav_memberchange) {
            if (guestList == null) {
                item.setEnabled(false);
                Toast.makeText(this, "로그인을 먼저하세요", Toast.LENGTH_SHORT).show();
            } else {
                item.setEnabled(true);
                CheckPasswordDialog cp = new CheckPasswordDialog(MainActivity.this, "비밀번호 인증",
                        "비밀번호를 입력하세요", MainActivity.guestList.getGuestPw());
                cp.show();


            }
        } else if (id == R.id.nav_logout) {
            /**
             * 정보 수정 프래그먼트
             */

            if (guestList == null) {
                Toast.makeText(this, "로그인을 먼저하세요", Toast.LENGTH_SHORT).show();
            } else if (guestList != null) {
                guestList = null;
                SharedPreferences sharedPreferences = getSharedPreferences(GUEST_DATA, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(this, "로그아웃 성공!", Toast.LENGTH_SHORT).show();
                btnMainLogin.setEnabled(true);
                textUserInfo.setText("로그인 하면 정보가 보입니다");
            }

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // 내가 쓴 글 Activity실행
    private void startMyDocumentList() {
        if (guestList != null) {
            Intent intent = new Intent(this, MyDocumentList.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        } else {
            Toast.makeText(this, "로그인을 먼저하세요", Toast.LENGTH_SHORT).show();
        }
    }

    // Community PostFragment 실행
    private void startPostFragment() {

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.mainFragment);

        if (fragment != null) {
            Fragment frag = PostMainFragment.newInstance();
            manager.beginTransaction().replace(R.id.mainFragment, frag).commit();
        }

    }

    // HomeFragment
    private void startHomeFragment() {

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.mainFragment);

        if (fragment == null) {
            Fragment frag = HomeFragment.newInstance();
            manager.beginTransaction().add(R.id.mainFragment, frag).commit();
        } else {
            Fragment frag = HomeFragment.newInstance();
            manager.beginTransaction().replace(R.id.mainFragment, frag).commit();
        }


    }

    @Override
    public void startDetailActivity(Post throwPost) {

        Intent intent = new Intent(this, PostDetailActivity.class);

        intent.putExtra(START_DETAIL_ACTIVITY, throwPost);

        startActivity(intent);
        // 화면 넘어갈때 오른쪽에서 왼쪽으로 이동하는 애니메이션 없앰
        overridePendingTransition(0,0);

    }

    //     글쓰기 FlotingButton
    public void startWriteActivity(View view) {
        if (guestList != null) {
            Intent intent = new Intent(this, PostWriteActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        } else {
            Toast.makeText(this, "로그인 후 사용 가능합니다.", Toast.LENGTH_SHORT).show();
        }

    }

    // 프로필 버튼을 Toolbar에 추가함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.myprofile_menu, menu);
        return true;
    }

    // 아이콘 클릭에 대한 이벤트리스너(다이얼 로그띄움)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (guestList != null) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        } else {
            Toast.makeText(this, "로그인을 먼저하세요", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public class BackPressCloseHandler {
        
        private long backKeyPressedTime = 0;
        private Toast toast;

        private Activity activity;

        public BackPressCloseHandler(Activity context) {
            this.activity = context;
        }

        public void onBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                toast.cancel();

                Intent t = new Intent(activity, MainActivity.class);
                activity.startActivity(t);

                activity.moveTaskToBack(true);
                activity.finish();
                android.os.Process.killProcess(android.os.Process.myPid());

            }
        }

        public void showGuide() {
            toast = Toast.makeText(activity, "'뒤로'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

}