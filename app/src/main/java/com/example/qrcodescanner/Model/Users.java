package com.example.qrcodescanner.Model;

public class Users {
    private String Name,Password,Location,ID;

    public Users()
    {

    }

    public Users(String name, String password, String location, String ID) {
        Name = name;
        Password = password;
        Location = location;
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
