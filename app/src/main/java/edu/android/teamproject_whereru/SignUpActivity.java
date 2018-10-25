package edu.android.teamproject_whereru;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.android.teamproject_whereru.Model.Guest;

public class SignUpActivity extends AppCompatActivity {

    private EditText editPw, editCheckPw, editName, editId, editEmail, editPhone;
    private TextView textCheckId, textCheckPw;
    private Button btnSignUp;

    private static final String TBL_GUEST = "guest";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mReference;
    private ChildEventListener childEventListener;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("회원가입");


        final EditText editPw = findViewById(R.id.editSpw);
        EditText editCheckPw = findViewById(R.id.editCheckPw);
        final EditText editId = findViewById(R.id.editSid);
        final EditText editName = findViewById(R.id.editName);
        final EditText editPhone = findViewById(R.id.editPhone);
        final EditText editEmail = findViewById(R.id.editEmail);

        textCheckPw = findViewById(R.id.textCheckPw);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = editId.getText().toString();
                String name = editName.getText().toString();
                String pw = editPw.getText().toString();
                String phone = editPhone.getText().toString();
                String email = editEmail.getText().toString();
                signUp(id, name, pw, phone, email);
            }
        });



    }

    // 비밀번호 유효성 검사


    // 취소버튼 onClick();
    public void Cancel(View view) {
    }

    public void signUp(String id, String name, String pw, String phone, String email) {
        mReference = FirebaseDatabase.getInstance().getReference();

        Guest guest = new Guest(name, pw, phone, email);
        mReference.child(TBL_GUEST).child(id).setValue(guest);

        Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show();



    }
}
