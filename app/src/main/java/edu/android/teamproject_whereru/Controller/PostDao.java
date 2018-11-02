package edu.android.teamproject_whereru.Controller;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import edu.android.teamproject_whereru.Model.GlideApp;
import edu.android.teamproject_whereru.Model.Post;
import edu.android.teamproject_whereru.R;

public class PostDao {

    private static PostDao instance = null;
    private List<Post> postList = new ArrayList<>();
    private Post post;
    private static final String TBL_POST = "post";

    private PostDao(){
        Log.i("aaa", "PostDao 생성자 시작");
//        makeDummyData();

    }

    private void makeDummyData() {
        Log.i("aaa", "makeDummyData 시작");
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference postReference = database.getReference(TBL_POST);

        ChildEventListener child = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("aaa", "onChildAdded 시작");
                //Log.i("aaa", "childAdded 실행");
                post = dataSnapshot.getValue(Post.class);
                String id = dataSnapshot.getKey();
                post.setPostKey(id);
                String image = post.getImage();
                /* down(image); */
                postList.add(post);


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
        postReference.addChildEventListener(child);
        Log.i("aaa", "dummyData 끝");
    }

    public static PostDao getInstance() {
        if (instance == null) {
            instance = new PostDao();


        }
        return instance;
    }

    public List<Post> getPostList(){
        Log.i("aaa", "getPostList 시작");
            makeDummyData();
        Log.i("aaa", "makeDummyData()끝, 리턴 전");
            return postList;

    }


}
