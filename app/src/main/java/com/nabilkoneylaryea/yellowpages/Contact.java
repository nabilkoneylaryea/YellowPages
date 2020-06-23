package com.nabilkoneylaryea.yellowpages;

import android.net.Uri;

public class Contact {
    private String firstName, lastName, phoneNumber;
    private int id;
    private Uri img;


    Contact(Uri img, String firstName, String lastName, String phoneNumber) {
        id = -1;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.img = img;
    }

    Contact(String img, String firstName, String lastName, String phoneNumber) {
        id = -1;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.img = Uri.parse(img);
    }

    Contact() {}

    @Override
    public String toString() {
        return "Name: " + firstName + " " + lastName + " Phone Number: " + phoneNumber;
    }

    public String toString2() {
        return firstName + " " + lastName + " " + phoneNumber;
    }

    public boolean hasImage() {
        return img != null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String number) {
        this.phoneNumber = number;
    }

    public Uri getImg() {
        return img;
    }

    public void setImg(Uri img) {
        this.img = img;
    }
}
