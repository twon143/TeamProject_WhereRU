package edu.android.teamproject_whereru;

// 메인 화면 리스트에서 이미지나 아이템을 클릭했을 때 자세히 보여지는 화면

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
    // 기타
    private static final String TAG = "tag";
    // Firebase
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private Comment comment;
    private Post detailPost;
    // 테이블 이름
    private static final String TBL_POST_DETAIL = "post_detail";
    private static final String TBL_PROFILE = "profile";
    // UI들
    private TextView textTitle, textWriter, textDate, textViews, textContent,
            text_id, text_comment, textWritrer;
    private EditText editText;
    private ImageView imageHeart, imageView;
    private ListView listView_comment;
    private CommentListAdapter adapter;
    // List<T>
    private List<String> profileKeys;
    private List<Comment> messages;


    private String userName;
    private boolean hasProfileKey;

    public void showProfile(View view) {
        final String writerIds = detailPost.getGuestId();
        final String postIds = detailPost.getPostKey();
        hasProfileKey = hasProfile(writerIds);
        if (hasProfileKey == true) {
            ProfileDialog profileDialog = new ProfileDialog(this);
            profileDialog.callFunction(writerIds, postIds);
        } else {
            Toast.makeText(PostDetailActivity.this, "프로필 정보가 없는 유저입니다", Toast.LENGTH_SHORT).show();
        }
    }

    public void showCommentProfile(View view) {
        // 댓글쓴 사람 눌럿을때 프로필 보이기
        final String commentIds = comment.getCommentId();
        final String postIds = detailPost.getPostKey();
        hasProfileKey = hasProfile(commentIds);
        if (hasProfileKey == true) {
            ProfileDialog profileDialog = new ProfileDialog(this);
            profileDialog.callFunction(commentIds, postIds);
        } else {
            Toast.makeText(PostDetailActivity.this, "프로필 정보가 없는 유저입니다", Toast.LENGTH_SHORT).show();
        }
    }
    // 프로필 관련

    class CommentListAdapter extends ArrayAdapter<Comment> {


        public CommentListAdapter(@NonNull Context context, int resource, @NonNull List<Comment> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

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
    }
    // ListView Setting

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        getProfileKey();
        textTitle = findViewById(R.id.textTitle);
        textWriter = findViewById(R.id.textWriter);
        textDate = findViewById(R.id.textDate);
        textViews = findViewById(R.id.textViews);
        textContent = findViewById(R.id.textContent);
        text_id = findViewById(R.id.text_id);
        text_comment = findViewById(R.id.text_comment);
        editText = findViewById(R.id.editText);
        imageView = findViewById(R.id.imageView);
        listView_comment = findViewById(R.id.listView_comment);
        messages = new ArrayList<>();
        profileKeys = new ArrayList<>();

        /*editText.setFocusable(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setFocusable(true);
            }
        });*/
        // 필요한 UI들 찾음


        // PostMainFragment 에서 클릭한 아이템을 PostDetailActivity 에서 화면 보여주기
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
            }
        });

        textWriter.setText("작성자 : " + throwPost.getGuestId());
        textTitle.setText("제목 : " + throwPost.getTitle());
        textDate.setText("날짜 : " + throwPost.getToday());
        textViews.setText("조회수 : " + String.valueOf(throwPost.getViewCount()));
        textContent.setText("내용 : " + throwPost.getContent());

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
                Log.i(TAG, "ChildChange 호출");
                adapter.notifyDataSetChanged();
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

    // 버튼 클릭시 Firebase에 저장
    public void btnRegist(View view) {
        if (MainActivity.guestList != null) {
            String text = editText.getText().toString();
            if (!text.equals("")) {
                userName = MainActivity.guestList.getGuestId();
                Log.i("test", "UserName : " + userName);
                databaseReference = FirebaseDatabase.getInstance().getReference(TBL_POST_DETAIL).child(detailPost.getPostKey());
                final Comment comment = new Comment(userName, text);
                databaseReference.push().setValue(comment);
                editText.setText("");
            } else {
                Toast.makeText(this, "댓글이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "로그인 후 사용 가능 합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    // 프로필 테이블의 KEY값을 리턴해주는메소드
    public void getProfileKey() {
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference(TBL_PROFILE);
        ChildEventListener child = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                profileKeys.add(dataSnapshot.getKey());
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
        dr.addChildEventListener(child);
    }

    public boolean hasProfile(String id) {
        boolean hasProfile = false;
        if (profileKeys != null) {
            for (String profileKey : profileKeys) {
                if (!profileKey.equals(detailPost.getGuestId())) {
                    hasProfile = false;
                } else if (profileKey.equals(detailPost.getGuestId())) {
                    hasProfile = true;
                }
            }
        }

        return hasProfile;
    }

}























