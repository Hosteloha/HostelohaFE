package com.hosteloha.app.datarepository.beans;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SellerProductObject {
    // Initialised with default values.
    @SerializedName("id")
    private int productId = 0;
    @SerializedName("product_title")
    private String title = "";
    @SerializedName("subtitle")
    private String subtitle = "";
    @SerializedName("description")
    private String description = "";
    @SerializedName("category_id")
    private int category_id = 1;
    @SerializedName("category")
    private String mainCategory = "Beauty";
    @SerializedName("subcategory1")
    private String subCategory1 = "FaceCreams";
    @SerializedName("subcategory2")
    private String subCategory2 = "Facewash";
    @SerializedName("users_id")
    private int users_id = 61; // testuser@gmail.com
    @SerializedName("condition_id")
    private int condition_id = 1;
    @SerializedName("delivery_format_id")
    private int delivery_format_id = 1;
    @SerializedName("payment_option_id")
    private int payment_option_id = 1;
    @SerializedName("selling_format_id")
    private int selling_format_id = 1;
    @SerializedName("currency")
    private String currency = "inr";
    @SerializedName("quantity")
    private int quantity = 1;
    @SerializedName("cost_price")
    private int costPrice = 0;
    @SerializedName("selling_price")
    private int sellingPrice = 0;
    @SerializedName("inserted_at")
    private Timestamp inserted_at;
    @SerializedName("updated_at")
    private Timestamp updated_at;
    @SerializedName("productimages")
    private List<ImageUrl> product_images = new ArrayList<>();

    public SellerProductObject() {
        super();
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public SellerProductObject(int productId, String subtitle, String description, int category_id, int users_id, int condition_id,
                               int delivery_format_id, int payment_option_id, int selling_format_id, String currency, int quantity,
                               int sellingPrice, int costPrice, Timestamp inserted_at, Timestamp updated_at) {
        this.productId = productId;
        this.subtitle = subtitle;
        this.description = description;
        this.category_id = category_id;
        this.users_id = users_id;
        this.condition_id = condition_id;
        this.delivery_format_id = delivery_format_id;
        this.payment_option_id = payment_option_id;
        this.selling_format_id = selling_format_id;
        this.currency = currency;
        this.quantity = quantity;
        this.sellingPrice = sellingPrice;
        this.costPrice = costPrice;
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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(int costPrice) {
        this.costPrice = costPrice;
    }

    public List<String> getProduct_images() {
        List<String> imageUrls = new ArrayList<>();
        for (Object obj : product_images)
            imageUrls.add(obj.toString());
        return imageUrls;
    }

    public void setProduct_images(List<String> product_images) {
        if (product_images == null)
            return;

        ArrayList<ImageUrl> productImages = new ArrayList<>();
        for (int i = 0; i < product_images.size(); i++) {
            productImages.add(new ImageUrl(product_images.get(i)));
        }
        this.product_images = productImages;
    }


    public SellerProductObject cloneProductObject() {
        SellerProductObject productObject = new SellerProductObject();
        productObject.productId = productId;
        productObject.title = title;
        productObject.subtitle = subtitle;
        productObject.description = description;
        productObject.category_id = category_id;
        productObject.mainCategory = mainCategory;
        productObject.subCategory1 = subCategory1;
        productObject.subCategory2 = subCategory2;
        productObject.users_id = users_id;
        productObject.condition_id = condition_id;
        productObject.delivery_format_id = delivery_format_id;
        productObject.payment_option_id = payment_option_id;
        productObject.selling_format_id = selling_format_id;
        productObject.currency = currency;
        productObject.quantity = quantity;
        productObject.costPrice = costPrice;
        productObject.sellingPrice = sellingPrice;
        productObject.inserted_at = inserted_at;
        productObject.updated_at = updated_at;
        productObject.product_images = product_images;

        return productObject;
    }

    @Override
    public String toString() {
        return "ProductObject [productId=" + productId + " , title = " + title + ", subtitle=" + subtitle + ", description=" + description
                + ", users_id=" + users_id + ", condition_id=" + condition_id + ", delivery_format_id="
                + delivery_format_id + ", payment_option_id=" + payment_option_id + ", selling_format_id="
                + selling_format_id + ", currency = " + currency + ", quantity=" + quantity + ",  mainCatergory = "
                + mainCategory + ", subCat 1 = " + subCategory1 + ", subCat 2 = " + subCategory2
                + ", sellingPrice=" + sellingPrice + ", costPrice = " + costPrice + " images=(" + getProduct_images() + "), inserted_at=" + inserted_at + ", updated_at=" + updated_at + "]";
    }
}

