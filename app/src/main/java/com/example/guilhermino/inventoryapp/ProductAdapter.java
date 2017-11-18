package com.example.guilhermino.inventoryapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by guilhermino on 01/07/16.
 */
public class ProductAdapter extends BaseAdapter {
    static final int REQUEST_DETAIL = 101;
    private ArrayList products;
    private LayoutInflater layoutInflater;
    private Context context;
    DatabaseHelper db;

    public ProductAdapter(Context context, ArrayList products) {
        this.products = products;
        this.context = context;
        db = new DatabaseHelper(context);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row, null);
            holder = new ViewHolder();
            holder.nameTextView = (TextView) convertView.findViewById(R.id.name_text_view);
            holder.priceTextView = (TextView) convertView.findViewById(R.id.price_text_view);
            holder.quantityTextView = (TextView) convertView.findViewById(R.id.quantity_text_view);
            holder.saleButton = (Button) convertView.findViewById(R.id.sale_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Product produt = (Product) products.get(position);

        if (produt != null) {
            Resources res = context.getResources();
            holder.nameTextView.setText(produt.getName());
            holder.priceTextView.setText(String.format(res.getString(R.string.price), produt.getPrice()));
            holder.quantityTextView.setText(String.format(res.getString(R.string.quantity), produt.getQuantity()));
            holder.saleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.update(produt.getId(), produt.sale());
                    notifyDataSetChanged();
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("position", produt.getId());
                    ((Activity)context).startActivityForResult(intent, REQUEST_DETAIL);
                }
            });
        }
        return convertView;
    }

    public void swapItems(ArrayList<Product> items) {
        this.products = items;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView nameTextView;
        TextView priceTextView;
        TextView quantityTextView;
        Button saleButton;
    }
}