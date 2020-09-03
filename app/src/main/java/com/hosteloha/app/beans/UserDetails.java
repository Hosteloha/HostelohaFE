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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUser_privacy_id() {
        return user_privacy_id;
    }

    public void setUser_privacy_id(int user_privacy_id) {
        this.user_privacy_id = user_privacy_id;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUniversity_name() {
        return university_name;
    }

    public void setUniversity_name(String university_name) {
        this.university_name = university_name;
    }

    public String getUniversity_website() {
        return university_website;
    }

    public void setUniversity_website(String university_website) {
        this.university_website = university_website;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRes_address_1() {
        return res_address_1;
    }

    public void setRes_address_1(String res_address_1) {
        this.res_address_1 = res_address_1;
    }

    public String getRes_address_2() {
        return res_address_2;
    }

    public void setRes_address_2(String res_address_2) {
        this.res_address_2 = res_address_2;
    }

    public String getPhone_ext() {
        return phone_ext;
    }

    public void setPhone_ext(String phone_ext) {
        this.phone_ext = phone_ext;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public int getIs_university_verified() {
        return is_university_verified;
    }

    public void setIs_university_verified(int is_university_verified) {
        this.is_university_verified = is_university_verified;
    }

    public int getIs_mail_verified() {
        return is_mail_verified;
    }

    public void setIs_mail_verified(int is_mail_verified) {
        this.is_mail_verified = is_mail_verified;
    }

    public String getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }

    public String getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(String registration_date) {
        this.registration_date = registration_date;
    }
}
