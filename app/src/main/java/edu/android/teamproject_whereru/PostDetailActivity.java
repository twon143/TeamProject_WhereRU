package edu.android.teamproject_whereru;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import edu.android.teamproject_whereru.Controller.PostDetailDao;
import edu.android.teamproject_whereru.Model.Comment;
import edu.android.teamproject_whereru.Model.Post;

public class PostDetailActivity extends AppCompatActivity {

    class PostDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        class PostDetailViewHolder extends RecyclerView.ViewHolder {

            private TextView text_id, text_comment;

            public PostDetailViewHolder(@NonNull View itemView) {
                super(itemView);
                text_id = itemView.findViewById(R.id.text_id);
                text_comment = itemView.findViewById(R.id.text_comment);

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
            Comment c =  dao.getCommentList().get(position);

            holder.text_id.setText(c.getCommentId());
            // 작성한 댓글 아이디
            holder.text_comment.setText(c.getContent());
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

    private TextView textTitle, textWritre, textDate, textViews, textContent, textViewCount,
                     text_comment, text_id;

    private ImageView imageView, imageHeart;
    private EditText editText;
    public static String commentId ="2";

    private PostDetailAdapter adapter;



    int i = 0;
    int g = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);


        textTitle = findViewById(R.id.textTitle);
        textWritre = findViewById(R.id.textWriter);
        textDate = findViewById(R.id.textDate);
        textViews = findViewById(R.id.textViews);
        textContent = findViewById(R.id.textContent);
        imageView = findViewById(R.id.imageView);
        imageHeart = findViewById(R.id.imageHeart);
        editText = findViewById(R.id.editText);
        textViewCount = findViewById(R.id.textViewCount);
        dao = PostDetailDao.getInstance();




        Intent intent = getIntent();

        Post post = (Post) intent.getSerializableExtra(MainActivity.START_DETAIL_ACTIVITY);

        textWritre.setText(post.getGuestId());
//        imageView.setImageResource(post.getImageTest());
        textContent.setText(String.valueOf(post.getContent()));



        imageHeart.setImageResource(R.drawable.h1);

        recyclerView_comment = findViewById(R.id.recyclerView_comment);
        recyclerView_comment.setLayoutManager(new LinearLayoutManager(PostDetailActivity.this));

        adapter = new PostDetailAdapter();
        recyclerView_comment.setAdapter(adapter);

        recyclerView_comment.setHasFixedSize(true);


        Button btnRegist = findViewById(R.id.btnRegist);
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(PostDetailActivity.this, PostDetailActivity.class);


               String content = editText.getText().toString();
               editText.setText("");

               Comment comment = new Comment(commentId, content);
               Post dCreated = new Post();

//               intent.putExtra(KEY1, comment);



                view = getLayoutInflater().inflate(R.layout.comment_item, null, false);
                text_id = view.findViewById(R.id.text_id);
                text_comment = view.findViewById(R.id.text_comment);

                text_id.setText(comment.getCommentId());
                text_comment.setText(comment.getContent());
//                textDate.setText(dCreated.getdCreated());


                int result = dao.insert(comment);

                //adapter.notifyItemInserted(0);
                if(result == 1) {
                    initRecyclerView();
                }
            }
        });




    }

    // PostWriteActivity에서 작성자, 날짜, 시간, 작성글을 받아와야 함


    private void initRecyclerView() {
        adapter.notifyDataSetChanged();
        recyclerView_comment.invalidate();
    }

    public void changeImage(View view) {
        Intent intent = new Intent(this, PostMainFragment.class);
        i = 1 - i;
        Post post = new Post();
        int recommendation = post.getRecommendation();

        if (i == 0) {
            imageHeart.setImageResource(R.drawable.h1);
            Toast.makeText(this, "좋아요 취소", Toast.LENGTH_LONG).show();
            recommendation--;


        } else {
            imageHeart.setImageResource(R.drawable.h2);
            Toast.makeText(this, "좋아요", Toast.LENGTH_LONG).show();
            recommendation++;
        }
        intent.putExtra(KEY2, i);
    }
    
}



















