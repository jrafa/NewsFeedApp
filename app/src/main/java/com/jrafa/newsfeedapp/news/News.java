package com.jrafa.newsfeedapp.news;

public class News {
    private final String title;
    private final String author;
    private final String section;
    private final String datePublish;
    private final String url;

    public News(String title, String author, String section, String datePublish, String url) {
        this.title = title;
        this.author = author;
        this.section = section;
        this.datePublish = datePublish;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getSection() {
        return section;
    }

    public String getDatePublish() {
        return datePublish;
    }

    public String getUrl() {
        return url;
    }

}
