package com.example.guilhermino.inventoryapp;

import android.provider.BaseColumns;

/**
 * Created by guilhermino on 7/4/16.
 */
public class DatabaseContract {

    public static final  int    DATABASE_VERSION   = 1;
    public static final String DATABASE_NAME = "inventory.db";

    private DatabaseContract() {}

    public static abstract class Product implements BaseColumns {
        public static final String TABLE_NAME = "product";
        public static final String PRODUCT_ID = "_id";
        public static final String PRODUCT_NAME = "name";
        public static final String PRODUCT_PRICE = "price";
        public static final String PRODUCT_QUANTITY = "quantity";
        public static final String PRODUCT_AVAILABILITY = "availability";
        public static final String PRODUCT_SUPPLIER = "supplier";
        public static final String PRODUCT_IMAGE = "image";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "("
                + PRODUCT_ID + " integer primary key autoincrement,"
                + PRODUCT_NAME + " text,"
                + PRODUCT_PRICE + " real,"
                + PRODUCT_QUANTITY + " integer,"
                + PRODUCT_AVAILABILITY + " integer,"
                + PRODUCT_SUPPLIER + " text,"
                + PRODUCT_IMAGE + " blob"
                + ")";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}