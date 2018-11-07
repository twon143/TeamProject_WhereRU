package edu.android.teamproject_whereru.Controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.android.teamproject_whereru.LoginActivity;
import edu.android.teamproject_whereru.Model.Guest;

public class GuestDao {
    // 회원과 관련된 기능을 구현한 클래스
    private static final GuestDao ourInstance = new GuestDao();
    private DatabaseReference mReference;
    private static final String TBL_GUEST = "guest";
    private ChildEventListener childEventListener;
    private List<String> list = new ArrayList<>();
    private static final String TAG = "teamproject_whereru";
    private int otherNumber;

    public static GuestDao getInstance() {
        return ourInstance;
    }

    private GuestDao() {


    }

    public void signUp(String id, String name, String pw, String phone, String email) {

        mReference = FirebaseDatabase.getInstance().getReference();

        Guest guest = new Guest(name, pw, phone, email);
        mReference.child(TBL_GUEST).child(id).setValue(guest);


    }

    public List<String> checkId(String id) {



        return list;
    }

    public String makePhoneAuthNumber() {
        Random random = new Random();
        int firstNumber = random.nextInt(9)+1;
       for(;;) {
           int otherNumber = random.nextInt(999)+1;
           if(otherNumber >= 100) {
               this.otherNumber = otherNumber;
               break;
           }
       }
       String firstNumberStr = String.valueOf(firstNumber);
       String otherNumberStr = String.valueOf(otherNumber);
       String phoneAuthNumber = firstNumberStr + otherNumberStr;
        return phoneAuthNumber;
    }


}
