package edu.android.teamproject_whereru;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PostDetailActivity extends AppCompatActivity {

    private TextView textTitle, textWritre, textDate, textTime, textRecommendation, textContent,
                     textComment;

    private ImageView imageView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        textTitle = findViewById(R.id.textTitle);
        textWritre = findViewById(R.id.textWriter);
        textDate = findViewById(R.id.textDate);
        textTime = findViewById(R.id.textTime);
        textRecommendation = findViewById(R.id.textRecommendation);
        textContent = findViewById(R.id.textContent);
        textComment = findViewById(R.id.textComment);
        imageView = findViewById(R.id.imageView);
        editText = findViewById(R.id.editText);
    }

    public void commentRegist(View view) {
        // 댓글 등록
    }

    public void IlikeIt(View view) {
        // 좋아요 클릭
    }
}
