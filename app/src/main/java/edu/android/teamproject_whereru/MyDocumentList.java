package edu.android.teamproject_whereru;



import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.android.teamproject_whereru.Model.Post;

public class MyDocumentList extends AppCompatActivity {

    class DocumentListAdapter extends ArrayAdapter<Post> {

        public DocumentListAdapter(@NonNull Context context,
                                   int resource,
                                   @NonNull List<Post> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(MyDocumentList.this);
                convertView = inflater.inflate(R.layout.my_document_item, parent, false);
            }

            TextView textTitle_1 = convertView.findViewById(R.id.textTitle_1);
            TextView textDate_1 = convertView.findViewById(R.id.textDate_1);
            TextView textViews_1 = convertView.findViewById(R.id.textViews_1);

            Post post = getItem(position);

            textTitle_1.setText(post.getTitle());
            textDate_1.setText(post.getToday());
            textViews_1.setText(String.valueOf(post.getViewCount()));


            return  convertView;
        }
    }

    private ListView MDListView;
    private List<Post> messages;
    private Post post;

    private DocumentListAdapter adapter;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;

    public static final String POST_LIST = "post";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_document_list);

        messages = new ArrayList<>();


        MDListView = findViewById(R.id.MDListView);


        Intent intent = getIntent();

        databaseReference = FirebaseDatabase.getInstance().getReference(POST_LIST);

        adapter = new DocumentListAdapter(this, R.layout.my_document_item, messages);
        MDListView.setAdapter(adapter);

        // 로그인 아이디와 포스트 작성자 아이디와 일치할 경우, 리스트뷰에 아이템 추가
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                
                post = dataSnapshot.getValue(Post.class);
                if (MainActivity.guestList.getGuestId().equals(post.getGuestId())) {
                    messages.add(post);
                    adapter.notifyDataSetChanged();
                }
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
        databaseReference.addChildEventListener(childEventListener);


    } // end onCreate



    } // end class MyDocumentList













