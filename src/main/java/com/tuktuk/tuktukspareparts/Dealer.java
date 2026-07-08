package com.tuktuk.tuktukspareparts;

public class Dealer {
    private String code;
    private String name;
    private String contactNumber;
    private String location;

    public Dealer(String code, String name, String contactNumber, String location) {
        this.code = code;
        this.name = name;
        this.contactNumber = contactNumber;
        this.location = location;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
