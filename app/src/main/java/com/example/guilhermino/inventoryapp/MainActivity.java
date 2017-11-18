package com.example.guilhermino.inventoryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_DETAIL = 101;
    static final int REQUEST_DELETE = 909;
    static final int REQUEST_ERROR = 999;
    private ArrayList<Product> productList = new ArrayList<Product>();
    private ProductAdapter mAdapter;
    private TextView emptyTextView;
    private Bitmap pictute;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        ListView listView = listView = (ListView) findViewById(R.id.list);
        mAdapter = new ProductAdapter(this, productList);
        listView.setAdapter(mAdapter);

        emptyTextView = (TextView) findViewById(R.id.empty_text_view);
        Button addButton = (Button) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_add_product, null);

                builder.setView(dialogView)
                        .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                EditText nameEditText = (EditText) dialogView.findViewById(R.id.name_edit_text);
                                EditText priceEditText = (EditText) dialogView.findViewById(R.id.price_edit_text);
                                EditText quantityEditText = (EditText) dialogView.findViewById(R.id.quantity_edit_text);
                                EditText supplierEditText = (EditText) dialogView.findViewById(R.id.supplier_edit_text);
                                if (dialogView != null) {
                                    String name = nameEditText.getText().toString();
                                    String price = priceEditText.getText().toString();
                                    String quantity = quantityEditText.getText().toString();
                                    String supplier = supplierEditText.getText().toString();
                                    if (name.isEmpty() || price.isEmpty() || quantity.isEmpty() || supplier.isEmpty() || pictute == null) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.validator_fail), Toast.LENGTH_LONG).show();
                                    } else {
                                        double priceDouble = Double.parseDouble(price);
                                        int quantityInt = Integer.parseInt(quantity);
                                        db.create(name, priceDouble, quantityInt, 1, supplier, pictute);
                                        prepareData();
                                    }
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {}
                        });
                builder.create();
                builder.show();
            }
        });
        prepareData();
    }

    private void prepareData() {
        productList.clear();
        productList.addAll(db.getAllProducts());

        mAdapter.notifyDataSetChanged();

        if (productList.size() == 0) {
            noData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DETAIL) {
            if (resultCode == RESULT_OK) {
                prepareData();
            } else if (resultCode == REQUEST_DELETE) {
                int position = data.getIntExtra("position", REQUEST_ERROR);
                if (position == REQUEST_ERROR) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                } else {
                    db.delete(position);
                    prepareData();
                }
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                pictute = (Bitmap) data.getExtras().get("data");
            }
        }
    }

    public void takePicture(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void noData() {
        emptyTextView.setText(getString(R.string.noData));
        emptyTextView.setVisibility(View.VISIBLE);
    }
}