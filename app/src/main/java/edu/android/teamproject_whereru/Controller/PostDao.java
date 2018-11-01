package edu.android.teamproject_whereru.Controller;

import java.util.ArrayList;
import java.util.List;

import edu.android.teamproject_whereru.Model.Post;
import edu.android.teamproject_whereru.R;

public class PostDao {

    private static PostDao instance = null;
    private List<Post> postList;

    private PostDao(){
        postList = new ArrayList<>();

        makeDummyData();
    }

    private void makeDummyData() {
        final int[] imageIds = {
                R.drawable.d1, R.drawable.d2, R.drawable.d3, R.drawable.d4, R.drawable.d6
        };

        for (int i = 0; i< 100; i++) {
//            Post p = new Post(imageIds[i % imageIds.length], "GuestId"+i, i);
//            postList.add(p);
        }

    }

    public static PostDao getInstance() {
        if (instance == null) {
            instance = new PostDao();


        }
        return instance;
    }

    public List<Post> getPostList(){
        return postList;
    }


}
