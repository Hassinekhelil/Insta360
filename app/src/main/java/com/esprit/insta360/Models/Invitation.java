package com.esprit.insta360.Models;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by TIBH on 28/01/2017.
 */

public class Invitation {

    private int id;
    private int sender;
    private int receiver;

    public Invitation(int id, int sender, int receiver) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
    }

    public Invitation(JSONObject j) {
        this.id=j.optInt("id");
        this.sender=j.optInt("sender");
        this.receiver=j.optInt("receiver");
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

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }
}
