package edu.android.teamproject_whereru.Controller;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import edu.android.teamproject_whereru.Model.Guest;

public class GuestDao {
    private static final GuestDao ourInstance = new GuestDao();



    public static GuestDao getInstance() {
        return ourInstance;
    }

    private GuestDao() {


    }






}
