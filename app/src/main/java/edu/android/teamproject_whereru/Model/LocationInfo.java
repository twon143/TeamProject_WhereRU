package edu.android.teamproject_whereru.Model;

// ---------------- database 에서 동물 위치 정보 받아오기 위해 사용하는 클래스

public class LocationInfo {
    private double Latitude;
    private double Longitude;

    public LocationInfo() {
    }

    public LocationInfo(double latitude, double longitude) {
        Latitude = latitude;
        Longitude = longitude;
    }

    @Override
    public String toString() {
        return "LocationInfo{" +
                "Latitude=" + Latitude +
                ", Longitude=" + Longitude +
                '}';
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
