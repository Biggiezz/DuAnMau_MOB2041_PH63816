package com.example.DuAnMau_PH63816.product;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.custom.CustomCardView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    public interface OnProductClickListener {
        void onProductClick(@NonNull Product product);
    }

    private final List<Product> items;
    private final OnProductClickListener listener;

    public ProductAdapter(@NonNull List<Product> items, @NonNull OnProductClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomCardView view = (CustomCardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_card, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = items.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private final CustomCardView cardView;

        ProductViewHolder(@NonNull CustomCardView itemView) {
            super(itemView);
            this.cardView = itemView;
        }

        void bind(Product product) {
            cardView.setTitle(product.getName());
            cardView.setSubtitle(product.getPriceLabel());
            cardView.setSubtitle2(product.getStockLabel());
            cardView.setIcon(product.getImageRes());

            cardView.setOnClickListener(v -> listener.onProductClick(product));
        }
    }
}
