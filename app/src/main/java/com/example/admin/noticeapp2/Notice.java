package com.example.admin.noticeapp2;

public class Notice {
    String title,Descrp,upload;

    public Notice() {
    }

    public Notice(String title, String Descrp){
        this.title = title;
        this.Descrp = Descrp;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public void setDes(String descrp){
        this.Descrp = descrp;
    }

    public String getTitle(){
        return this.title;
    }
    public String getDes(){
        return this.Descrp;
    }

    public String getUpload(){
        return this.upload;
    }

    public void setUpload(String upload){
        this.upload = upload;
    }
}
