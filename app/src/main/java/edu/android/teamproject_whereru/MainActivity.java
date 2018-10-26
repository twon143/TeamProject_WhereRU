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

import java.io.Serializable;

import edu.android.teamproject_whereru.Model.Post;

// 메인 액티비티
// 모델, 컨트롤러 폴더 추가(MVC 분할)
// firebase 인증에 필요한 라이브러리 추가
public class MainActivity extends AppCompatActivity implements PostMainFragment.PostMainCallback{

    private TextView textLogInfo;
    private Button btnLogTest;
    private Button btnMapDisplay;

    public static final String KEY = "detailActivity";

    private static final String TAG = "why";

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




        // BottomNavigation 뷰에 정의한 xml파일, String을 사용하여 구성함
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        btnLogTest = findViewById(R.id.btnLogTest);
        btnLogTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTest = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentTest);
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