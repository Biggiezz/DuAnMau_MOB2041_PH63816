package com.example.DuAnMau_PH63816.product;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ImageViewCompat;

import com.example.DuAnMau_PH63816.R;
import com.squareup.picasso.Picasso;

import java.util.Map;

public final class ProductImageResolver {

    public static final String DEFAULT_IMAGE_NAME = "ic_set_sushi";

    private static final Map<String, String> LEGACY_IMAGE_BY_PRODUCT_NAME = createLegacyImageMap();

    private ProductImageResolver() {
    }

    public static void loadInto(@NonNull ImageView target, @Nullable String imageValue) {
        ImageViewCompat.setImageTintList(target, null);
        target.clearColorFilter();

        String image = imageValue != null ? imageValue.trim() : "";
        if (isUriOrUrl(image)) {
            Picasso.get()
                    .load(image)
                    .placeholder(getPlaceholderRes())
                    .error(getPlaceholderRes())
                    .fit()
                    .into(target);
            return;
        }

        Picasso.get()
                .load(resolveDrawableResId(target.getContext(), image))
                .placeholder(getPlaceholderRes())
                .error(getPlaceholderRes())
                .fit()
                .into(target);
    }

    @NonNull
    public static String normalizeForStorage(
            @NonNull Context context,
            @Nullable String productName,
            @Nullable String imageValue
    ) {
        String image = imageValue != null ? imageValue.trim() : "";
        String mappedImageName = mappedImageName(productName);
        if (TextUtils.isEmpty(image)) {
            return fallbackImageName(productName);
        }

        if (isUriOrUrl(image)) {
            return image;
        }

        Integer resId = parseInteger(image);
        if (resId != null) {
            if (!TextUtils.isEmpty(mappedImageName)) {
                return mappedImageName;
            }
            String resourceName = resolveResourceEntryName(context, resId);
            if (!TextUtils.isEmpty(resourceName)) {
                return resourceName;
            }
            return fallbackImageName(productName);
        }

        int drawableRes = findDrawableByName(context, image);
        if (drawableRes != 0) {
            return image;
        }

        return fallbackImageName(productName);
    }

    @DrawableRes
    public static int resolveDrawableResId(@NonNull Context context, @Nullable String imageValue) {
        String image = imageValue != null ? imageValue.trim() : "";
        if (TextUtils.isEmpty(image)) {
            return getPlaceholderRes();
        }

        Integer legacyResId = parseInteger(image);
        if (legacyResId != null) {
            String resourceName = resolveResourceEntryName(context, legacyResId);
            if (!TextUtils.isEmpty(resourceName)) {
                int stableResId = findDrawableByName(context, resourceName);
                if (stableResId != 0) {
                    return stableResId;
                }
            }
            return getPlaceholderRes();
        }

        int drawableResId = findDrawableByName(context, image);
        if (drawableResId != 0) {
            return drawableResId;
        }

        return getPlaceholderRes();
    }

    @DrawableRes
    public static int getPlaceholderRes() {
        return R.drawable.bg_product_placeholder;
    }

    @NonNull
    private static String fallbackImageName(@Nullable String productName) {
        String byName = mappedImageName(productName);
        if (!TextUtils.isEmpty(byName)) {
            return byName;
        }
        return DEFAULT_IMAGE_NAME;
    }

    @Nullable
    private static String mappedImageName(@Nullable String productName) {
        if (TextUtils.isEmpty(productName)) {
            return null;
        }
        return LEGACY_IMAGE_BY_PRODUCT_NAME.get(productName);
    }

    private static boolean isUriOrUrl(@Nullable String image) {
        if (TextUtils.isEmpty(image)) {
            return false;
        }
        return image.startsWith("http://")
                || image.startsWith("https://")
                || image.startsWith("content://")
                || image.startsWith("file://")
                || image.startsWith("android.resource://");
    }

    @Nullable
    private static Integer parseInteger(@Nullable String rawValue) {
        if (TextUtils.isEmpty(rawValue)) {
            return null;
        }
        try {
            return Integer.parseInt(rawValue);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private static int findDrawableByName(@NonNull Context context, @NonNull String resourceName) {
        int drawableResId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
        if (drawableResId != 0) {
            return drawableResId;
        }
        return context.getResources().getIdentifier(resourceName, "mipmap", context.getPackageName());
    }

    @Nullable
    private static String resolveResourceEntryName(@NonNull Context context, int resId) {
        try {
            String resourceType = context.getResources().getResourceTypeName(resId);
            if (!"drawable".equals(resourceType) && !"mipmap".equals(resourceType)) {
                return null;
            }
            return context.getResources().getResourceEntryName(resId);
        } catch (Resources.NotFoundException ignored) {
            return null;
        }
    }

    @NonNull
    private static Map<String, String> createLegacyImageMap() {
        return Map.of("Mì Ramen Tonkotsu", "ic_ramen",
                "Kem Matcha Premium", "ic_icream_matcha",
                "Sushi Set Omakase", "ic_set_sushi",
                "Nước suối", "img_nuoc_suoi",
                "Topping trứng ngâm", "img_trung_ngam",
                "Salad cá hồi", "img_salad",
                "Trà đào cam sả", "img_tra_dao",
                "Takoyaki sốt mayo", "img_takoyaki",
                "Voucher giảm giá", "logo",
                "Phụ phí phục vụ", "logo");
    }
}
