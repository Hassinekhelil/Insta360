package com.esprit.insta360.Models;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TIBH on 05/02/2017.
 */

public class Comment {
    private int id;
    private int user;
    private int owner;
    private String name;
    private String content;
    private String picture;
    private Date created_at;
    private Boolean isLiked;
    private int likes;

    public Comment(int id, int user, String name, String content, String picture, Date created_at, Boolean isLiked, int likes) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.content = content;
        this.picture = picture;
        this.created_at = created_at;
        this.isLiked = isLiked;
        this.likes = likes;
    }

    public Comment(JSONObject j) {
        this.id=j.optInt("id");
        this.owner=j.optInt("owner");
        this.user=j.optInt("user");
        this.name=j.optString("name");
        this.content=j.optString("content");
        this.picture=j.optString("photo");
        this.isLiked=j.optBoolean("liked");
        this.likes=j.optInt("likes");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.created_at= sdf.parse(j.optString("date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
