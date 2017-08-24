package com.cssweb.mytest.pickphoto;

import java.util.Arrays;

public class UploadTestRq extends Request {

    private String name;
    private byte[] photo;
    private byte[] photo1;
    private byte[] photo2;

    public byte[] getPhoto1() {
        return photo1;
    }

    public void setPhoto1(byte[] photo1) {
        this.photo1 = photo1;
    }

    public byte[] getPhoto2() {
        return photo2;
    }

    public void setPhoto2(byte[] photo2) {
        this.photo2 = photo2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UploadTestRq [name=");
        builder.append(name);
        builder.append(", photo=");
        builder.append(Arrays.toString(photo));
        builder.append("]");
        return builder.toString();
    }
}
