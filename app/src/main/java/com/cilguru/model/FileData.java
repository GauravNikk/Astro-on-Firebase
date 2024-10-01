package com.cilguru.model;


import com.google.firebase.database.Exclude;

public class FileData {
    private String fName;


    private String flink;
    private String imgurl;


    private int position;
    private String key;
    public FileData(){ }
    public FileData(String fName, String flink, String imgurl){
        this.fName=fName;
        this.flink=flink;
        this.imgurl=imgurl;

    }

    public FileData(int position){
        this.position = position;
    }


//    public YtData(String chName, String desc, String imgUrl, String lang, String cate, String wlink, String bnnr) {
//    }


    public String getfName() { return fName; }
    public void setfName(String fName) { this.fName = fName; }

    //    public String getLang() { return lang; }
//    public void setLang(String lang) { this.lang = lang; }
//
    public String getImgurl() { return imgurl; }
    public void setImgurl(String imgurl) { this.imgurl = imgurl; }
//
//    public String getDesc() { return desc; }
//    public void setDesc(String desc) { this.desc = desc; }
//
//    public String getCate() { return cate; }
//    public void setCate(String cate) { this.cate = cate; }
//
//    public String getLink() { return link; }
//    public void setLink(String link) { this.link = link; }

    public String getFlink() { return flink; }
    public void setFlink(String flink) { this.flink = flink; }

//    public String getTvideo() { return tvideo; }
//    public void setTvideo(String tvideo) { this.tvideo = tvideo; }
//
//    public String getBnnr() { return bnnr; }
//    public void setBnnr(String bnnr) { this.bnnr = bnnr; }


    @Override
    public String toString() {
        return getfName();
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