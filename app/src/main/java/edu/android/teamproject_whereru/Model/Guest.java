package edu.android.teamproject_whereru.Model;

public class Guest {

    private String guestId; // primary key
    private String guestName;
    private String guestPw;
    private String phoneNo;
    private String guestEmail;


    public Guest(){}

    public Guest(String guestName, String guestPw, String phoneNo, String guestEmail) {
        this.guestName = guestName;
        this.guestPw = guestPw;
        this.phoneNo = phoneNo;
        this.guestEmail = guestEmail;
    }

    public Guest(String phoneNo, String guestEmail, String guestPw) {
        this.guestPw = guestPw;
        this.phoneNo = phoneNo;
        this.guestEmail = guestEmail;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getGuestPw() {
        return guestPw;
    }

    public void setGuestPw(String guestPw) {
        this.guestPw = guestPw;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "guestId='" + guestId + '\'' +
                ", guestPw='" + guestPw + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", guestEmail='" + guestEmail + '\'' +
                '}';
    }
}
