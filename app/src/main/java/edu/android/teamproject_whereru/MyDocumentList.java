package edu.android.teamproject_whereru;



import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.android.teamproject_whereru.Model.Post;

public class MyDocumentList extends AppCompatActivity {

    private ListView MDListView;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_document_list);

        Intent intent = getIntent();


        MDListView = findViewById(R.id.MDListView);

        MDListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startPostDetailActivity();
            }
        });

        // 어댑터 등록
        dataSetting();
    }

    private void startPostDetailActivity() {
        Intent intent = new Intent(this, PostDetailActivity.class);
        startActivity(intent);
    }

    private void dataSetting() {
        final DocumentAdapter adapter = new DocumentAdapter();

        /* 리스트뷰에 어댑터 등록 */
        MDListView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();

        databaseReference = database.getReference().child("Document");

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Post post = dataSnapshot.getValue(Post.class);
                String title = dataSnapshot.getKey();
                post.setTitle(title);

                
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












