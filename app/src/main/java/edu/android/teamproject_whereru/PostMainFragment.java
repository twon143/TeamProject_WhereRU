package edu.android.teamproject_whereru;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.android.teamproject_whereru.Model.Post;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostMainFragment extends Fragment {

    class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        class PostViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;
            private TextView textGuestName, textLikeCount;


            public PostViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                textGuestName = itemView.findViewById(R.id.textGuestName);
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
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            PostViewHolder holder = (PostViewHolder) viewHolder;

            // 저장되어있는 Post 모델클래스의 생성자를 불러와서
            // image, 작성자이름, 좋아요 카운트

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 여기서는 Detail 액티비티로 넘겨줘야함


                }
            });

        }

        @Override
        public int getItemCount() {
            // PostDao 클래스 만들고 나서 ArrayList에 저장되어있는 갯수 꺼내고
            // Firebase에 저장되어 있는 객체들 리스트 만큼

            return 0;
        }
    }

    private PostAdapter adapter;
    private List<Post> posts;
    private RecyclerView recyclerView;

    private FirebaseDatabase database;
    private DatabaseReference postreference;
    private ChildEventListener childEventListener;


    public PostMainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


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

        posts = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        database = FirebaseDatabase.getInstance();
        postreference = database.getReference().child(""); // 저장되어있는 이름 꺼내기


        adapter = new PostAdapter();
        recyclerView.setAdapter(adapter);

        recyclerView.setHasFixedSize(true);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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



    }
}
