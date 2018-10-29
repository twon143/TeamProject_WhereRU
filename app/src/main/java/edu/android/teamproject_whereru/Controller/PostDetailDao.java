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

    public int insert(Comment comment) {
        int result = 1;



        commentList.add(comment);

        return result;
    }
}
















