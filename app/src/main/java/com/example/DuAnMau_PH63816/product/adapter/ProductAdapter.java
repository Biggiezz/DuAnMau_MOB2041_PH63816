package com.example.DuAnMau_PH63816.product.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.custom.CustomCardView;
import com.example.DuAnMau_PH63816.product.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends BaseAdapter {

    public interface OnProductClickListener {
        void onProductClick(@NonNull Product product);
    }

    private final LayoutInflater inflater;
    private final List<Product> items;
    private final OnProductClickListener listener;

    public ProductAdapter(Context context, List<Product> items, OnProductClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Product getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            CustomCardView cardView = (CustomCardView) inflater.inflate(R.layout.item_product_card, parent, false);
            holder = new ViewHolder(cardView);
            cardView.setTag(holder);
            convertView = cardView;
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = getItem(position);

        holder.cardView.setTitle(product.getName());
        holder.cardView.setSubtitle(product.getPriceLabel());
        holder.cardView.setSubtitle2(product.getStockLabel());

        String image = product.getImage();
        try {
            int resId = Integer.parseInt(image);
            Picasso.get()
                    .load(resId)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .fit()
                    .into(holder.imgIcon);
        } catch (NumberFormatException ex) {
            Picasso.get()
                    .load(image)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .fit()
                    .into(holder.imgIcon);
        }

        holder.cardView.setOnClickListener(v -> listener.onProductClick(product));

        return convertView;
    }

    private static class ViewHolder {
        final CustomCardView cardView;
        final ImageView imgIcon;

        ViewHolder(@NonNull CustomCardView cardView) {
            this.cardView = cardView;
            this.imgIcon = cardView.findViewById(R.id.imgIcon);
        }
    }
}
