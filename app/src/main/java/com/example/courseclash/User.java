package com.example.courseclash;

public class User {

    public String userName;
    public String email;
    public String title;
    public String userID;
    public String comments;
    public int score;

    public User(){

    }

    public User(String userName, String email, String title, String userID, int score) {
        this.userName = userName;
        this.email = email;
        this.title = title;
        this.userID = userID;
        this.score = score;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }



}
