package com.example.smartlab;

public class DownModel {
    private String date;
    private String link;

    public DownModel() {

    }

    public DownModel(String date, String link) {
        this.date = date;
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
