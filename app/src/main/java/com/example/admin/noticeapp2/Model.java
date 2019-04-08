package com.example.admin.noticeapp2;

import java.util.Date;

public class Model {
    String from;
    String tag,msg,upload,type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUpload() {
        return upload;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }

    public Model(String from, String tag, String msg, String descrp, Date date) {
        this.from = from;
        this.tag = tag;
        this.msg = msg;
        this.descrp = descrp;
        this.date = date;
    }

    public Model(String from, String tag, Date date, String descrp) {
        this.from = from;
        this.tag = tag;
        this.descrp = descrp;
        this.date = date;
    }

    String descrp;
    Date date;

    public Model() {
    }

    public Model(String from, String tag) {
        this.from = from;
        this.tag = tag;
    }

    public Model(String from, String tag, Date date) {
        this.from = from;
        this.tag = tag;
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTag() {
        return tag;
    }

    public String getDescrp() {
        return descrp;
    }

    public void setDescrp(String descrp) {
        this.descrp = descrp;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
