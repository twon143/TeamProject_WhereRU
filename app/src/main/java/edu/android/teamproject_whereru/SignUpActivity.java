package edu.android.teamproject_whereru;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {

    private EditText editPw, editCheckPw;
    private TextView textCheckId, textCheckPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("회원가입");

        EditText editPw = findViewById(R.id.editPw);
        EditText editCheckPw = findViewById(R.id.editCheckPw);

        textCheckPw = findViewById(R.id.textCheckPw);

        checkValidatePassword();
    }

    // 비밀번호 유효성 검사
    private void checkValidatePassword() {
        String pw = editPw.getText().toString();
      if(pw != null) {
          if (pw.length() > 8 && pw.length() < 12) {
              textCheckPw.setText("사용가능한 비밀번호 입니다");
          } else {

          }
      }
    }

    // 취소버튼 onClick();
    public void Cancel(View view) {
    }
    // 가입 버튼
    public void Register(View view) {
    }
}
