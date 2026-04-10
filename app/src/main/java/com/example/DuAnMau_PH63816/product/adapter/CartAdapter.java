package com.example.DuAnMau_PH63816.product.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.product.CartActivity;
import com.example.DuAnMau_PH63816.product.data.CartManager;
import com.example.DuAnMau_PH63816.product.model.CartItem;
import com.example.DuAnMau_PH63816.product.model.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private final List<CartItem> items;

    public CartAdapter(Context context, List<CartItem> items) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_cart_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = items.get(position);
        Product product = item.getProduct();
        holder.txtCartProductName.setText(product.getName());
        holder.txtCartProductPrice.setText(product.getPriceLabel().replace("k", "đ"));
        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));
        ProductAdapter.bindProductImage(holder.imgCartProduct, product.getImage());
        holder.imgCartProduct.setBackgroundResource(position % 2 == 0
                ? R.drawable.bg_cart_product_image
                : R.drawable.bg_cart_product_image_alt);
        holder.btnDecreaseQuantity.setOnClickListener(v -> {
            CartManager.decreaseQuantity(product);
            refreshCart();
        });
        holder.btnIncreaseQuantity.setOnClickListener(v -> {
            if (!CartManager.increaseQuantity(product)) {
                Toast.makeText(context, "Sản phẩm đã hết tồn trong giỏ hàng", Toast.LENGTH_SHORT).show();
                return;
            }
            refreshCart();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void refreshCart() {
        if (context instanceof CartActivity) {
            ((CartActivity) context).refreshCart();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imgCartProduct;
        final TextView txtCartProductName;
        final TextView txtCartProductPrice;
        final TextView btnDecreaseQuantity;
        final TextView txtQuantity;
        final TextView btnIncreaseQuantity;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCartProduct = itemView.findViewById(R.id.imgCartProduct);
            txtCartProductName = itemView.findViewById(R.id.txtCartProductName);
            txtCartProductPrice = itemView.findViewById(R.id.txtCartProductPrice);
            btnDecreaseQuantity = itemView.findViewById(R.id.btnDecreaseQuantity);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            btnIncreaseQuantity = itemView.findViewById(R.id.btnIncreaseQuantity);
        }
    }
}
