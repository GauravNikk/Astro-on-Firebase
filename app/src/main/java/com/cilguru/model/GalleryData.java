package com.cilguru.model;


import com.google.firebase.database.Exclude;

public class GalleryData {
    private String chName;
    private String wlink;
    private String imgurl,cate;
    private int position;
    private String key;
    public GalleryData(){ }
    public GalleryData(String chName, String wlink){
        this.chName=chName;
        this.wlink=wlink;
        this.imgurl=imgurl;
        this.cate=cate;
    }

    public GalleryData(int position){
        this.position = position;
    }


//    public YtData(String chName, String desc, String imgUrl, String lang, String cate, String wlink, String bnnr) {
//    }


    public String getChName() { return chName; }
    public void setChName(String chName) { this.chName = chName; }

    public String getCate() { return cate; }
    public void setCate(String cate) { this.cate = cate; }

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

    public String getWlink() { return wlink; }
    public void setWlink(String wlink) { this.wlink = wlink; }

//    public String getTvideo() { return tvideo; }
//    public void setTvideo(String tvideo) { this.tvideo = tvideo; }
//
//    public String getBnnr() { return bnnr; }
//    public void setBnnr(String bnnr) { this.bnnr = bnnr; }


    @Override
    public String toString() {
        return getChName();
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