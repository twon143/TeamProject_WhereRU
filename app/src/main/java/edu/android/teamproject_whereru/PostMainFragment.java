package edu.android.teamproject_whereru;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import edu.android.teamproject_whereru.Controller.PostDao;
import edu.android.teamproject_whereru.Model.GlideApp;
import edu.android.teamproject_whereru.Model.Post;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostMainFragment extends Fragment {

    class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        class PostViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;
            private TextView textGuestName, textViewCount, textLikeCount;


            public PostViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                textGuestName = itemView.findViewById(R.id.textGuestName);
                textViewCount = itemView.findViewById(R.id.textViewCount);
                textLikeCount = itemView.findViewById(R.id.textLikeCount);

            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View itemView = inflater.inflate(R.layout.postmainitem, viewGroup, false);
            PostViewHolder holder = new PostViewHolder(itemView);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
            final PostViewHolder holder = (PostViewHolder) viewHolder;

            // 저장되어있는 Post 모델클래스의 생성자를 불러와서
            // image, 작성자이름, 좋아요 카운트

            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            FirebaseStorage storage = FirebaseStorage.getInstance();

//            PostWriteActivity image = new PostWriteActivity();
//            final String mainImage = image.getImage();

            DatabaseReference postReference = database.getReference(TBL_POST);
//            StorageReference storageReference =
//                    storage.getReferenceFromUrl("gs://whereru-364b0.appspot.com").child("images/"+ mainImage);
////            GlideApp.with(getActivity())
////                    .load(storage)
////                    .into(holder.imageView);
//
//            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//
//                    GlideApp.with(getActivity()).load(uri).into(holder.imageView);
////                    Glide.with(getActivity()).load(uri).into(holder.imageView);
////                    holder.imageView.setImageURI(uri);
//                    Log.i("aaa", "uri : " + uri);
//                }
//            });

//            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//            Glide.with(getActivity()).load("gs://whereru-364b0.appspot.com").into(holder.imageView);

            ChildEventListener child = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    //Log.i("aaa", "childAdded 실행");
                    post = dataSnapshot.getValue(Post.class);
                    String id = dataSnapshot.getKey();
                    post.setPostKey(id);
                    
                    String image = post.getImage();

                    down(image);

                    holder.textGuestName.setText(post.getGuestId());
                    // 날짜처리
                    holder.textViewCount.setText(String.valueOf(post.getViewCount()));
                    holder.textLikeCount.setText(String.valueOf(post.getRecommendation()));
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

                private void down (String main){
//                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    FirebaseStorage storage = FirebaseStorage.getInstance();

//                    PostWriteActivity image = new PostWriteActivity();
//                    final String mainImage = image.getImage();

//                    DatabaseReference postReference = database.getReference(TBL_POST);
                    StorageReference storageReference =
                            storage.getReferenceFromUrl("gs://whereru-364b0.appspot.com").child("images/"+ main);
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
//                            uriList.add(uri);
                            GlideApp.with(getActivity()).load(uri).into(holder.imageView);
//                            holder.imageView.setImageURI(uri);
                            Log.i("aaa", "uri : " + uri);
                        }
                    });
                }

            };
            postReference.addChildEventListener(child);
//            final Post p = postList.getPostList().get(position);

            // PostDetailActivity로 보내기 위해 post 모델클래스에 저장
//            int imageTest = p.getImageTest();
//            String guestId = p.getGuestId();
//            int recommendation = p.getRecommendation();

//            final Post post = new Post(imageTest, guestId, recommendation);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 여기서는 Detail 액티비티로 넘겨줘야함
                    Toast.makeText(getActivity(), "실험 완료", Toast.LENGTH_SHORT).show();
                    // 콜백 메소드를 이용하여 모델클래스 저장
                    callback.startDetailActivity(post);
                }
            });

        }




        @Override
        public int getItemCount() {
            // PostDao 클래스 만들고 나서 ArrayList에 저장되어있는 갯수 꺼내고
            // Firebase에 저장되어 있는 객체들 리스트 만큼

            // 이거 왜 안되냐고
//            uriList.size();
            return 10;
        }


    }



    private PostAdapter adapter;
    private PostDao postList;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    private static final String TBL_POST = "post";

    private FirebaseDatabase database;
    private DatabaseReference postreference;
    private ChildEventListener childEventListener;
    private Post post;
    private List<Uri> uriList;

    public interface PostMainCallback {
        void startDetailActivity(Post post);
    }


    private PostMainCallback callback;


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
    public void onDetach() {
        callback = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_post_main, container, false);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();

        postList = PostDao.getInstance();

        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        database = FirebaseDatabase.getInstance();
        postreference = database.getReference(); // 저장되어있는 이름 꺼내기


        adapter = new PostAdapter();
        recyclerView.setAdapter(adapter);

        recyclerView.setHasFixedSize(true);

    }
}
