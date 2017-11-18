package com.example.guilhermino.inventoryapp;

import android.graphics.Bitmap;

/**
 * Created by guilhermino on 01/07/16.
 */
public class Product {
    private double mPrice;
    private int mId, mQuantity , mAvailable;
    private String mName, mSupplier;
    private Bitmap mPicture;

    public Product(int id, String name, double price, int quantity, int available, String supplier,
                   Bitmap picture) {
        mId = id;
        mName = name;
        mPrice = price;
        mQuantity = quantity;
        mAvailable = available;
        mSupplier = supplier;
        mPicture = picture;
    }

    public double getPrice() {
        return mPrice;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public int getAvailable() {
        return mAvailable;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getSupplier() {
        return mSupplier;
    }

    public Bitmap getPicture() {
        return mPicture;
    }

    public void setPicture(Bitmap mPicture) {
        this.mPicture = mPicture;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public int sale() {
        if(mQuantity>0) {
            mQuantity = mQuantity - 1;
        }
        return mQuantity;
    }

    public int shipment() {
        mQuantity = mQuantity + 1;
        return mQuantity;
    }
}
