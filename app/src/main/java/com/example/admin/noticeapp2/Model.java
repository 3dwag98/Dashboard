package com.example.admin.noticeapp2;

import java.util.Date;

public class Model {
    String from,tag;
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
