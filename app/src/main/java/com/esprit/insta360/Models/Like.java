package com.esprit.insta360.Models;

import org.json.JSONObject;

/**
 * Created by TIBH on 30/01/2017.
 */

public class Like {
    private int id;
    private int user;
    private int post;

    public Like(int id, int user, int post) {
        this.id = id;
        this.user = user;
        this.post = post;
    }

    public Like(JSONObject j){


    }
}
