package edu.android.teamproject_whereru.Controller;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import edu.android.teamproject_whereru.Model.Comment;
import edu.android.teamproject_whereru.PostDetailActivity;

public class PostDetailDao {
    private static PostDetailDao instance = null;
    private List<Comment> commentList;

    private static final String TAG = "teamproject";
    private PostDetailDao() {
        commentList = new ArrayList<>();
        makeDummyData();
    }

    private void makeDummyData() {
        Log.i(TAG, "commentId:  " + PostDetailActivity.commentId);
        commentList.add(new Comment("1", "제발"));
        commentList.add(new Comment("2", "성공"));
        commentList.add(new Comment("3", "가즈아"));
    }

    public  static PostDetailDao getInstance() {
        if (instance == null) {
            instance = new PostDetailDao();
        }
        return instance;
    }
    public List<Comment> getCommentList() {

        return commentList;
    }
}
















