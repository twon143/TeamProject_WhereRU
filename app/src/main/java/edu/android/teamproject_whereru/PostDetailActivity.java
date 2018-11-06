package edu.android.teamproject_whereru;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.android.teamproject_whereru.Model.Comment;
import edu.android.teamproject_whereru.Model.GlideApp;
import edu.android.teamproject_whereru.Model.Post;

public class PostDetailActivity extends AppCompatActivity {

    public static final String KEY2 = "image_key";

    private static final String TAG = "tag";

    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private Comment comment;
    private TextView textTitle, textWriter, textDate, textViews, textContent,
                     text_id, text_comment, textWritrer;

    private EditText editText;
    private Post detailPost;
    private ImageView imageHeart, imageView;

    private ListView listView_comment;

    private List<Comment> messages;

    private CommentListAdapter adapter;

    private static final String TBL_POST_DETAIL = "post_detail";
    private static final String TBL_POST = "post";

    // 좋아요 카운트
    private int recommendation;

    // 로그인한 사용자 아이디
    private String userName;
    private int i = 0;


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
            Log.i("test", "getItem :  " + getItem(position));
            Log.i("test", "comment : " + comment.toString());
            Log.i("test", "CommentID : " + comment.getCommentId());

            text_id.setText(comment.getCommentId());
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

        Intent intent = getIntent();
        final Post throwPost = (Post) intent.getSerializableExtra(MainActivity.START_DETAIL_ACTIVITY);
        detailPost = throwPost;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String selectImage = throwPost.getImage();
        StorageReference storageReference =
                storage.getReferenceFromUrl("gs://whereru-364b0.appspot.com")
                        .child("images/" + selectImage);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                GlideApp.with(PostDetailActivity.this).load(uri).into(imageView);
                Toast.makeText(PostDetailActivity.this, "이미지 다운 성공", Toast.LENGTH_SHORT).show();
            }
        });

        textWriter.setText(throwPost.getGuestId());
        textTitle.setText(throwPost.getTitle());
        textDate.setText(throwPost.getToday());
        textViews.setText(String.valueOf(throwPost.getViewCount()));
        imageHeart.setImageResource(R.drawable.h1);
        textContent.setText(throwPost.getContent());
        Log.i("aaa", detailPost.getPostKey());
        databaseReference = FirebaseDatabase.getInstance().getReference(TBL_POST_DETAIL).child(detailPost.getPostKey());

        adapter = new CommentListAdapter(this, R.layout.comment_item, messages);

        listView_comment.setAdapter(adapter);
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                comment = dataSnapshot.getValue(Comment.class);
                messages.add(comment);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addChildEventListener(childEventListener);

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

    // 버튼 클릭시 Firebase에 저장
    public void btnRegist(View view) {
        String text = editText.getText().toString();
        userName = MainActivity.guestList.getGuestId();
        Log.i("test", "UserName : " + userName);
        databaseReference = FirebaseDatabase.getInstance().getReference(TBL_POST_DETAIL).child(detailPost.getPostKey());
        final Comment comment = new Comment(userName, text);
        databaseReference.push().setValue(comment);
        editText.setText("");




    }
}























