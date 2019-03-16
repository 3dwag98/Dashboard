package com.example.admin.noticeapp2;

public class Notice {
    String title, descrp,upload;

    public Notice() {
    }

    public Notice(String title, String Descrp){
        this.title = title;
        this.descrp = Descrp;
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
