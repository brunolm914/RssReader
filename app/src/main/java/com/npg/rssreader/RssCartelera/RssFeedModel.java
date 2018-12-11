package com.npg.rssreader.RssCartelera;
public class RssFeedModel {

    public String title;
    public String link;
    public String description;
    public String imageLink;

    public RssFeedModel(String title, String link, String description, String imageLink) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.imageLink=imageLink;
    }
}