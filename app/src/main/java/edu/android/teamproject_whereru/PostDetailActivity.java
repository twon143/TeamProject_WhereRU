package edu.android.teamproject_whereru;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.android.teamproject_whereru.Controller.PostDetailDao;
import edu.android.teamproject_whereru.Model.Comment;
import edu.android.teamproject_whereru.Model.Post;

public class PostDetailActivity extends AppCompatActivity {

    class PostDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        class PostDetailViewHolder extends RecyclerView.ViewHolder {

            private TextView text_id, text_comment;

            public PostDetailViewHolder(@NonNull View itemView) {
                super(itemView);
                text_id = (TextView) itemView.findViewById(R.id.text_id);
                text_comment = (TextView) itemView.findViewById(R.id.text_comment);

            }
        } // end class PostDetailViewHolder

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            LayoutInflater inflater = LayoutInflater.from(PostDetailActivity.this);

            View itemView = inflater.inflate(R.layout.comment_item, viewGroup, false);

            PostDetailViewHolder holder = new PostDetailViewHolder(itemView);


            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

            PostDetailViewHolder holder = (PostDetailViewHolder) viewHolder;
            final Comment c =  dao.getCommentList().get(position);
            Log.i(TAG, "here " + text_id.toString());
            Log.i(TAG, "here: "  + c.getCommentId());
            Log.i(TAG, "holder: " + holder.toString());
            holder.text_id.setText(commentId);
            // 작성한 댓글 아이디
            holder.text_comment.setText(content);
            // 작성한 댓글 Body

        }

        @Override
        public int getItemCount() {
            return dao.getCommentList().size();
        }
    }
    // 커뮤니티 확대시 보일화면
    private static final String DLG_TAG = "dlg";
    private static final String TAG = "teamproject";

    public static final String KEY1 = "comment_key";
    public static final String KEY2 = "image_key";
    private View view;
    private PostDetailDao dao;
    private RecyclerView recyclerView_comment;
    private LinearLayoutManager linearLayoutManager;

    private TextView textTitle, textWritre, textDate, textTime, textContent,
                     text_comment, text_id;

    private ImageView imageView, imageHeart;
    private EditText editText;
    public static  String commentId = "2";
    public static String  content;




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
        dao = PostDetailDao.getInstance();




        Intent intent = getIntent();

        Post post = (Post) intent.getSerializableExtra(MainActivity.KEY);

        textWritre.setText(post.getGuestId());
        imageView.setImageResource(post.getImageTest());
        textContent.setText(String.valueOf(post.getContent()));

        Button btnRegist = findViewById(R.id.btnRegist);
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                content = editText.getText().toString();

               Comment comment = new Comment(commentId, content);


//        intent.putExtra(KEY1, comment);




                recyclerView_comment = findViewById(R.id.recyclerView_comment);

                view = getLayoutInflater().inflate(R.layout.comment_item, null, false);
                text_id = view.findViewById(R.id.text_id);
                text_comment = view.findViewById(R.id.text_comment);
                TextView textGuestName = view.findViewById(R.id.textGuestName);
                Log.i(TAG, "text_id: " + text_id);
                Log.i(TAG, "guestName" + textGuestName);
                text_id.setText(comment.getCommentId());
                // null 값이 뜬다네 commentId 로그찍어바
                text_comment.setText(comment.getContent());


                recyclerView_comment.setLayoutManager(new LinearLayoutManager(PostDetailActivity.this));

                PostDetailAdapter adapter = new PostDetailAdapter();
                recyclerView_comment.setAdapter(adapter);

                recyclerView_comment.setHasFixedSize(true);
            }
        });



    }

    // PostWriteActivity에서 작성자, 날짜, 시간, 작성글을 받아와야 함



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



















