package com.rookies.ecommerce.utils;

import java.text.Normalizer;

public class SlugUtil {

    public static String createSlug(String name) {
        String slug = name.toLowerCase();

        // Loại bỏ dấu tiếng Việt hoặc các dấu từ ký tự đặc biệt
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        slug = slug.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // Thay thế khoảng trắng và các ký tự không phải chữ/số bằng dấu gạch ngang
        slug = slug.replaceAll("[^a-z0-9]+", "-");

        // Loại bỏ dấu gạch ngang ở đầu và cuối chuỗi
        slug = slug.replaceAll("^-|-$", "");

        return slug;
    }

}
