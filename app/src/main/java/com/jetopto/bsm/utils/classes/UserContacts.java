package com.jetopto.bsm.utils.classes;

public class UserContacts {

    private String name;
    private String thumbUri;
    private String phoneNumber;

    public UserContacts(String name, String uri, String number) {
        this.name = name;
        this.thumbUri = uri;
        this.phoneNumber = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbUri() {
        return thumbUri;
    }

    public void setThumbUri(String thumbUri) {
        this.thumbUri = thumbUri;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Contact { ");
        sb.append("name: " + name);
        sb.append(", phone: " + phoneNumber);
        sb.append(", thumbnail: " + thumbUri);
        sb.append("}");
        return sb.toString();
    }
}
