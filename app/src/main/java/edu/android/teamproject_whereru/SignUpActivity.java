package edu.android.teamproject_whereru;

import android.app.Dialog;
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

import com.google.firebase.auth.ActionCodeSettings;
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

import edu.android.teamproject_whereru.Controller.CheckingSignupForm;
import edu.android.teamproject_whereru.Controller.GuestDao;
import edu.android.teamproject_whereru.Model.Guest;

public class SignUpActivity extends AppCompatActivity implements PhoneAuthDialog.PhoneAuthDialogCallback {
    // 회원가입과 관련된 기능처리
    private TextView textIdResilt, textPwResult, textEmailPhoneResult;
    private Button btnSignUp, btnCheckId, btnCheckEmail;
    private List<String> list = new ArrayList<>();
    private static final String TBL_GUEST = "guest";
    private static final String TAG = "teamproject_whereru";
    private ChildEventListener childEventListener;
//    private List<String> list = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mReference;
    private GuestDao dao;
    private EditText editPhone;
    private int REQ_CODE;
    private boolean NAME_STATUS, ID_STATUS, PW_STATUS, CHECK_PW_STATUS, EMAIL_STATUS, PHONE_STATUS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        dao = GuestDao.getInstance();
        setTitle("회원가입");



        final EditText editPw = findViewById(R.id.editPw);
        final EditText editCheckPw = findViewById(R.id.editCheckPw);
        final EditText editId = findViewById(R.id.editId);
        final EditText editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        final EditText editEmail = findViewById(R.id.editEmail);
        textPwResult = findViewById(R.id.textPwResult);
        textIdResilt = findViewById(R.id.textIdResilt);
        textEmailPhoneResult = findViewById(R.id.textChekcPwPhone);
        btnCheckId = findViewById(R.id.btnCheckId);

        btnCheckId.setEnabled(false);

        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(CheckingSignupForm.validateName(editName.getText().toString()) == false) {
                    textIdResilt.setTextColor(Color.RED);
                    textIdResilt.setText("정확한 이름을 입력해주세요");
                    NAME_STATUS = false;
                }
                else if(CheckingSignupForm.validateName(editName.getText().toString()) == true) {
                    textIdResilt.setTextColor(Color.GREEN);
                    textIdResilt.setText("올바른 이름 입니다");
                    NAME_STATUS = true;
                }
            }
        });
        editId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String id = editId.getText().toString();
                CheckingSignupForm check = new CheckingSignupForm();
                if(id.length() < 6 || id.length() > 12) {
                    textIdResilt.setTextColor(Color.RED);
                    textIdResilt.setText("아이디는 최소 6자리이상 12자리 이하로 입력");
                    ID_STATUS = false;
                }
                else if(CheckingSignupForm.validateId(id) == false) {
                    textIdResilt.setTextColor(Color.RED);
                }
                else if(CheckingSignupForm.validateId(editId.getText().toString()) == false) {
                    textIdResilt.setText("아이디는 영어 소문자로만 입력");
                    ID_STATUS = false;

                }
                else {
                    textIdResilt.setTextColor(Color.RED);
                    textIdResilt.setText("아이디 중복체크를 해주세요");
                    btnCheckId.setEnabled(true);;
                    mReference = FirebaseDatabase.getInstance().getReference(TBL_GUEST);
                    childEventListener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            list.add(dataSnapshot.getKey());

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
                    mReference.addChildEventListener(childEventListener);

                    btnCheckId.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                compareData(list, id);

                        }
                    });
                }
            }
        });
        // 비밀번호 형식 검사
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
                    PW_STATUS = false;
                    textPwResult.setText("비밀번호는 8자리 이상 12자리 이하로만 입력");
                    textPwResult.setTextColor(Color.RED);
                }
                else  {
                    PW_STATUS = true;
                    textPwResult.setText("사용가능한 비밀번호 입니다");
                    textPwResult.setTextColor(Color.GREEN);
                }
          }
      });
        editCheckPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textPwResult.setTextColor(Color.RED);
                if(editCheckPw.getText().toString().equals(editPw.getText().toString())) {
                    CHECK_PW_STATUS = true;
                    textPwResult.setTextColor(Color.GREEN);
                    textPwResult.setText("비밀번호가 일치합니다");
                }
                else {
                    CHECK_PW_STATUS = false;
                    textPwResult.setTextColor(Color.RED);
                    textPwResult.setText("비밀번호가 일치하지 않습니다");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String email = editEmail.getText().toString();

                boolean checkEmailResult = CheckingSignupForm.validateEmail(email);

                if(checkEmailResult == true) {
                    EMAIL_STATUS = true;
                    textEmailPhoneResult.setTextColor(Color.GREEN);
                    textEmailPhoneResult.setText("사용 가능한 이메일 입니다");

                }
                else {
                    EMAIL_STATUS = false;
                    textEmailPhoneResult.setTextColor(Color.RED);
                    textEmailPhoneResult.setText("올바르지 않는 이메일 형식 입니다");
                }

            }
        });
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setEnabled(true);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NAME_STATUS == true && ID_STATUS == true && PW_STATUS == true && CHECK_PW_STATUS == true
                        && EMAIL_STATUS == true) {
                    String id = editId.getText().toString();
                    String name = editName.getText().toString();
                    String pw = editPw.getText().toString();
                    String phone = editPhone.getText().toString();
                    String email = editEmail.getText().toString();
                    dao.signUp(id, name, pw, phone, email);

                    Toast.makeText(SignUpActivity.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, "올바르지 않는 양식이 있습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    // 취소버튼 onClick(); 로그인화면으로 돌아감
    public void cancel(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void compareData(List<String> list, String id) {

        for(String data : list) {
            if(data.equals(id)) {
                ID_STATUS = false;
                textIdResilt.setTextColor(Color.RED);
                textIdResilt.setText("중복된 아이디 입니다");
                break;
            }
            else {
                ID_STATUS = true;
                textIdResilt.setTextColor(Color.GREEN);
                
                textIdResilt.setText("사용 가능한 아이디입니다");
            }

        }
    }


    @Override
    public void requestCode(int requestCode) {
        this.REQ_CODE = requestCode;
    }
}





