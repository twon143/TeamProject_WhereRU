package edu.android.teamproject_whereru;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileDialog {

    private Context context;
    private Button btnChangeProfile;
    private static final int REQ_CODE = 1;


    public ProfileDialog(@NonNull Context context) {
        this.context = context;
    }

    public void callFunction() {
        final Dialog dlg = new Dialog(context);

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dlg.setContentView(R.layout.profile_dialog);

        dlg.show();
        btnChangeProfile = dlg.findViewById(R.id.btnChangeProfile);
//        btnChangeProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ActivityCompat.requestPermissions(, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
//                , REQ_CODE);
//            }
//        });

    }


}