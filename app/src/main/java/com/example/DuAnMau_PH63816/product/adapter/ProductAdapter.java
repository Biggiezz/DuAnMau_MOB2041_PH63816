package com.example.DuAnMau_PH63816.product.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.custom.CustomCardView;
import com.example.DuAnMau_PH63816.product.DetailProductScreen;
import com.example.DuAnMau_PH63816.product.ProductImageResolver;
import com.example.DuAnMau_PH63816.product.ProductExtras;
import com.example.DuAnMau_PH63816.product.data.CartManager;
import com.example.DuAnMau_PH63816.product.model.Product;

import java.util.List;

/// extend adapter cua recycler view vao class nay
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    /// khai bao context va list de lay du lieu
    private final Context context;
    private final LayoutInflater inflater;
    private final List<Product> items;

    /// khoi tao context va list de lay du lieu
    public ProductAdapter(Context context, List<Product> items) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.items = items;
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
        holder.cardView.setSubtitle2(CartManager.getAvailableStockLabel(product));

        bindProductImage(holder.imgIcon, product.getImage());

        holder.cardView.setOnClickListener(v -> openDetail(product));
        holder.cardView.setOnEditClickListener(v -> {
            if (!CartManager.addProduct(product)) {
                Toast.makeText(context, "Sản phẩm đã hết tồn trong giỏ hàng", Toast.LENGTH_SHORT).show();
                return;
            }

            v.animate()
                    .scaleX(1.12f)
                    .scaleY(1.12f)
                    .alpha(0.7f)
                    .setDuration(120)
                    .withEndAction(() -> v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .alpha(1f)
                            .setDuration(120)
                            .start())
                    .start();

            notifyDataSetChanged();
            Toast.makeText(context, context.getString(R.string.toast_added_to_cart, product.getName()), Toast.LENGTH_SHORT).show();
        });
        holder.cardView.setOnRemoveClickListener(v -> {
            CartManager.decreaseQuantity(product);
            notifyDataSetChanged();
            Toast.makeText(context, context.getString(R.string.toast_removed_from_cart, product.getName()), Toast.LENGTH_SHORT).show();
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
        ProductImageResolver.loadInto(target, image);
    }

    private void openDetail(Product product) {
        Intent intent = new Intent(context, DetailProductScreen.class);
        intent.putExtra(ProductExtras.NAME, product.getName());
        intent.putExtra(ProductExtras.PRICE, product.getPriceLabel());
        intent.putExtra(ProductExtras.STOCK, product.getStockLabel());
        intent.putExtra(ProductExtras.IMAGE, product.getImage());
        intent.putExtra(ProductExtras.ID, product.getId());
        intent.putExtra(ProductExtras.CATEGORY, product.getCategory());
        intent.putExtra(ProductExtras.UNIT, product.getUnit());
        intent.putExtra(ProductExtras.DATE, product.getDate());
        intent.putExtra(ProductExtras.STATUS, product.getStatus());
        context.startActivity(intent);
    }
}
