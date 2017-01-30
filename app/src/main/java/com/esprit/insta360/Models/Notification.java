package com.esprit.insta360.Models;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TIBH on 16/01/2017.
 */

public class Notification {
    private int id;
    private int sender;
    private int link;
    private String name;
    private String type;
    private String profile;
    private String url;
    private Date created_at;

    public Notification(){

    }


    public Notification(int id ,int sender, int link, String name ,  String type,String profile,String url,Date created_at){
        this.id=id;
        this.sender=sender;
        this.link=link;
        this.name=name;
        this.type=type;
        this.profile=profile;
        this.url=url;
        this.created_at=created_at;
    }

    public Notification(JSONObject j) {
        this.id=j.optInt("id");
        this.sender=j.optInt("sender");
        this.link=j.optInt("link");
        this.type=j.optString("type");
        this.url=j.optString("url");
        this.profile=j.optString("photo");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.created_at= sdf.parse(j.optString("created_at"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.name=j.optString("name");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getLink() {
        return link;
    }

    public void setLink(int link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
