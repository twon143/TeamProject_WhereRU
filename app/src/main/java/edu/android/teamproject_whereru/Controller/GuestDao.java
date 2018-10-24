package edu.android.teamproject_whereru.Controller;

public class GuestDao {
    private static final GuestDao ourInstance = new GuestDao();

    public static GuestDao getInstance() {
        return ourInstance;
    }

    private GuestDao() {
    }
}
