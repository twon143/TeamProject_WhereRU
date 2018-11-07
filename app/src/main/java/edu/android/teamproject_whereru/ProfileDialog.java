package edu.android.teamproject_whereru;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import edu.android.teamproject_whereru.Model.GlideApp;
import edu.android.teamproject_whereru.Model.Puppy;

public class ProfileDialog {
    // 타인의 프로필 정보를 보여주는 기능 구현(다이얼로그로)

    private Context context;
    private FirebaseDatabase database;
    private DatabaseReference pReference;
    private ChildEventListener child;
    private String postIds;
    private String writerId;
    private static final String TBL_PROFILE = "profile";
    private StorageReference storageReference;
    private FirebaseStorage storage;

    public ProfileDialog(@NonNull Context context) {
        this.context = context;
    }

    public void callFunction(String writerIds, String postIds) {
        this.writerId = writerIds;
        this.postIds = postIds;
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.profile_dialog);
        dlg.show();
        final EditText editProfileName = dlg.findViewById(R.id.editProfileName);
        editProfileName.setEnabled(false);
        final EditText editProfileAge = dlg.findViewById(R.id.editProfileAge);
        editProfileAge.setEnabled(false);
        final EditText editProfileGender = dlg.findViewById(R.id.editProfileGender);
        editProfileGender.setEnabled(false);
        Button btnProfileCancel = dlg.findViewById(R.id.btnProfileCancel);
        final ImageView imageProfile = dlg.findViewById(R.id.imageProfile);
        btnProfileCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        database = FirebaseDatabase.getInstance();
        pReference = database.getReference(TBL_PROFILE);
        storage = FirebaseStorage.getInstance();
        child = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String ids = dataSnapshot.getKey();
                if(ids.equals(writerId)) {
                    Puppy puppy = dataSnapshot.getValue(Puppy.class);
                    String image = puppy.getPuppyProfileImage();
                    storageReference = storage.getReferenceFromUrl("gs://whereru-364b0.appspot.com")
                            .child("profiles/" + image);
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            GlideApp.with(context).load(uri).into(imageProfile);
                        }
                    });
                    editProfileName.setText(puppy.getPuppyName());
                    editProfileAge.setText(puppy.getPuppyAgeOfMonth() + "살");
                    editProfileGender.setText(puppy.getPuppyGender());

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
        pReference.addChildEventListener(child);

    }
}
