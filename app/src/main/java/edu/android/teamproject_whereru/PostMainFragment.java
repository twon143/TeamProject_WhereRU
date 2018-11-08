package edu.android.teamproject_whereru;

// 메인화면에 커뮤니티 버튼에 리스트로 보여줄 화면

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
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

import edu.android.teamproject_whereru.Model.GlideApp;
import edu.android.teamproject_whereru.Model.Post;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostMainFragment extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference postReference;
    private ChildEventListener child;

    private PostAdapter adapter;
    private RecyclerView recyclerView;
    private static final String TBL_POST = "post";

    private Post post;

    private List<Post> postlists = new ArrayList<>();
    private PostMainCallback callback;

    class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        class PostViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;
            private TextView textGuestName, textViewCount;


            public PostViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                textGuestName = itemView.findViewById(R.id.textGuestName);
                textViewCount = itemView.findViewById(R.id.textViewCount);
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            Log.i("aaa", "onCreateViewHolder 시작");
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View itemView = inflater.inflate(R.layout.postmainitem, viewGroup, false);
            PostViewHolder holder = new PostViewHolder(itemView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
            Log.i("ddd", "onBindViewHolder()");
            final PostViewHolder holder = (PostViewHolder) viewHolder;
            Post p = postlists.get(position);

            final String image = p.getImage();

            final FirebaseStorage storage = FirebaseStorage.getInstance();

            StorageReference storageReference =
                    storage.getReferenceFromUrl("gs://whereru-364b0.appspot.com").child("images/" + image);
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    GlideApp.with(getActivity()).load(uri).into(holder.imageView);
                }
            });

            holder.textGuestName.setText("작성자 : " + p.getGuestId());
            holder.textViewCount.setText("조회수 : " + String.valueOf(p.getViewCount()));

            final String postKey = p.getPostKey();

            String guestId = p.getGuestId();
            String day = p.getToday();
            String title = p.getTitle();

            String selectImage = p.getImage();
            String content = p.getContent();
            final int viewCount = p.getViewCount();

            final Post throwPost = new Post(postKey, guestId, day, title, selectImage, content, viewCount);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 여기서는 Detail 액티비티로 넘겨줘야함
                    Map<String, Object> taskMap = new HashMap<>();
                    int temp = viewCount + 1;
                    String gId = throwPost.getGuestId();
                    String d = throwPost.getToday();
                    String t = throwPost.getTitle();
                    String sImage = throwPost.getImage();
                    String con = throwPost.getContent();
                    Post changePost = new Post(postKey, gId, d, t, sImage, con, temp);
                    taskMap.put(postKey, changePost);
                    postReference.updateChildren(taskMap);
                    // 콜백 메소드를 이용하여 모델클래스 저장
                    callback.startDetailActivity(changePost);
                    Log.i("ddd", "DetailActivity ()");

                }
            });

        }

        @Override
        public int getItemCount() {
            return postlists.size();
        }

    }

    public PostMainFragment() {
        // Required empty public constructor
    }

    public static PostMainFragment newInstance() {
        PostMainFragment instance = new PostMainFragment();
        return instance;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof PostMainCallback) {
            callback = (PostMainCallback) context;
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new PostAdapter();
        recyclerView.setAdapter(adapter);

        recyclerView.setHasFixedSize(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_main, container, false);

        database = FirebaseDatabase.getInstance();

        postReference = database.getReference(TBL_POST);

        child = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("ddd", "childAdded 실행");
                post = dataSnapshot.getValue(Post.class);
                String id = dataSnapshot.getKey();
                post.setPostKey(id);
                Log.i("ddd", post.toString());
                /* down(image); */

                postlists.add(post);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("ddd", "뷰카운트 바꾸기 실행");
                String key = dataSnapshot.getKey();
                int position = findViewCountById(key);
                postlists.get(position).setViewCount(postlists.get(position).getViewCount() + 1);
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
        postReference.addChildEventListener(child);


        return view;
    }

    @Override
    public void onDetach() {
        callback = null;
        super.onDetach();
    }

    public interface PostMainCallback {
        void startDetailActivity(Post throwPost);

    }

    private int findViewCountById(String Id) {
        for (Post p : postlists) {
            if (Id.equals(p.getPostKey())) {
                int index = postlists.indexOf(p);
                return index;
            }
        }
        return -1;
    }

}
