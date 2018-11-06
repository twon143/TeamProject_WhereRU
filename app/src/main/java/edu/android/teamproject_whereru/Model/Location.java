package edu.android.teamproject_whereru.Model;

import java.io.Serializable;

public class Location implements Serializable{
    private String name;
    private String address;
    private String status;
    private String phone;

    public Location() {
    }

    public Location(String name, String address, String status, String phone) {
        this.name = name;
        this.address = address;
        this.status = status;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", status='" + status + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
