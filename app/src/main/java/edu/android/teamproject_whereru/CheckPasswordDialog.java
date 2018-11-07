package edu.android.teamproject_whereru;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.android.teamproject_whereru.Model.Guest;


public class CheckPasswordDialog extends Dialog {


    private String title;
    private String content;
    private String guestPw;
    private Context context;
    private Guest guest;


    public CheckPasswordDialog(@NonNull Context context, String title, String content, String guestPw) {

        super(context);
        this.title = title;
        this.content = content;
        this.guestPw = guestPw;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.checkpassword_dialog);
        TextView textTitle = findViewById(R.id.textTitle);
        TextView textContent = findViewById(R.id.textContent);
        final EditText editCheckPw = findViewById(R.id.editCheckedPassword);
        Button btnPwOk = findViewById(R.id.btnPwOk);
        Button btnPwCancel = findViewById(R.id.btnPwCancel);
        textTitle.setText(title);
        textContent.setText(content);

        btnPwOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkPw = editCheckPw.getText().toString();
                if (checkPw.equals(guestPw)) {
                    Intent intent = new Intent(getContext(), UpdateUserData.class);
                    getContext().startActivity(intent);
                    dismiss();
                }
                else {
                    Toast.makeText(getContext(), "비밀번호가 다릅니다", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnPwCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }
}
