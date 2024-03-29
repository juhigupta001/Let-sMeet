package com.example.myapp.models;

public class ModelUsers {
    //use same name as in firebase database

    String name,email,search,phone,image,cover,uid;
    ModelUsers()
    {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getSearch() {
        return search;
    }

    public String getPhone() {
        return phone;
    }

    public String getImage() {
        return image;
    }

    public String getCover() {
        return cover;
    }

    public String getUid() {
        return uid;
    }

    public ModelUsers(String name, String email, String search, String phone, String image, String cover, String uid) {
        this.name = name;
        this.email = email;
        this.search = search;
        this.phone = phone;
        this.image = image;
        this.cover = cover;
        this.uid = uid;
    }
}
