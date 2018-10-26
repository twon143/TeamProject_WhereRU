package edu.android.teamproject_whereru;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.android.teamproject_whereru.Model.Comment;
import edu.android.teamproject_whereru.Model.Post;

public class PostDetailActivity extends AppCompatActivity {

    class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
    // 커뮤니티 확대시 보일화면
    private static final String DLG_TAG = "dlg";

    public static final String KEY1 = "comment_key";
    public static final String KEY2 = "image_key";

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    private TextView textTitle, textWritre, textDate, textTime, textContent,
                     textComment;

    private ImageView imageView, imageHeart;
    private EditText editText;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        textTitle = findViewById(R.id.textTitle);
        textWritre = findViewById(R.id.textWriter);
        textDate = findViewById(R.id.textDate);
        textTime = findViewById(R.id.textTime);
        textContent = findViewById(R.id.textContent);
        imageView = findViewById(R.id.imageView);
        imageHeart = findViewById(R.id.imageHeart);
        editText = findViewById(R.id.editText);

        Intent intent = getIntent();

        Post post = (Post) intent.getSerializableExtra(MainActivity.KEY);

        textWritre.setText(post.getGuestId());
        imageView.setImageResource(post.getImageTest());
        textContent.setText(String.valueOf(post.getContent()));

    }

    // PostWriteActivity에서 작성자, 날짜, 시간, 작성글을 받아와야 함

    public void commentRegist(View view) {
        // 댓글 등록, 삭제, 수정
        Intent intent = new Intent(this, Comment.class);
        String commentId = "";
        String postId = "";
        String guestId = "";
        String content = editText.getText().toString();

        Comment comment = new Comment(commentId, postId, guestId, content);
        textComment.setText(comment.getContent().toString());

        intent.putExtra(KEY1, comment);

        startActivity(intent);
    }

    public void changeImage(View view) {
        Intent intent = new Intent(this, PostWriteActivity.class);
        i = 1 - i;
        if (i == 0) {
            imageHeart.setImageResource(R.drawable.h1);
        } else {
            imageHeart.setImageResource(R.drawable.h2);
        }
        intent.putExtra(KEY2, i);
    }
    
}



















