package com.example.guilhermino.inventoryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    static final int REQUEST_DELETE = 909;
    private TextView quantityTextView;
    private Product product;
    private int position;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        db = new DatabaseHelper(this);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        product = db.getProduct(position);

        TextView nameTextView = (TextView) findViewById(R.id.name_text_view);
        quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        TextView availableTextView = (TextView) findViewById(R.id.available_text_view);
        TextView supplierTextView = (TextView) findViewById(R.id.supplier_text_view);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        imageView.setImageBitmap(product.getPicture());

        nameTextView.setText(getString(R.string.name, product.getName()));
        quantityTextView.setText(getString(R.string.quantity, product.getQuantity()));
        priceTextView.setText(getString(R.string.price, product.getPrice()));
        supplierTextView.setText(getString(R.string.supplier, product.getSupplier()));
        if (product.getAvailable() == 1) {
            availableTextView.setText(getString(R.string.availability, getString(R.string.available)));
        } else {
            availableTextView.setText(getString(R.string.availability, getString(R.string.unavailable)));
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(DetailActivity.RESULT_OK, intent);
        finish();
    }

    private String orderSummary() {
        String message = getString(R.string.name, product.getName());
        message += "\n" + getString(R.string.quantity, product.getQuantity());
        message += "\n" + getString(R.string.price, product.getPrice());
        if (product.getAvailable() == 1) {
            message += "\n" + getString(R.string.availability, getString(R.string.available));
        } else {
            message += "\n" + getString(R.string.availability, getString(R.string.unavailable));
        }
        message += "\n" + getString(R.string.supplier, product.getSupplier());
        return message;
    }

    public void sale(View v) {
        int quantity = product.sale();
        db.update(position, quantity);
        quantityTextView.setText(getString(R.string.quantity, quantity));
    }

    public void shipment(View v) {
        int quantity = product.shipment();
        db.update(position, quantity);
        quantityTextView.setText(getString(R.string.quantity, quantity));
    }

    public void order(View v) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_TEXT, orderSummary());
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        }
    }

    public void delete(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
        builder.setMessage(R.string.delete_dialog)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        intent.putExtra("position", position);
                        setResult(REQUEST_DELETE, intent);
                        finish();                            }
                })
                .setNegativeButton(R.string.cancel, null);
        builder.create();
        builder.show();
    }
}