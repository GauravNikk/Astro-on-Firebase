package com.cilguru.model;


import com.google.firebase.database.Exclude;


public class Events {
    private String evName;
    private String lang;
    private String imgurl;
    private String link;
    private String wlink;
    //  private String imgurl;

    private String desc;
    private int position;
    private String key;
    private String date;
    private String tvideo,bnnr,livetv,website;

    public Events(String evName, String desc, String imgurl, String date){
        this.evName=evName;
        this.imgurl = imgurl;
        this.desc = desc;
        this.date=date;


    }

    public Events(int position){
        this.position = position;
    }


    public String getEvName() { return evName; }
    public void setEvName(String evName) { this.evName = evName; }

    public String getImgurl() {
        return imgurl;
    }
    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    @Override
    public String toString() {
        return getEvName();
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