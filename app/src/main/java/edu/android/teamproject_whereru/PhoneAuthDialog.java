package edu.android.teamproject_whereru;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.android.teamproject_whereru.Controller.GuestDao;

public class PhoneAuthDialog {
    // 휴대폰 인증과 관련된 다이얼로그
    interface PhoneAuthDialogCallback  {
        void requestCode(int requestCode);
    }

    private Context context;
    private String PhoneNumber;
    private GuestDao dao;
    private int min = 3;
    private int sec = 0;
    PhoneAuthDialogCallback callback;


    public PhoneAuthDialog(@NonNull Context context) {
        this.context = context;
        dao = GuestDao.getInstance();

        if (context instanceof PhoneAuthDialogCallback) {
            callback = (PhoneAuthDialogCallback) context;
        }

    }

   public void callFunction(final String phoneNumber) {
        this.PhoneNumber = phoneNumber;
        final Dialog dlg = new Dialog(context);

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dlg.setContentView(R.layout.auth_dialog);

        dlg.show();

       final EditText emailAuth_number = dlg.findViewById(R.id.emailAuth_number);
       final TextView emailAuth_time_counter = dlg.findViewById(R.id.emailAuth_time_counter);
       Button emailAuth_btn = dlg.findViewById(R.id.emailAuth_btn);
       Button btnCancelPhoneAuth = dlg.findViewById(R.id.btnCancelPhoneAuth);
       Toast.makeText(context,"생성 성공", Toast.LENGTH_SHORT).show();
       final String authNumber = dao.makePhoneAuthNumber();
       emailAuth_number.setText(authNumber);

       emailAuth_time_counter.setText(min + " : " + sec);

       final Thread thread = new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   while(true) {

                       if (min == 0 && sec == 0) {
                           emailAuth_time_counter.setText(min + " : " + sec);
                           Toast.makeText(context, "인증 대기 시간 초과!", Toast.LENGTH_LONG).show();
                           Thread.sleep(2000);
                           callback.requestCode(0);
                           dlg.dismiss();
                           break;
                       }
                       if (sec == 0) {
                           sec = 59;
                           min = min - 1;
                           emailAuth_time_counter.setText(min + " : " + sec);
                       }
                       Thread.sleep(1000);
                       sec--;
                       emailAuth_time_counter.setText(min + " : " + sec);

                   }
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       });
       thread.start();


       btnCancelPhoneAuth.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               callback.requestCode(0);
               dlg.dismiss();
           }
       });

       emailAuth_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(emailAuth_number.getText().toString().equals(authNumber)) {
                   Toast.makeText(context, "인증 성공", Toast.LENGTH_SHORT).show();
                   callback.requestCode(1);
                   dlg.dismiss();

               }
               else {
                   Toast.makeText(context, "인증 번호가 일치 하지 않습니다", Toast.LENGTH_SHORT).show();
               }
           }
       });
   }
}
