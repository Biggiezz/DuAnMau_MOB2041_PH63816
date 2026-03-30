package com.example.DuAnMau_PH63816.product.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.custom.CustomCardView;
import com.example.DuAnMau_PH63816.product.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

/// extend adapter cua recycler view vao class nay
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    public interface OnProductClickListener {
        void onProductClick(@NonNull Product product);
    }

    public interface OnAddToCartClickListener {
        void onAddToCartClick(@NonNull Product product, @NonNull View addIconView);
    }

    public interface OnRemoveFromCartClickListener {
        void onRemoveFromCartClick(@NonNull Product product);
    }

    /// khai bao context va list de lay du lieu
    private final LayoutInflater inflater;
    private final List<Product> items;
    private final OnProductClickListener listener;
    private final OnAddToCartClickListener addToCartListener;
    private final OnRemoveFromCartClickListener removeFromCartListener;

    /// khoi tao context va list de lay du lieu
    public ProductAdapter(
            Context context,
            List<Product> items,
            OnProductClickListener listener,
            OnAddToCartClickListener addToCartListener,
            OnRemoveFromCartClickListener removeFromCartListener
    ) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
        this.listener = listener;
        this.addToCartListener = addToCartListener;
        this.removeFromCartListener = removeFromCartListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /// lay layoutInflater de lay layout custom
        CustomCardView cardView = (CustomCardView) inflater.inflate(R.layout.item_product_card, parent, false);

        /// no se tra ve viewholder
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /// lay du lieu tu list va gan vao viewholder de hien thi len giao dien
        Product product = items.get(position);

        holder.cardView.setTitle(product.getName());
        holder.cardView.setSubtitle(product.getPriceLabel());
        holder.cardView.setSubtitle2(product.getStockLabel());

        bindProductImage(holder.imgIcon, product.getImage());

        holder.cardView.setOnClickListener(v -> listener.onProductClick(product));
        holder.cardView.setOnEditClickListener(v -> {
            if (addToCartListener != null) {
                addToCartListener.onAddToCartClick(product, v);
            }
        });
        holder.cardView.setOnRemoveClickListener(v -> {
            if (removeFromCartListener != null) {
                removeFromCartListener.onRemoveFromCartClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        /// lay du lieu tu layout custom
        final CustomCardView cardView;
        final ImageView imgIcon;

        ViewHolder(@NonNull CustomCardView cardView) {
            super(cardView);
            this.cardView = cardView;
            this.imgIcon = cardView.findViewById(R.id.imgIcon);
        }
    }

    public static void bindProductImage(@NonNull ImageView target, String image) {
        if (image != null) {
            try {
                int resId = Integer.parseInt(image);
                Picasso.get()
                        .load(resId)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .fit()
                        .into(target);
            } catch (NumberFormatException ex) {
                Picasso.get()
                        .load(image)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .fit()
                        .into(target);
            }
        } else {
            target.setImageResource(R.drawable.ic_launcher_background);
        }
    }
}
