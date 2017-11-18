package com.example.guilhermino.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by guilhermino on 7/4/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;

    public static synchronized DatabaseHelper getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public DatabaseHelper(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.Product.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DatabaseContract.Product.DELETE_TABLE);
        onCreate(db);
    }

    public void deleteDatabase(Context context) {
        context.deleteDatabase(DatabaseContract.DATABASE_NAME);
    }

    public boolean create(String name, Double price, Integer quantity, Integer availability, String supplier, Bitmap image) {
        ContentValues values;
        long result;

        SQLiteDatabase db = getWritableDatabase();
        byte[] data = getBitmapAsByteArray(image);

        values = new ContentValues();
        values.put(DatabaseContract.Product.PRODUCT_NAME, name);
        values.put(DatabaseContract.Product.PRODUCT_PRICE, price);
        values.put(DatabaseContract.Product.PRODUCT_QUANTITY, quantity);
        values.put(DatabaseContract.Product.PRODUCT_AVAILABILITY, availability);
        values.put(DatabaseContract.Product.PRODUCT_SUPPLIER, supplier);
        values.put(DatabaseContract.Product.PRODUCT_IMAGE, data);

        result = db.insert(DatabaseContract.Product.TABLE_NAME, null, values);
        db.close();

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Product getProduct(int id){
        String where = DatabaseContract.Product.PRODUCT_ID + "=" + id;
        SQLiteDatabase db = getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from product where " + where, null );

        if (res != null) {
            res.moveToFirst();
        }

        String name = res.getString(res.getColumnIndex(DatabaseContract.Product.PRODUCT_NAME));
        double price = res.getDouble(res.getColumnIndex(DatabaseContract.Product.PRODUCT_PRICE));
        int quantity = res.getInt(res.getColumnIndex(DatabaseContract.Product.PRODUCT_QUANTITY));
        int avaibility = res.getInt(res.getColumnIndex(DatabaseContract.Product.PRODUCT_AVAILABILITY));
        String supplier = res.getString(res.getColumnIndex(DatabaseContract.Product.PRODUCT_SUPPLIER));
        byte[] imgByte = res.getBlob(res.getColumnIndex(DatabaseContract.Product.PRODUCT_IMAGE));

        Bitmap image = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);

        Product product = new Product(id, name, price, quantity, avaibility, supplier, image);

        db.close();
        return product;
    }

    public void update(Integer id, Integer quantity) {
        ContentValues values;
        String where;

        SQLiteDatabase db = getWritableDatabase();

        where = DatabaseContract.Product.PRODUCT_ID + "=" + id;

        values = new ContentValues();
        values.put(DatabaseContract.Product.PRODUCT_QUANTITY, quantity);

        db.update(DatabaseContract.Product.TABLE_NAME, values, where, null);
        db.close();
    }

    public void delete(Integer id) {
        String where = DatabaseContract.Product.PRODUCT_ID + "=" + id;
        SQLiteDatabase db = getReadableDatabase();
        db.delete(DatabaseContract.Product.TABLE_NAME, where, null);
        db.close();
    }

    public ArrayList<Product> getAllProducts(){
        ArrayList<Product> productsList = new ArrayList<Product>();

        SQLiteDatabase db = getWritableDatabase();

        Cursor res = db.rawQuery("select * from product", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            int id = res.getInt(res.getColumnIndex(DatabaseContract.Product.PRODUCT_ID));
            String name = res.getString(res.getColumnIndex(DatabaseContract.Product.PRODUCT_NAME));
            double price = res.getDouble(res.getColumnIndex(DatabaseContract.Product.PRODUCT_PRICE));
            int quantity = res.getInt(res.getColumnIndex(DatabaseContract.Product.PRODUCT_QUANTITY));
            int avaibility = res.getInt(res.getColumnIndex(DatabaseContract.Product.PRODUCT_AVAILABILITY));
            String supplier = res.getString(res.getColumnIndex(DatabaseContract.Product.PRODUCT_SUPPLIER));
            byte[] imgByte = res.getBlob(res.getColumnIndex(DatabaseContract.Product.PRODUCT_IMAGE));

            Bitmap image = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);

            productsList.add(new Product(id, name, price, quantity, avaibility, supplier, image));
            res.moveToNext();
        }
        return productsList;
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}