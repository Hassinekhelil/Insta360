package com.esprit.insta360.Models;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TIBH on 14/h11/2016.
 */

public class User {
    private int id;
    private String name;
    private String email;
    private String login;
    private String password;
    private String photo;
    private String position;
    private Date updated_at;
    private String biographie;
    private int followers;
    private int followings;
    private int posts;

    public User()
    {

    }

    public User(String name, String email, String login, String password, String photo,
                String biographie,int followers,int followings,int posts) {
        this.name = name;
        this.email = email;
        this.login = login;
        this.password = password;
        this.photo = photo;
        this.biographie=biographie;
        this.followers=followers;
        this.followings=followings;
        this.posts=posts;
    }

    public User(JSONObject j) {
        this.id=j.optInt("id");
        this.name = j.optString("name");
        this.email=j.optString("email");
        this.login = j.optString("login");
        this.biographie=j.optString("biographie");
        this.photo=j.optString("photo");
        this.followers=j.optInt("followers");
        this.followings=j.optInt("followings");
        this.posts=j.optInt("posts");
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowings() {
        return followings;
    }

    public void setFollowings(int followings) {
        this.followings = followings;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public String getBiographie() {
        return biographie;
    }

    public void setBiographie(String biographie) {
        this.biographie = biographie;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", login=" + login +
                ", password='" + password + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (login != user.login) return false;
        if (!name.equals(user.name)) return false;
        if (!email.equals(user.email)) return false;
        if (!password.equals(user.password)) return false;
        if (!photo.equals(user.photo)) return false;
        return true ;

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + login.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + photo.hashCode();
        return result;
    }


}
