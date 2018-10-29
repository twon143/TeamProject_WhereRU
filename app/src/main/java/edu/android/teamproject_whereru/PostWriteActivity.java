package edu.android.teamproject_whereru;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PostWriteActivity extends AppCompatActivity {

    private static final int CAMERA_CODE = 111;

    private EditText editTitle, editBody;
    private ImageView imageWrite;
    private TextView writeGuestName, writeToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_write);

        editTitle = findViewById(R.id.editTitle);
        editBody = findViewById(R.id.editBody);
        imageWrite = findViewById(R.id.imageWrite);

        writeGuestName = findViewById(R.id.writeGuestName);
        writeToday = findViewById(R.id.writeToday);


    }

    public void postCancel(View view) {
        // TODO : 취소버튼

    }

    public void postResult(View view) {
        // TODO : 확인버튼
    }


    public void addPhoto(View view) {
        // TODO : 사진 추가 버튼 암시적 인텐트로 갤러리 화면 열기



    }
}
