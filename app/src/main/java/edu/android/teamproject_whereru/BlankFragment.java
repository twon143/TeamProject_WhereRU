package edu.android.teamproject_whereru;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.android.teamproject_whereru.Model.Guest;


/**
 * A simple {@link Fragment} subclass.
 */

/**
 * 정보 수정 할 때 프래그먼트로 만들었음
 */
public class BlankFragment extends Fragment {



    private View v;
    private Button btnUpdate;
    private EditText editChangePhone,editUpdateEmail,editUpdatePw;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private static final String TBL_GUEST = "guest";

    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.update_frame,container,false);

        editChangePhone = v.findViewById(R.id.editChangePhone);
        editUpdateEmail = v.findViewById(R.id.editUpdateEmail);
        editUpdatePw = v.findViewById(R.id.editUpdatePw);

        editChangePhone.setText(MainActivity.guestList.getPhoneNo());
        editUpdateEmail.setText(MainActivity.guestList.getGuestEmail());
        editUpdatePw.setText(MainActivity.guestList.getGuestPw());

        btnUpdate = v.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference(TBL_GUEST).child(MainActivity.guestList.getGuestId());
                String changePhone = editChangePhone.getText().toString();
                String changeEmail = editUpdateEmail.getText().toString();
                String changePw = editUpdatePw.getText().toString();
                Guest guest =  new Guest(changePhone, changeEmail, changePw);

                reference.setValue(guest);
            }
        });

        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("finishstatus",true);
                startActivity(intent);
                getActivity().finish();

                return true;
            }
        });



        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();


    }
}
