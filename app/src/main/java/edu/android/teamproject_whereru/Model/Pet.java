package edu.android.teamproject_whereru.Model;

public class Pet {

    private String petId;
    private String petName;
    private String guestId;
    private int gpsNo;

    public Pet() {}

    public Pet(String petId, String petName, String guestId, int gpsNo) {
        this.petId = petId;
        this.petName = petName;
        this.guestId = guestId;
        this.gpsNo = gpsNo;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public int getGpsNo() {
        return gpsNo;
    }

    public void setGpsNo(int gpsNo) {
        this.gpsNo = gpsNo;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "petId='" + petId + '\'' +
                ", petName='" + petName + '\'' +
                ", guestId='" + guestId + '\'' +
                ", gpsNo=" + gpsNo +
                '}';
    }

}
