package com.hosteloha.app.beans;

import com.google.gson.annotations.SerializedName;

public class UserDetails {
    @SerializedName("id")
    private int userId;
    @SerializedName("productId")
    private int productId;
    @SerializedName("user_privacy_id")
    private int user_privacy_id;
    @SerializedName("phone")
    private int phone;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("firstname")
    private String firstname;
    @SerializedName("last_name")
    private String last_name;
    @SerializedName("university_name")
    private String university_name;
    @SerializedName("university_website")
    private String university_website;
    @SerializedName("dob")
    private String dob;
    @SerializedName("city")
    private String city;
    @SerializedName("state")
    private String state;
    @SerializedName("zip")
    private int zip;
    @SerializedName("country")
    private String country;
    @SerializedName("res_address_1")
    private String res_address_1;
    @SerializedName("res_address_2")
    private String res_address_2;
    @SerializedName("phone_ext")
    private String phone_ext;
    @SerializedName("ip_address")
    private String ip_address;
    @SerializedName("is_active")
    private int is_active;
    @SerializedName("is_university_verified")
    private int is_university_verified;
    @SerializedName("is_mail_verified")
    private int is_mail_verified;
    @SerializedName("verification_code")
    private String verification_code;
    @SerializedName("registration_date")
    private String registration_date;
}
