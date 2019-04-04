
package com.example.admin.noticeapp2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TextSimilarity {

    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("time")
    @Expose
    private Integer time;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("langConfidence")
    @Expose
    private Integer langConfidence;
    @SerializedName("text1")
    @Expose
    private String text1;
    @SerializedName("url1")
    @Expose
    private String url1;
    @SerializedName("text2")
    @Expose
    private String text2;
    @SerializedName("url2")
    @Expose
    private String url2;
    @SerializedName("similarity")
    @Expose
    private Double similarity;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Integer getLangConfidence() {
        return langConfidence;
    }

    public void setLangConfidence(Integer langConfidence) {
        this.langConfidence = langConfidence;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

}
