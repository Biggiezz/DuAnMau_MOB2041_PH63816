package com.example.DuAnMau_PH63816.product.data;

import androidx.annotation.NonNull;

import com.example.DuAnMau_PH63816.product.model.CartItem;
import com.example.DuAnMau_PH63816.product.model.Product;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class CartManager {

    public interface OnCartChangedListener {
        void onCartChanged(int totalQuantity);
    }

    private static final LinkedHashMap<Integer, CartItem> ITEMS = new LinkedHashMap<>();
    private static final Set<OnCartChangedListener> LISTENERS = new LinkedHashSet<>();

    private CartManager() {
    }

    public static void addProduct(@NonNull Product product) {
        int productId = getStableProductId(product);
        CartItem existingItem = ITEMS.get(productId);
        if (existingItem == null) {
            ITEMS.put(productId, new CartItem(copyProduct(product), 1));
            notifyCartChanged();
            return;
        }
        existingItem.setQuantity(existingItem.getQuantity() + 1);
        notifyCartChanged();
    }

    public static void increaseQuantity(@NonNull Product product) {
        addProduct(product);
    }

    public static void decreaseQuantity(@NonNull Product product) {
        int productId = getStableProductId(product);
        CartItem existingItem = ITEMS.get(productId);
        if (existingItem == null) {
            return;
        }
        int nextQuantity = existingItem.getQuantity() - 1;
        if (nextQuantity <= 0) {
            ITEMS.remove(productId);
            notifyCartChanged();
            return;
        }
        existingItem.setQuantity(nextQuantity);
        notifyCartChanged();
    }

    public static List<CartItem> getItems() {
        return new ArrayList<>(ITEMS.values());
    }

    public static int getTotalQuantity() {
        int total = 0;
        for (CartItem item : ITEMS.values()) {
            total += item.getQuantity();
        }
        return total;
    }

    public static long getSubtotal() {
        long subtotal = 0L;
        for (CartItem item : ITEMS.values()) {
            subtotal += parsePrice(item.getProduct().getPriceLabel()) * item.getQuantity();
        }
        return subtotal;
    }

    public static String formatCurrency(long amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#,###", symbols);
        return formatter.format(amount) + "đ";
    }

    public static void addOnCartChangedListener(OnCartChangedListener listener) {
        if (listener == null) {
            return;
        }
        LISTENERS.add(listener);
        listener.onCartChanged(getTotalQuantity());
    }

    public static void removeOnCartChangedListener(OnCartChangedListener listener) {
        if (listener == null) {
            return;
        }
        LISTENERS.remove(listener);
    }

    private static int getStableProductId(@NonNull Product product) {
        if (product.getId() > 0) {
            return product.getId();
        }
        return product.getName().hashCode();
    }

    private static Product copyProduct(@NonNull Product product) {
        Product copy = new Product();
        copy.setId(product.getId());
        copy.setName(product.getName());
        copy.setPriceLabel(product.getPriceLabel());
        copy.setStockLabel(product.getStockLabel());
        copy.setImage(product.getImage());
        copy.setCategory(product.getCategory());
        copy.setUnit(product.getUnit());
        copy.setDate(product.getDate());
        copy.setStatus(product.getStatus());
        return copy;
    }

    private static long parsePrice(String priceLabel) {
        if (priceLabel == null || priceLabel.trim().isEmpty()) {
            return 0L;
        }
        String normalized = priceLabel
                .replace("đ", "")
                .replace("k", "")
                .replace("K", "")
                .replace(".", "")
                .replace(",", "")
                .trim();
        if (normalized.isEmpty()) {
            return 0L;
        }
        try {
            return Long.parseLong(normalized);
        } catch (NumberFormatException exception) {
            return 0L;
        }
    }

    private static void notifyCartChanged() {
        int totalQuantity = getTotalQuantity();
        for (OnCartChangedListener listener : new ArrayList<>(LISTENERS)) {
            listener.onCartChanged(totalQuantity);
        }
    }
}
