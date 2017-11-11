package com.example.abeeral_olayan.productratingapp;

/**
 * Created by abeeral-olayan on 10/15/17.
 */

public class UserAdmin {

    private  String UAID;
    private  String UAName;
    private  String AdminOrUser;

    public UserAdmin(){}

    public UserAdmin(String userOrAdmin, String UAID, String UAName){
        this.AdminOrUser="U";
        this.UAID=UAID;
        this.UAName=UAName;

    }

    public String getAdminOrUser(){return AdminOrUser;}
    public String getUAName(){return UAName;}
    public String getUAID(){return UAID;}

    public void setAdminOrUser(String AdminOrUser){this.AdminOrUser=AdminOrUser;}
    public void setUAName(String UAName){this.UAName=UAName;}
    public void setUAID(String Email){this.UAID=Email;}

}
