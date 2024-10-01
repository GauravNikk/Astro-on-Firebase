package com.cilguru.model;


import com.google.firebase.database.Exclude;

public class ShopData {
    private String pName;
    private String pprice;
    private String pimgurl,pdesc,pfeature,pjoin;


    private int position;
    private String key;

    public ShopData(){ }
    public ShopData(String pName, String pimgurl, String pprice, String pdesc, String pfeature){
        this.pName=pName;
        this.pprice=pprice;
        this.pimgurl=pimgurl;
        this.pdesc=pdesc;
        this.pfeature=pfeature;
        this.pjoin=pjoin;


    }

    public ShopData(int position){
        this.position = position;
    }

    public String getpName() {
        return pName;
    }
    public String getPprice() {
        return pprice;
    }
    public String getPjoin() {
        return pjoin;
    }
    public String getPdesc() {
        return pdesc;
    }
    public String getPfeature() {
        return pfeature;
    }
    public String getPimgurl() {
        return pimgurl;
    }


    public void setpName(String pName) {
        this.pName = pName;
    }
    public void setPdesc(String pdesc) {
        this.pdesc = pdesc;
    }
    public void setPfeature(String pfeature) {
        this.pfeature = pfeature;
    }
    public void setPprice(String pprice) {
        this.pprice = pprice;
    }
    public void setPjoin(String pjoin) {
        this.pjoin = pjoin;
    }
    public void setPimgurl(String pimgurl) {
        this.pimgurl = pimgurl;
    }


    @Override
    public String toString() {
        return getpName();
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