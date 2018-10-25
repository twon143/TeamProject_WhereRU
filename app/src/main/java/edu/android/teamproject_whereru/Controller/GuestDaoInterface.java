package edu.android.teamproject_whereru.Controller;

import java.util.List;

import edu.android.teamproject_whereru.Model.Guest;

public interface GuestDaoInterface {
    // TODO: 리턴 타입 정확히 정해야함

    boolean signUpGuest(List<Guest> guestList);
    String findId(Guest id);
    String findPw(Guest password);


}
