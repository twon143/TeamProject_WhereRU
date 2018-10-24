package edu.android.teamproject_whereru.Model;

public class Guest {

    private String guestId;
    private String guestPw;
    private String phoneNo;
    private String guestEmail;


    public Guest(){}

    public Guest(String guestId, String guestPw, String phoneNo, String guestEmail) {
        this.guestId = guestId;
        this.guestPw = guestPw;
        this.phoneNo = phoneNo;
        this.guestEmail = guestEmail;
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
