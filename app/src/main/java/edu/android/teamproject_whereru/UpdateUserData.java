package edu.android.teamproject_whereru;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.android.teamproject_whereru.Model.Guest;

public class UpdateUserData extends AppCompatActivity {
    private EditText editGetGuestEmail, editGetGuestPassword, editGetGuestPhoneNo;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private static final String TBL_GUEST = "guest";
    private static final String GUEST_DATA = "guestData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_data);
        editGetGuestEmail = findViewById(R.id.editGetGuestEmail);
        editGetGuestPassword = findViewById(R.id.editGetGuestPw);
        editGetGuestPhoneNo = findViewById(R.id.editGuestPhoneNo);

        editGetGuestEmail.setText(MainActivity.guestList.getGuestEmail());
        editGetGuestPassword.setText(MainActivity.guestList.getGuestPw());
        editGetGuestPhoneNo.setText(MainActivity.guestList.getPhoneNo());
    }

    public void SaveUserData(View view) {
        Toast.makeText(this, "회원정보 변경 성공", Toast.LENGTH_SHORT).show();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference(TBL_GUEST);
        String changePhone = editGetGuestPhoneNo.getText().toString();
        String changeEmail = editGetGuestEmail.getText().toString();
        final String changePw = editGetGuestPassword.getText().toString();
        Guest guest =  new Guest(MainActivity.guestList.getGuestName(), changePw, changePhone, changeEmail);
        reference.child(MainActivity.guestList.getGuestId()).setValue(guest);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("aaa", "실행 ");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("aaa", "비번: " + MainActivity.guestList.getGuestPw());
                String id = dataSnapshot.getKey();
                if(id.equals(MainActivity.guestList.getGuestId())) {
                    String newPw = MainActivity.guestList.getGuestPw();
                    MainActivity.guestList.setGuestPw(newPw);
                }
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
        reference.addChildEventListener(childEventListener);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void ReturnToMainAcitivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
