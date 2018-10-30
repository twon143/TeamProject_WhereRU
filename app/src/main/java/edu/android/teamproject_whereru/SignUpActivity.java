package edu.android.teamproject_whereru;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.android.teamproject_whereru.Model.Guest;

public class SignUpActivity extends AppCompatActivity {

    private EditText editPw, editCheckPw, editName, editId, editEmail, editPhone;
    private TextView textName, textId, textPw, textCheckPw, textEmail, textPhoneNumber, textIdResilt, textPwResult;
    private Button btnSignUp, btnCheckId;

    private static final String TBL_GUEST = "guest";
    private static final String TAG = "teamproject_whereru";
    private ChildEventListener childEventListener;
    private List<String> list = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mReference;

    public static final Pattern VALID_ID_REGEX = Pattern.compile("^[a-zA-z]{1}[a-zA-z0-9]*$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_NAME = Pattern.compile("^[가-힣]{2,4}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_PHONE_REGEX = Pattern.compile("^010-[0-9]{4}-[0-9]{4}$", Pattern.CASE_INSENSITIVE);

    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("회원가입");



        final EditText editPw = findViewById(R.id.editPw);
        final EditText editCheckPw = findViewById(R.id.editCheckPw);
        final EditText editId = findViewById(R.id.editId);
        final EditText editName = findViewById(R.id.editName);
        final EditText editPhone = findViewById(R.id.editPhone);
        final EditText editEmail = findViewById(R.id.editEmail);

        textCheckPw = findViewById(R.id.textCheckPw);
        textPwResult = findViewById(R.id.textPwResult);
        textIdResilt = findViewById(R.id.textIdResult);
        btnCheckId = findViewById(R.id.btnCheckId);
        btnCheckId.setEnabled(false);


        editId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editId.getText().length() < 6 || editId.getText().length() > 12) {
                    textIdResilt.setText("아이디는 최소 6자리이상 12자리 이하로 입력");
                    textIdResilt.setTextColor(Color.RED);
                }
                else if(validateId(editId.getText().toString()) == false) {
                    textIdResilt.setText("아이디는 영어 소문자로만 입력");


                }
                else {
                    textIdResilt.setText("아이디 중복체크를 해주세요");
                    btnCheckId.setEnabled(true);
                    btnCheckId.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String id = editId.getText().toString();

                            mReference = FirebaseDatabase.getInstance().getReference(TBL_GUEST);




                        }
                    });




                }
            }
        });

      editCheckPw.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {

          }

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {

          }

          @Override
          public void afterTextChanged(Editable s) {
                if(!editCheckPw.getText().equals(editPw)) {
                    textPwResult.setText("비밀번호가 일치하지 않습니다");
                    textPwResult.setTextColor(Color.RED);
                }
                else if(editCheckPw.getText().equals(editPw)) {
                    textPwResult.setText("비밀번호가 일치합니다");
                    textPwResult.setTextColor(Color.GREEN);
                }
          }
      });

      editPw.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {

          }

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {

          }

          @Override
          public void afterTextChanged(Editable s) {
                if(editPw.length() < 8 || editPw.length() >12) {
                    textPwResult.setText("비밀번호는 8자리 이상 12자리 이하로만 입력");
                    textPwResult.setTextColor(Color.RED);
                }
          }
      });




        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setEnabled(true);
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





    // 취소버튼 onClick(); 로그인화면으로 돌아감
    public void Cancel(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void signUp(String id, String name, String pw, String phone, String email) {

        mReference = FirebaseDatabase.getInstance().getReference();

        Guest guest = new Guest(name, pw, phone, email);
        mReference.child(TBL_GUEST).child(id).setValue(guest);

        Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();

    }
    // 아이디에 대한 패턴검사
    public static boolean validateId(String idStr) {
        Matcher matcher = VALID_ID_REGEX.matcher(idStr);
        return matcher.find();
    }
    // 이메일에 관한 패턴검사
    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    // 이름에 관한 패턴검사
    public static boolean validateName(String nameStr) {
        Matcher matcher = VALID_NAME.matcher(nameStr);
        return matcher.find();
    }

    // 폰번호에 대한 패턴검사
    public static boolean validatePhone(String phoneStr) {
        Matcher matcher = VALID_PHONE_REGEX.matcher(phoneStr);
        return matcher.find();
    }


}
