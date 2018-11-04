package edu.android.teamproject_whereru;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.android.teamproject_whereru.Model.Comment;
import edu.android.teamproject_whereru.Model.Post;

public class PostDetailActivity extends AppCompatActivity {

    public static final String KEY2 = "image_key";

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;

    private TextView textTitle, textWriter, textDate, textViews, textContent,
                     text_id, text_comment, textWritrer;

    private EditText editText;

    private ImageView imageHeart, imageView;

    private ListView listView_comment;

    private List<Comment> messages;

    private CommentListAdapter adapter;

    private static final String TBL_POST_DETAIL = "post_detail";

    // 좋아요 카운트
    private int recommendation;

    // 로그인한 사용자 아이디
    private String userName;
    private int i = 0;

    // 버튼 클릭시 Firebase에 저장
    public void btnRegist(View view) {
        String text = editText.getText().toString();
        userName = MainActivity.guestList.getGuestId();

        Log.i("test", "UserName : " + userName);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Comment comment = new Comment(userName, text);
        databaseReference.child(TBL_POST_DETAIL).child(userName).setValue(comment);

        editText.setText("");


        
    }
    class CommentListAdapter extends ArrayAdapter<Comment> {


        public CommentListAdapter(@NonNull Context context, int resource, @NonNull List<Comment> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView,@NonNull ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(PostDetailActivity.this);
                convertView = inflater.inflate(R.layout.comment_item,
                        parent, false);
            }
            TextView text_id = convertView.findViewById(R.id.text_id);
            TextView text_comment = convertView.findViewById(R.id.text_comment);

            Comment comment = getItem(position);
            Log.i("test", "getItem : " + getItem(position));
            Log.i("test", "comment : " + comment.toString());
            Log.i("test", "CommentID : " + comment.getCommentId());

            text_id.setText(MainActivity.guestList.getGuestId());
            text_comment.setText(comment.getContent());

            return convertView;
        }
    } // end class CommentListAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        textTitle = findViewById(R.id.textTitle);
        textWriter = findViewById(R.id.textWriter);
        textDate = findViewById(R.id.textDate);
        textViews = findViewById(R.id.textViews);
        textContent = findViewById(R.id.textContent);
        text_id = findViewById(R.id.text_id);
        text_comment = findViewById(R.id.text_comment);
        editText = findViewById(R.id.editText);
        imageHeart = findViewById(R.id.imageHeart);
        imageView = findViewById(R.id.imageView);

        listView_comment = findViewById(R.id.listView_comment);

        messages = new ArrayList<>();
        adapter = new CommentListAdapter(this, R.layout.comment_item, messages);
        listView_comment.setAdapter(adapter);




//        childEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Comment comment = dataSnapshot.getValue(Comment.class);
//                String id = dataSnapshot.getKey();
//                comment.setCommentId(id);
//
//                adapter.add(comment);
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                String changedId = dataSnapshot.getKey();
//                Comment changedC = dataSnapshot.getValue(Comment.class);
//
//                Comment orginal = findMessageById(changedId);
//                orginal.setCommentId(changedC.getCommentId());
//                orginal.setContent(changedC.getContent());
//                adapter.notifyDataSetChanged();
//
//                // 댓글 저장한것을 다시 불러옴
//                databaseReference = FirebaseDatabase.getInstance().getReference(TBL_POST_DETAIL);
//            }
//
//
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//                String removedId = dataSnapshot.getKey();
//                Comment comment = findMessageById(removedId);
//                adapter.remove(comment);
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
//        databaseReference.addChildEventListener(childEventListener);

        Intent intent = getIntent();

        final Post throwPost = (Post) intent.getSerializableExtra(MainActivity.START_DETAIL_ACTIVITY);


//        textWritrer.setText(post.getGuestId());
//        imageView.setImageResource(post.getImageTest());
//        String guestId, String today, String title, String image, String content, int viewCount, int recommendation;

        textWriter.setText(throwPost.getGuestId());
        textTitle.setText(throwPost.getTitle());
        // 날짜 출력
        textViews.setText(String.valueOf(throwPost.getViewCount()));
        imageHeart.setImageResource(R.drawable.h1);

        Bitmap bitmap = BitmapFactory.decodeFile(throwPost.getImage());
        imageView.setImageBitmap(bitmap);
        textContent.setText(throwPost.getContent());

    }

    private Comment findMessageById(String changedId) {
        for (Comment c : messages) {
            if (changedId.equals(c.getCommentId())) {
                return c;
            }
        }
        return null;
    }

    public void changeImage(View view) {

        Intent intent = new Intent(this, PostMainFragment.class);
        i = 1 - i;
        Post post = new Post();
        recommendation = post.getRecommendation();

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























