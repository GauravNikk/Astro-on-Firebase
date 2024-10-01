package com.cilguru.model;


public class User {
    String Displayname;


    String Email,udob,umob;
    long createdAt;

    public User (){};
    public User(String displayname, String email,String udob,String umob, long createdAt){
        this.Displayname=displayname;
       this.Email=email;
        this.createdAt=createdAt;
        this.udob=udob;
        this.umob=umob;
    }


    public String getDisplayname() {
        return Displayname;
    }

    public String getEmail() {
        return Email;
    }
    public String getUdob() {
        return udob;
    }

    public String getUmob() {
        return umob;
    }

    public long getCreatedAt() {
        return createdAt;
    }

}
