package com.esprit.insta360.Models;

import org.json.JSONObject;

/**
 * Created by TIBH on 16/01/2017.
 */

public class Notification {
    private int id;
    private User sender;
    private User receiver ;
    private String type;
    private int post;

    public Notification(){

    }

    public Notification(int id , User sender , User receiver, String type, int post){
        this.id=id;
        this.sender=sender;
        this.receiver=receiver;
        this.type=type;
        this.post=post;
    }

    public Notification(JSONObject j) {
        this.id=j.optInt("id");
        //this.sender = j.optString("name");
        this.type=j.optString("type");
        //this.post= j.optString("login");


    }
}
