package edu.android.teamproject_whereru;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class PostWriteActivity extends AppCompatActivity {

    private EditText editTitle, editBody;
    private ImageView imageWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_write);

        editTitle = findViewById(R.id.editTitle);
        editBody = findViewById(R.id.editBody);
        imageWrite = findViewById(R.id.imageWrite);



    }

    public void postCancle(View view) {
        // TODO : 취소버튼

    }

    public void postResult(View view) {
        // TODO : 확인버튼
    }
}
