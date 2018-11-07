package edu.android.teamproject_whereru.Controller;



import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import edu.android.teamproject_whereru.Model.Comment;


public class PostDetailDao {
    // 댓글목록들을 리턴해주는 클래스
    private static PostDetailDao instance = null;
    private List<Comment> commentList;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;




    private static final String TAG = "teamproject";

    private PostDetailDao() {
        commentList = new ArrayList<>();


    }


    public static PostDetailDao getInstance() {
        if (instance == null) {
            instance = new PostDetailDao();
            //firebase
        }
        return instance;
    }

    public List<Comment> getCommentList() {

        return commentList;
    }

    public int insert(Comment comment) {
        int result = 1;



        commentList.add(comment);

        return result;
    }
}
















