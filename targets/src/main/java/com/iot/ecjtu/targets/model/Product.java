package com.iot.ecjtu.targets.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 497908738@qq.com on 2016/3/6.
 */
public class Product implements Serializable {
    private String image, name;
    private Double price;
    private int id;
    private String description, orderTime;
    private int rate, orderNumber, orderState;
    private int number;
    private Picture[] mPictures;
    private Review mReview;
    private int categoriesId;
    private String categoriesName;
    private String categoriesIcon;
    private List<Product> mProducts;

    public String getCategoriesName() {
        return categoriesName;
    }

    public void setCategoriesName(String categoriesName) {
        this.categoriesName = categoriesName;
    }

    public String getCategoriesIcon() {
        return categoriesIcon;
    }

    public void setCategoriesIcon(String categoriesIcon) {
        this.categoriesIcon = categoriesIcon;
    }

    public int getCategoriesId() {
        return categoriesId;
    }

    public void setCategoriesId(int categoriesId) {
        this.categoriesId = categoriesId;
    }

    public Review getReview() {
        return mReview;
    }

    public void setReview(Review review) {
        mReview = review;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private int quantity;

    public Picture[] getPictures() {
        return mPictures;
    }

    public void setPictures(Picture[] pictures) {
        mPictures = pictures;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }


    public List<Product> getProducts() {
        return mProducts;
    }

    public void setProducts(List<Product> products) {
        mProducts = products;
    }
}
