package com.example.admin.noticeapp2;

import java.util.Date;

public class Query {
    String to,from,query;
    Date date;

    public Query() {
    }

    public Query(String to, String query, Date date) {
        this.to = to;
        this.query = query;
        this.date = date;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Query(String to, String from, String query, Date date) {
        this.to = to;
        this.from = from;
        this.query = query;
        this.date = date;
    }
}
