package edu.android.teamproject_whereru.Model;

public class Puppy {
    private String guestId;
    private String puppyName;
    private String puppyGender;
    private String puppyAgeOfMonth;
    private String puppyKind;
    private String puppyProfileImage;
    private boolean puppyWhetherNeutral;

    public Puppy(String puppyName, String puppyGender, String puppyAgeOfMonth, String puppyKind, String puppyProfileImage, boolean puppyWhetherNeutral) {
        this.puppyName = puppyName;
        this.puppyGender = puppyGender;
        this.puppyAgeOfMonth = puppyAgeOfMonth;
        this.puppyKind = puppyKind;
        this.puppyProfileImage = puppyProfileImage;
        this.puppyWhetherNeutral = puppyWhetherNeutral;
    }

    public Puppy() {}
    // primary key
    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getPuppyName() {
        return puppyName;
    }

    public void setPuppyName(String puppyName) {
        this.puppyName = puppyName;
    }

    public String getPuppyGender() {
        return puppyGender;
    }

    public void setPuppyGender(String puppyGender) {
        this.puppyGender = puppyGender;
    }

    public String getPuppyAgeOfMonth() {
        return puppyAgeOfMonth;
    }

    public void setPuppyAgeOfMonth(String puppyAgeOfMonth) {
        this.puppyAgeOfMonth = puppyAgeOfMonth;
    }

    public String getPuppyKind() {
        return puppyKind;
    }

    public void setPuppyKind(String puppyKind) {
        this.puppyKind = puppyKind;
    }

    public String getPuppyProfileImage() {
        return puppyProfileImage;
    }

    public void setPuppyProfileImage(String puppyProfileImage) {
        this.puppyProfileImage = puppyProfileImage;
    }

    public boolean getPuppyWhetherNeutral() {
        return puppyWhetherNeutral;
    }

    public void setPuppyWhetherNeutral(boolean puppyWhetherNeutral) {
        this.puppyWhetherNeutral = puppyWhetherNeutral;
    }

    @Override
    public String toString() {
        return "Puppy{" +
                "guestId='" + guestId + '\'' +
                ", puppyName='" + puppyName + '\'' +
                ", puppyGender='" + puppyGender + '\'' +
                ", puppyAgeOfMonth='" + puppyAgeOfMonth + '\'' +
                ", puppyKind='" + puppyKind + '\'' +
                ", puppyProfileImage='" + puppyProfileImage + '\'' +
                ", puppyWhetherNeutral='" + puppyWhetherNeutral + '\'' +
                '}';
    }
}
