package edu.android.teamproject_whereru;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.android.teamproject_whereru.Model.Guest;

public class UpdateUserData extends AppCompatActivity {
    private EditText editGetGuestEmail, editGetGuestPassword, editGetGuestPhoneNo;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private static final String TBL_GUEST = "guest";

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
        reference = database.getReference(TBL_GUEST).child(MainActivity.guestList.getGuestId());
        String changePhone = editGetGuestEmail.getText().toString();
        String changeEmail = editGetGuestPassword.getText().toString();
        String changePw = editGetGuestPhoneNo.getText().toString();
        Guest guest =  new Guest(MainActivity.guestList.getGuestName(), changePhone, changeEmail, changePw);
        reference.setValue(guest);


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
