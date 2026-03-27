package com.example.DuAnMau_PH63816.top_product.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DuAnMau_PH63816.R;
import com.example.DuAnMau_PH63816.top_product.TopSellingProductsScreen;
import com.example.DuAnMau_PH63816.top_product.model.TopSellingProductItem;

import java.util.List;

public class TopSellingProductAdapter extends RecyclerView.Adapter<TopSellingProductAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<TopSellingProductItem> items;

    public TopSellingProductAdapter(Context context, List<TopSellingProductItem> items) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /// Tạo giao diện cho từng item trong danh sách top sản phẩm.
        View view = inflater.inflate(R.layout.item_top_selling_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /// Gán dữ liệu của từng sản phẩm lên item view.
        TopSellingProductItem item = items.get(position);
        holder.tvRank.setText(String.valueOf(position + 1));
        holder.tvProductName.setText(item.getProductName());
        holder.tvSoldQuantity.setText("Đã bán: " + item.getSoldQuantity());
        holder.tvRevenue.setText("Doanh thu: " + TopSellingProductsScreen.formatCurrency(item.getRevenue()));

        if (item.getImageRes() != 0) {
            holder.imgProduct.setImageResource(item.getImageRes());
        } else {
            holder.imgProduct.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void updateItems(List<TopSellingProductItem> newItems) {
        /// Cập nhật lại danh sách mới sau khi thống kê xong.
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        /// Ánh xạ các view trong layout item_top_selling_product.
        final TextView tvRank;
        final ImageView imgProduct;
        final TextView tvProductName;
        final TextView tvSoldQuantity;
        final TextView tvRevenue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tvRank);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvSoldQuantity = itemView.findViewById(R.id.tvSoldQuantity);
            tvRevenue = itemView.findViewById(R.id.tvRevenue);
        }
    }
}
