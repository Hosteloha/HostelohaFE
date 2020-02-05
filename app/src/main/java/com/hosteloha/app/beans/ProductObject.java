package com.hosteloha.app.beans;

import java.sql.Timestamp;

public class ProductObject {
    private int id;
    private String title;
    private String subtitle;
    private String description;
    private int category_id;
    private String mainCategory;
    private String subCategory1;
    private String subCategory2;
    private int users_id;
    private int condition_id;
    private int delivery_format_id;
    private int payment_option_id;
    private int selling_format_id;
    private Timestamp inserted_at;
    private Timestamp updated_at;

    public ProductObject() {
        super();
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public ProductObject(int id, String subtitle, String description, int category_id, int users_id, int condition_id,
                         int delivery_format_id, int payment_option_id, int selling_format_id, Timestamp inserted_at,
                         Timestamp updated_at) {
        this.id = id;
        this.subtitle = subtitle;
        this.description = description;
        this.category_id = category_id;
        this.users_id = users_id;
        this.condition_id = condition_id;
        this.delivery_format_id = delivery_format_id;
        this.payment_option_id = payment_option_id;
        this.selling_format_id = selling_format_id;
        this.inserted_at = inserted_at;
        this.updated_at = updated_at;

    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public String getSubCategory1() {
        return subCategory1;
    }

    public void setSubCategory1(String subCategory1) {
        this.subCategory1 = subCategory1;
    }

    public String getSubCategory2() {
        return subCategory2;
    }

    public void setSubCategory2(String subCategory2) {
        this.subCategory2 = subCategory2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUsers_id() {
        return users_id;
    }

    public void setUsers_id(int users_id) {
        this.users_id = users_id;
    }

    public int getCondition_id() {
        return condition_id;
    }

    public void setCondition_id(int condition_id) {
        this.condition_id = condition_id;
    }

    public int getDelivery_format_id() {
        return delivery_format_id;
    }

    public void setDelivery_format_id(int delivery_format_id) {
        this.delivery_format_id = delivery_format_id;
    }

    public int getPayment_option_id() {
        return payment_option_id;
    }

    public void setPayment_option_id(int payment_option_id) {
        this.payment_option_id = payment_option_id;
    }

    public int getSelling_format_id() {
        return selling_format_id;
    }

    public void setSelling_format_id(int selling_format_id) {
        this.selling_format_id = selling_format_id;
    }

    public Timestamp getInserted_at() {
        return inserted_at;
    }

    public void setInserted_at(Timestamp inserted_at) {
        this.inserted_at = inserted_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "ProductObject [id=" + id + ", subtitle=" + subtitle + ", description=" + description
                + ", users_id=" + users_id + ", condition_id=" + condition_id + ", delivery_format_id="
                + delivery_format_id + ", payment_option_id=" + payment_option_id + ", selling_format_id="
                + selling_format_id + ", inserted_at=" + inserted_at + ", updated_at=" + updated_at + "]";
    }
}
