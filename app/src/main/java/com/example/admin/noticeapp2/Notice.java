package com.example.admin.noticeapp2;

import java.util.Date;

public class Notice {
    String title, descrp,upload,type;
    Date time;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Notice(String title, String descrp, String upload, String type, Date time) {
        this.title = title;
        this.descrp = descrp;
        this.upload = upload;
        this.type = type;
        this.time = time;
    }

    public Notice() {
    }

    public Notice(String title, String Descrp){
        this.title = title;
        this.descrp = Descrp;
    }

    public Notice(String title, String descrp, String upload) {
        this.title = title;
        this.descrp = descrp;
        this.upload = upload;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public void setDescrp(String descrp){
        this.descrp = descrp;
    }

    public String getTitle(){
        return this.title;
    }
    public String getDescrp(){
        return this.descrp;
    }

    public String getUpload(){
        return this.upload;
    }

    public void setUpload(String upload){
        this.upload = upload;
    }
}
