package com.esprit.insta360.Models;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TIBH on 27/01/2017.
 */

public class Post {
    private int id;
    private Date date;
    private String description;
    private String url;
    private int likes;
    private int owner;

    public Post(int id, Date date, String description, String url, int likes, int owner) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.url = url;
        this.likes = likes;
        this.owner = owner;
    }

    public Post(JSONObject j) {
        this.id=j.optInt("id");
        this.owner=j.optInt("user_id");
        this.likes=j.optInt("like");
        this.description=j.optString("description");
        this.url=j.optString("url");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.date= sdf.parse(j.optString("date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }
}
