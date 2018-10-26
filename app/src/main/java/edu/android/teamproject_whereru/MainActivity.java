package edu.android.teamproject_whereru;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

import edu.android.teamproject_whereru.Model.Guest;
import edu.android.teamproject_whereru.Model.Post;

// 메인 액티비티
// 모델, 컨트롤러 폴더 추가(MVC 분할)
// firebase 인증에 필요한 라이브러리 추가
public class MainActivity extends AppCompatActivity implements PostMainFragment.PostMainCallback {



    private TextView textGuestLoginTest;
    private Button btnLogTest, btnLogout;
    private Button btnMapDisplay;
    public static Guest guestList;
    public static final String KEY = "detailActivity";
    private String id;
    private static final String TAG = "why";
    private FirebaseDatabase database;
    private DatabaseReference reference;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogTest = findViewById(R.id.btnLogTest);
        btnLogout = findViewById(R.id.btnLogout);
        // 로그인 테스트용
        textGuestLoginTest = findViewById(R.id.textGuestLoginTest);
        if(guestList == null) {
            textGuestLoginTest.setText("로그인 하면 정보가 보입니다");
            btnLogout.setEnabled(false);
        }
        else {
            textGuestLoginTest.setText(guestList.toString());
            btnLogout.setEnabled(true);
        }




        // BottomNavigation 뷰에 정의한 xml파일, String을 사용하여 구성함
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        btnLogTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTest = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentTest);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(guestList != null) {
                    guestList = null;
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "로그인하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });


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


}