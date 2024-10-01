package com.cilguru.model;


import com.google.firebase.database.Exclude;

public class Req {
    private String pname;
    private String pprice;
    private String pimgurl;

    private String uname,umob,uadd,uemail,payst,uid;
    private int position;
    private String key;

    public Req(String pname, String pprice, String pimgurl, String uname, String umob, String uemail, String uadd, String payst, String uid){
        this.pname=pname;
        this.pprice=pprice;
        this.pimgurl=pimgurl;

        this.uname=uname;
        this.umob=umob;
        this.uemail=uemail;
        this.payst=payst;
        this.uadd=uadd;
        this.uid=uid;
       // this.pimgurl=pimgurl;

    }

    public Req(){}
    public Req(int position){
        this.position = position;
    }

    public String getPname() { return pname; }
    public void setPname(String pname) { this.pname = pname; }
    public String getPprice() { return pprice; }
    public void setPprice(String pprice) { this.pprice = pprice; }
    public String getPimgurl() { return pimgurl; }
    public void setPimgurl(String pimgurl) { this.pimgurl = pimgurl; }

    public String getUname() { return uname; }
    public void setUname(String uname) { this.uname = uname; }
    public String getUmob() { return umob; }
    public void setUmob(String umob) { this.umob = umob; }
    public String getUemail() { return uemail; }
    public void setUemail(String uemail) { this.uemail = uemail; }
    public String getUadd() { return uadd; }
    public void setUadd(String uadd) { this.uadd = uadd; }
    public String getPayst() {
        return payst;
    }
    public void setPayst(String payst) {
        this.payst = payst;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return getPname();
    }

    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}